package nz.ac.app.metlink;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.GetRecentContextCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookTicket extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    DatabaseHelper myDb;
    Button btnNext;
    Spinner spinnerTrainLIne, spinnerSource, spinnerDest, spinnerPassen, spinnerTicketType;
    TextView errorTextS, errorTextD,errorTextE,errorTrainLine,errorT;
    String Email_string="", Mobile_num="";
    String json_string="";
    private ArrayList<TrainLine> listItems;
    private ArrayList<TrainStations> listItems1;
    private ArrayList<TrainStations> listItems2;
    String TrainLine_url="http://tranzmetrometlink.com/TrainLineSpinner.php";
    String TrainSourceStations_url="http://tranzmetrometlink.com/TrainSourceStationSpinner.php";
    String TrainDestStations_url="http://tranzmetrometlink.com/TrainDestStationSpinner.php";

    ArrayAdapter<String> adapter;
    String result="";
    JSONObject jsonObject;
    JSONArray jsonArray;
    AlertDialog alertDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        myDb = new DatabaseHelper(this);
        int whitecolor= Color.WHITE;
        View view=this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);
        btnNext = (Button) findViewById(R.id.buttonNext);
        spinnerTrainLIne = (Spinner) findViewById(R.id.spinnerTrainLine);
        spinnerSource = (Spinner) findViewById(R.id.spinnerSource);
        spinnerDest = (Spinner) findViewById(R.id.spinnerDest);
        spinnerPassen = (Spinner) findViewById(R.id.spinnerPassenType);
        spinnerTicketType = (Spinner) findViewById(R.id.spinnerTicketType);

        errorTextS = (TextView) findViewById(R.id.textViewSError);
        errorTextD = (TextView) findViewById(R.id.textViewDError);
        errorTextE=(TextView)findViewById(R.id.textViewEError);
        errorTrainLine=(TextView)findViewById(R.id.textViewerrortrainline);
        errorT=(TextView)findViewById(R.id.textViewerrorT);
        btnNext.setOnClickListener(this);
        listItems=new ArrayList<TrainLine>();
        listItems1=new ArrayList<TrainStations>();
        listItems2=new ArrayList<TrainStations>();


        loadTrainLineSpinnerData();

        spinnerTrainLIne.setOnItemSelectedListener(this);
        Bundle extras= getIntent().getExtras();
        if(extras!=null) {
            Email_string = extras.getString("Email");
         //   Mobile_num = extras.getString("Mobile_Num");
        }
       // Toast.makeText(BookTicket.this, Email_string,Toast.LENGTH_LONG).show();
       // Toast.makeText(BookTicket.this, Mobile_num,Toast.LENGTH_LONG).show();

    }

    //Implementation of OnItemSelectedListener

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //11.	http://www.androidhive.info/2012/06/android-populating-spinner-data-from-sqlite-database/ (Spinner code)

        if(spinnerTrainLIne.getSelectedItem().toString().equals("Select Train Line"))
        {
            //spinnerSource.setAdapter(null);
            List<String> lablesSource = new ArrayList<String>();
            List<String> lablesDest = new ArrayList<String>();
            lablesSource.add("Select Source Station");
            lablesDest.add("Select Destination Station");


            // Creating adapter for spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, lablesSource);
            ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, lablesDest);

            // Drop down layout style - list view with radio button
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinnerSource.setAdapter(spinnerAdapter);
            spinnerDest.setAdapter(spinnerAdapter1);



        }
        else {
           // spinnerSource.setAdapter(null);
            //spinnerDest.setAdapter(null);
            Toast.makeText(getApplicationContext(),parent.getSelectedItem().toString() + " Selected" , Toast.LENGTH_SHORT).show();
            loadSourceStationSpinnerData();
            loadDestStationSpinnerData();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
        //Use JSON object and php script file address to access the data from database
         // Function to load the Train Line Spinner from SQLite database

    private void loadTrainLineSpinnerData() {

        GetTrainLineCategories getTrainLineCategories=new GetTrainLineCategories(this);
        getTrainLineCategories.execute();
    }
    /**
     * Adding TrainLine spinner data
     * */
    private void populateSpinnerTrainLine() {

        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < listItems.size(); i++) {
            lables.add(listItems.get(i).getTrainLineName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerTrainLIne.setAdapter(spinnerAdapter);

    }

    /**
     * Async task to get all TrainLine categories
     * */
    private class GetTrainLineCategories extends AsyncTask<Void, Void, Void> {
       Context ctx;
       private final ProgressDialog pDialog=new ProgressDialog(BookTicket.this);

        GetTrainLineCategories(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(ctx).create();
            pDialog.setMessage("Fetching Train Line categories..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONfunctions jsonParser = new JSONfunctions();
            String json = jsonParser.getJSONformURL(TrainLine_url);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Train_Line_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            TrainLine cat = new TrainLine(catObj.getString("Train_Line_Name"));
                            listItems.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }


            populateSpinnerTrainLine();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }



//..................................................................................................

    // Function to load the Source Station Names Spinner from SQLite database

    private void loadSourceStationSpinnerData() {

        String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        GetSourceTrainStations getSourceTrainStations=new GetSourceTrainStations(this);
        getSourceTrainStations.execute(Train_line_Name);

    }
    /**
     * Adding TrainStation spinner data
     * */
    private void populateSpinnerSource() {

        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < listItems1.size(); i++) {
            lables.add(listItems1.get(i).gettrainStationName());
           // Toast.makeText(BookTicket.this,listItems1.get(i).gettrainStationName().toString(),Toast.LENGTH_LONG).show();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSource.setAdapter(spinnerAdapter);

    }

    /**
     * Async task to get all TrainStation categories
     * */
    private class GetSourceTrainStations extends AsyncTask<String, Void, Void> {
       // String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
       private final ProgressDialog pDialog=new ProgressDialog(BookTicket.this);

       Context ctx;

        GetSourceTrainStations(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            listItems1.clear();
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(ctx).create();

            pDialog.setMessage("Fetching Train Stations categories..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... params) {
            String Line_Name=params[0];

            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURL(TrainSourceStations_url,Line_Name);


            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Train_Station_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            TrainStations cat = new TrainStations(catObj.getString("Train_Station_Name"));
                            listItems1.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            populateSpinnerSource();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }



//........................................................................................................................................

//...........................................................................................................................................

    // Function to load the Destination Station Names Spinner from SQLite database



    private void loadDestStationSpinnerData() {
        String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        GetDestTrainStations getDestTrainStations=new GetDestTrainStations(this);
        getDestTrainStations.execute(Train_line_Name);

    }
    /**
     * Adding TrainStationDest spinner data
     * */
    private void populateDestSource() {
        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < listItems2.size(); i++) {
            lables.add(listItems2.get(i).gettrainStationName());
            // Toast.makeText(BookTicket.this,listItems1.get(i).gettrainStationName().toString(),Toast.LENGTH_LONG).show();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerDest.setAdapter(spinnerAdapter);

    }

    /**
     * Async task to get all TrainStationDest categories
     * */
    private class GetDestTrainStations extends AsyncTask<String, Void, Void> {
        // String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        private final ProgressDialog pDialog=new ProgressDialog(BookTicket.this);
        Context ctx;

        GetDestTrainStations(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            listItems2.clear();
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(ctx).create();

            pDialog.setMessage("Fetching Train Stations categories..");
            pDialog.setCancelable(false);
            pDialog.show();

        }
// 28.	https://www.youtube.com/watch?v=qMJo15o4pKE(Login json)
        @Override
        protected Void doInBackground(String... params) {
            String Line_Name=params[0];

            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURL(TrainDestStations_url,Line_Name);


            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Train_Station_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            TrainStations cat = new TrainStations(catObj.getString("Train_Station_Name"));
                            listItems2.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            populateDestSource();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }



    @Override
    public void onClick(View view) {

        int flag = 0;
        String Fare = "";
        String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        String Passen_Type_Name = spinnerPassen.getSelectedItem().toString();
        String Source_Station = spinnerSource.getSelectedItem().toString();
        String Dest_Station = spinnerDest.getSelectedItem().toString();
        String TicketType = spinnerTicketType.getSelectedItem().toString();


        //Validating that source and destination stations cannot be same

        if (spinnerSource.getSelectedItem().toString().equals(spinnerDest.getSelectedItem().toString())) {

            errorTextS.setTextColor(Color.RED);
            errorTextS.setText("Source and Destination cannot be same");
            errorTextD.setTextColor(Color.RED);
            errorTextD.setText("Source and Destination cannot be same");
            flag = 1;
        }
        else if( spinnerSource.getSelectedItem().toString().equals("Select Source Station") ){
            errorTextS.setTextColor(Color.RED);
            errorTextS.setText("Select Source Station");
            flag=1;
        }
        else {
            errorTextS.setText("");
            flag = 0;
        }
         if( spinnerDest.getSelectedItem().toString().equals("Select Destination Station")){
            errorTextD.setTextColor(Color.RED);
            errorTextD.setText("Select Destination Station");
            flag=1;
        }
        else
         {
             errorTextD.setText("");
             flag=0;
         }

        if( spinnerTrainLIne.getSelectedItem().toString().equals("Select Train Line")){
            errorTrainLine.setTextColor(Color.RED);
            errorTrainLine.setText("Select Train Line");
            flag=1;
        }
        else
        {
            errorTrainLine.setText("");
            flag=0;
        }


        if (spinnerPassen.getSelectedItem().toString().equals("Select Either Adult or Child")) {
            errorTextE.setTextColor(Color.RED);
            errorTextE.setText("Select Adult or Child");
            flag = 1;
        } else {
            errorTextE.setText("");
            flag = 0;
        }

        if (spinnerTicketType.getSelectedItem().toString().equals("Select Ticket Type")) {
            errorT.setTextColor(Color.RED);
            errorT.setText("Select Monthly Ticket");
            flag = 1;
        } else {
            errorT.setText("");
            flag = 0;
        }


        // If there is no error then press next to book ticket and know the fare.


        if (flag == 0) {
            if(TicketType.equals("Monthly Ticket")) {
                Intent intent = new Intent(BookTicket.this, MonthlyPass.class);
                Bundle extras = new Bundle();
                extras.putString("Email", Email_string);
                extras.putString("Mobile_num", Mobile_num);
                extras.putString("Train_line_Name", Train_line_Name);
                extras.putString("Passen_Type_Name", Passen_Type_Name);
                extras.putString("Source_Station", Source_Station);
                extras.putString("Dest_Station", Dest_Station);
                extras.putString("TicketType",TicketType);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        }
    }

    public void homeClicked(View view)
    {
        Intent intent=new Intent(BookTicket.this, MainActivity.class);
        Bundle extras=new Bundle();
        extras.putString("Email",Email_string);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }


    public void thumbsupClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(BookTicket.this);
        builder.setTitle("Like Us On Facebook!!!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent=new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/TranzMetroMetlink/"));
                startActivity(intent);

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //17.	http://stackoverflow.com/questions/19286970/using-intents-to-pass-data-between-activities-in-android (value transfer from one intent to another)
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog1 = builder.create();
        alertDialog1.show();
    }
}
