package nz.ac.app.metlink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fare_Enquiry extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    DatabaseHelper myDb;
    Button buttonCalculate;
    Spinner spinnerTrainLIne, spinnerSource, spinnerDest;
    TextView errorText, errorText1;
    ListView ListViewFare;
    ArrayList<String> FareDetails=new ArrayList<String>();
   ArrayAdapter<String> adapter;
   private ArrayList<TrainLine> listItems;
    private ArrayList<TrainStations> listItems1;
    private ArrayList<TrainStations> listItems2;
    private ArrayList<PassengerTicketDetails> listItemsP;

    String TrainLine_url="http://tranzmetrometlink.com/TrainLineSpinner.php";
    String TrainSourceStations_url="http://tranzmetrometlink.com/TrainSourceStationSpinner.php";
    String TrainDestStations_url="http://tranzmetrometlink.com/TrainDestStationSpinner.php";
    String Ticket_url="http://tranzmetrometlink.com/FareEnquiry.php";
    AlertDialog.Builder alertDialog;
    int status=0;
    String stdTicket,TentripTicket,MonthlyTicket,OffpeakTicket,Email_string;
    private Session session;
    public static Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare__enquiry);
        myDb = new DatabaseHelper(this);
        int whitecolor= Color.WHITE;
        View view=this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);




        buttonCalculate = (Button) findViewById(R.id.buttonCalculate);
        spinnerTrainLIne = (Spinner) findViewById(R.id.spinnerTrainLine);
        spinnerSource = (Spinner) findViewById(R.id.spinnerSource);
        spinnerDest = (Spinner) findViewById(R.id.spinnerDest);
        errorText = (TextView) findViewById(R.id.textViewSError);
        errorText1 = (TextView) findViewById(R.id.textViewDError);
        ListViewFare=(ListView) findViewById(R.id.listViewFareDetails);
        listItemsP=new ArrayList<PassengerTicketDetails>();
        buttonCalculate.setOnClickListener(this);
        loadTrainLineSpinnerData();
