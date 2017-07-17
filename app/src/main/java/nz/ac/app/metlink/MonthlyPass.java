package nz.ac.app.metlink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthlyPass extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText editTxtFare,editTextMonth;
    Spinner spinnerMonth;

    String Email_string,Mobile_num,Train_line_Name,Passen_Type_Name,Source_Station,Dest_Station,TicketType;
    String TrainFare_url="http://tranzmetrometlink.com/TrainFare.php";
    private ArrayList<TrainStations> listItems;
    String Fare,Monthselected;
    int status=0;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int whitecolor= Color.WHITE;
        View view=this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);
        setContentView(R.layout.activity_monthly_pass);
        listItems=new ArrayList<TrainStations>();
        editTxtFare=(EditText)findViewById(R.id.editTextFare);
        editTextMonth=(EditText)findViewById(R.id.editTextmonth);
        spinnerMonth=(Spinner)findViewById(R.id.spinnerMonth);
        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {

            Email_string=extras.getString("Email");
            Mobile_num=extras.getString("Mobile_num");
            Train_line_Name=extras.getString("Train_line_Name");
            Passen_Type_Name=extras.getString("Passen_Type_Name");
            Source_Station=extras.getString("Source_Station");
            Dest_Station=extras.getString("Dest_Station");
            TicketType=extras.getString("TicketType");

        }

        editTxtFare.setKeyListener(null);
        editTextMonth.setKeyListener(null);
        spinnerMonth.setOnItemSelectedListener(this);
//18.	http://stackoverflow.com/questions/15914705/populate-spinner-with-years-dynamically-in-android (Calendar)
        // Populate the spinner with next month till next 6 months
        List<String> months=new ArrayList<String>();
        String label="";
        int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
        int thisYear=Calendar.getInstance().get(Calendar.YEAR);
        thisMonth = thisMonth + 2;


        //Toast.makeText(MonthlyPass.this, thisYear, Toast.LENGTH_LONG).show();
        for(int i=0;i<=6;i++) {

                if (thisMonth > 12) {
                    thisMonth = 1;
                    thisYear += 1;
                }
                else
                {
                switch (thisMonth) {
                    case 1:

                        label="January,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 2:
                        label="February,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 3:
                        label="March,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 4:
                        label="April,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 5:
                        label="May,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 6:
                        label="June,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 7:
                        label="July,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 8:
                        label="August,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 9:
                        label="September,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 10:
                        label="October,"+thisYear;
                        months.add(label);
                        thisMonth = thisMonth + 1;
                        break;
                    case 11:
                        label="November,"+thisYear;
                        months.add(label );
                        thisMonth = thisMonth + 1;
                        break;
                    case 12:
                        label="December,"+thisYear;
                        months.add(label );
                        thisMonth = thisMonth + 1;
                        break;
                }
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinMonth = (Spinner)findViewById(R.id.spinnerMonth);
        spinMonth.setAdapter(adapter);

        loadFareData();

    }
    private void loadFareData()
    {
        GetFareData getFareData=new GetFareData(this);
        getFareData.execute(Train_line_Name,Passen_Type_Name,Source_Station,Dest_Station);

    }
    /**
     * Adding TrainFare data
     * */
    private void populateFareData() {
        List<String> lables = new ArrayList<String>();


        for (int i = 0; i < listItems.size(); i++) {
            lables.add(listItems.get(i).gettrainStationName());

        }
        if(status==1)
        {
            editTxtFare.setText("");
        }
        else {
            editTxtFare.setText(lables.toString().replace("]", "").replace("[", ""));
        }
        Fare=editTxtFare.getText().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        editTextMonth.setText(spinnerMonth.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        editTextMonth.setText("");

    }

    /**
     * Async task to get seleted Route TrainFare
     * */
    private class GetFareData extends AsyncTask<String, Void, String> {
        // String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        private final ProgressDialog pDialog=new ProgressDialog(MonthlyPass.this);
        Context ctx;

        GetFareData(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            editTxtFare.setText("");
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(MonthlyPass.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Train Fare..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String Line_Name=params[0];
            String PassenType=params[1];
            String SourceStation=params[2];
            String DestStation=params[3];
            String DialogStatus="";


            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLFare(TrainFare_url,Line_Name,PassenType,SourceStation,DestStation);


            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);

                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Train_Fare_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);

                            //
                            if(catObj.getString("Train_Station_Fare").equals("Fare for the selected route does not exist. Sorry for the inconvinience.!!!!"))
                            {
                                DialogStatus="Fare for the selected route does not exist. Sorry for the inconvinience.!!!!";
                                status=1;

                            }
                            else {
                                status=2;
                                DialogStatus="Fare for selected route exists.!!!";
                                TrainStations cat = new TrainStations(catObj.getString("Train_Station_Fare"));
                                listItems.add(cat);
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
                            if(status==1)
                            {

                                Intent intent=new Intent(MonthlyPass.this,BookTicket.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                populateFareData();
                            }
                        }
                    });
            AlertDialog alert=alertDialog.create();
            alert.show();
            if (pDialog.isShowing()) {

                pDialog.dismiss();
            }

            //populateFareData();
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }
    public void btnPayClicked(View view)
    {
        if(editTxtFare.getText().toString()!=null)
        {
//20.	http://stackoverflow.com/questions/31069017/null-value-in-getintent-getextras-getstring (get data through extra:bundle)
            Monthselected=spinnerMonth.getSelectedItem().toString();
            Intent intent=new Intent(MonthlyPass.this, Card_Details.class);
            Bundle extras=new Bundle();
            extras.putString("Email",Email_string);
            extras.putString("Fare",Fare);
            extras.putString("MonthSelected",Monthselected);
            extras.putString("TicketType",TicketType);
            intent.putExtras(extras);
            startActivity(intent);
            finish();
        }
    }

    public void backClicked(View view)
    {
        Intent intent=new Intent(MonthlyPass.this, BookTicket.class);
        Bundle extras=new Bundle();
        extras.putString("Email",Email_string);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }
    public void thumbsupClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MonthlyPass.this);
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