//        ListViewFare.setAdapter();
        spinnerTrainLIne.setOnItemSelectedListener(this);
      //  adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,FareDetails);
        listItems=new ArrayList<TrainLine>();
        listItems1=new ArrayList<TrainStations>();
        listItems2=new ArrayList<TrainStations>();

        ctx = this;
        session = new Session(ctx);


        if (session.loggedIn() == true) {

            // 19.	http://stackoverflow.com/questions/8822553/null-value-in-getintent-getextras-getstring (Moving data through intent)
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Email_string = extras.getString("Email");
            }

        }
    }
    //Implementation of OnItemSelectedListener

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(),parent.getSelectedItem().toString() + " Selected" , Toast.LENGTH_LONG).show();
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
            //spinnerSource.setAdapter(null);
            //spinnerDest.setAdapter(null);

            loadSourceStationSpinnerData();
            loadDestStationSpinnerData();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


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
        private final ProgressDialog pDialog=new ProgressDialog(Fare_Enquiry.this);

        GetTrainLineCategories(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(Fare_Enquiry.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Train Line Details..");
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
        private final ProgressDialog pDialog=new ProgressDialog(Fare_Enquiry.this);

        Context ctx;

        GetSourceTrainStations(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            listItems1.clear();
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(Fare_Enquiry.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Source Stations Details..");
            pDialog.setIndeterminate(false);
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
        private final ProgressDialog pDialog=new ProgressDialog(Fare_Enquiry.this);
        Context ctx;

        GetDestTrainStations(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            listItems2.clear();
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(Fare_Enquiry.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Destination Stations Details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

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
        int Train_line_id = spinnerTrainLIne.getSelectedItemPosition() + 1;
        String Source_Station = spinnerSource.getSelectedItem().toString();
        String Dest_Station = spinnerDest.getSelectedItem().toString();

        //Validating that source and destination stations cannot be same

        if (spinnerSource.getSelectedItem().toString().equals(spinnerDest.getSelectedItem().toString())) {

            errorText.setTextColor(Color.RED);
            errorText.setText("Source and Destination cannot be same");

            errorText1.setTextColor(Color.RED);
            errorText1.setText("Source and Destination cannot be same");
            Toast.makeText(Fare_Enquiry.this, "Source and Destination cannot be same", Toast.LENGTH_LONG).show();
            flag = 1;
        } else {
            errorText.setText("");
            errorText1.setText("");
            flag = 0;
        }

        // If there is no error then press next to book ticket and know the fare.

        if (flag == 0) {
            loadTicketData();

        }
    }



    // Load the Fare of Selected route

    private void loadTicketData()
    {
        String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        String Source_Station = spinnerSource.getSelectedItem().toString();
        String Dest_Station = spinnerDest.getSelectedItem().toString();

        //call the do in background function to make HTTP connection with cloud database.

        GetTicketData getTicketData=new GetTicketData(this);
        getTicketData.execute(Train_line_Name,Source_Station,Dest_Station);
    }
    private void populateTicketData(String stdTicket, String TentripTicket,String MonthlyTicket, String OffpeakTicket) {

       List<String> lables = new ArrayList<String>();
        lables.add("Standard Ticket:"+stdTicket);
        lables.add("Ten Trip Ticket:"+TentripTicket);
        lables.add("Monthly Ticket:"+MonthlyTicket);
        lables.add("Off Peak Ticket:"+OffpeakTicket);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lables);
        ListViewFare.setAdapter(arrayAdapter);
    }

    /**
     * Async task to get Ticket details of a particular passenger
     * */
    private class GetTicketData extends AsyncTask<String, Void, String> {
        // String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        private final ProgressDialog pDialog=new ProgressDialog(Fare_Enquiry.this);
        Context ctx;

        GetTicketData(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(Fare_Enquiry.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Fare Details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
           String Train_line_Name=params[0];
            String Source_Station=params[1];
            String Dest_Station=params[2];
            String DialogStatus="";



            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLFareEnquiry(Ticket_url,Train_line_Name,Source_Station,Dest_Station);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    String Ticket_Num,Ticket_Type,Ticket_Purchase_Date,Ticket_Expiry_Date,Ticket_Fare;
                    JSONObject jsonObj = new JSONObject(json);


                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Train_Fare_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = categories.getJSONObject(i);

                            if (catObj.getString("Fare_Status") != null) {
                                if (catObj.getString("Fare_Status").equals("No Fare Found")) {
                                    DialogStatus = "No Fare Found....Try Again for another route. Sorry for inconvenience !!!!";
                                    status=1;

                                } else {
                                    JSONObject cobj=catObj.getJSONObject("Fare_Status");
                                    stdTicket = cobj.getString("Std_Ticket");
                                    TentripTicket = cobj.getString("TenTrip_Ticket");
                                    OffpeakTicket = cobj.getString("OffPeak_Ticket");
                                    MonthlyTicket = cobj.getString("Monthly_Ticket");
                                    DialogStatus = "Click ok to view Fare for selected route. Thanks !!!! " ;

                                    status = 2;

                                }
                            }
                        }
                    }



                    return DialogStatus;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            alertDialog.setMessage(result);

            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {

                            dialog.dismiss();
                            if(status==2)
                            {
                                populateTicketData(stdTicket,TentripTicket,MonthlyTicket,OffpeakTicket);
                            }

                            else
                            {

                                Intent intent=new Intent(Fare_Enquiry.this,Fare_Enquiry.class);
                                startActivity(intent);
                                finish();
                            }


                        }
                    });
            AlertDialog alert=alertDialog.create();
            alert.show();
            if (pDialog.isShowing()) {

                pDialog.dismiss();
            }

        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    public void homeClicked(View view)
    {



        Intent intent=new Intent(Fare_Enquiry.this, MainActivity.class);
        Bundle extra=new Bundle();
        extra.putString("Email", Email_string);
        intent.putExtras(extra);
        startActivity(intent);
        finish();
    }


     public void thumbsupClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Fare_Enquiry.this);
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
