/*
package nz.ac.app.metlink;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Insert_Update_Fare extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    DatabaseHelper myDb;
    EditText editTxtStdFare, editTxtMonthlyFare, editTextTenTrip, editTextOffPeak;
    Button btnInsert;
    Spinner spinnerTrainLIne, spinnerSource, spinnerDest, spinnerPassen;
    TextView errorText, errorText1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert__update__fare);
        myDb = new DatabaseHelper(this);
       // editTxt1 = (EditText) findViewById(R.id.editText1);
       // editTxt2 = (EditText) findViewById(R.id.editText2);

        btnInsert = (Button) findViewById(R.id.buttonInsert);
        spinnerTrainLIne=(Spinner) findViewById(R.id.spinnerTrainLine);
        spinnerSource=(Spinner) findViewById(R.id.spinnerSource);
        spinnerDest=(Spinner) findViewById(R.id.spinnerDest);
        spinnerPassen=(Spinner)findViewById(R.id.spinnerPassenType);
        editTxtStdFare=(EditText)findViewById(R.id.editTextStdFare);
        editTxtMonthlyFare=(EditText)findViewById(R.id.editTextMonthlyFare);
        editTextTenTrip=(EditText)findViewById(R.id.editTextTenTripFare);
        editTextOffPeak=(EditText)findViewById(R.id.editTextOffPeakFare);
        errorText = (TextView) findViewById(R.id.textViewSError);
        errorText1 = (TextView) findViewById(R.id.textViewDError);
        btnInsert.setOnClickListener(this);
        loadTrainLineSpinnerData();
        spinnerTrainLIne.setOnItemSelectedListener(this);

        //spinnerSource.setOnItemSelectedListener(this);


    }
    //Implementation of OnItemSelectedListener

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label="", label1="";

                label= parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "You Selected : "+label, Toast.LENGTH_LONG).show();
                loadSourceStationSpinnerData(label);
                loadDestStationSpinnerData(label);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    // Function to load the Train Line Spinner from SQLite database

    private void loadTrainLineSpinnerData()
    {
        DatabaseHelper db=new DatabaseHelper((getApplicationContext()));
        List<String> labels=db.getAllLables();
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTrainLIne.setAdapter(dataAdapter);
    }

    // Function to load the Source Station Names Spinner from SQLite database

    private void loadSourceStationSpinnerData(String station)
    {
        DatabaseHelper db=new DatabaseHelper((getApplicationContext()));
        List<String> labels=db.getTrainStations(station);
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSource.setAdapter(dataAdapter);
    }
    // Function to load the Source Station Names Spinner from SQLite database

    private void loadDestStationSpinnerData(String station)
    {
        DatabaseHelper db=new DatabaseHelper((getApplicationContext()));
        List<String> labels=db.getTrainStationsDest(station);
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDest.setAdapter(dataAdapter);
    }


    @Override
    public void onClick(View view) {

        int flag = 0;
        int Train_line_id = spinnerTrainLIne.getSelectedItemPosition()+1;
        int Passen_Type_Id = spinnerPassen.getSelectedItemPosition()+1;
        String Source_Station = spinnerSource.getSelectedItem().toString();
        String Dest_Station = spinnerDest.getSelectedItem().toString();

        //Validating that source and destination stations cannot be same

        if (spinnerSource.getSelectedItem().toString().equals(spinnerDest.getSelectedItem().toString())) {

            errorText.setTextColor(Color.RED);
            errorText.setText("Source and Destination cannot be same");

            errorText1.setTextColor(Color.RED);
            errorText1.setText("Source and Destination cannot be same");
            Toast.makeText(Insert_Update_Fare.this, "Source and Destination cannot be same", Toast.LENGTH_LONG).show();
            flag = 1;
        }

         else {
            errorText.setText("");
            errorText1.setText("");
            flag = 0;
        }
        //Validating that none of the fare text box is empty

        if(editTxtStdFare.getText().toString().length()==0){
            editTxtStdFare.setError("Standard Fare Cannot be empty");
            flag=1;
        }
        else {
            editTxtStdFare.setError(null);
            flag=0;
        }

        if(editTextTenTrip.getText().toString().length()==0){
            editTextTenTrip.setError("Ten Trip Fare Cannot be empty");
            flag=1;
        }
        else {
            editTextTenTrip.setError(null);
            flag=0;
        }

        if(editTxtMonthlyFare.getText().toString().length()==0){
            editTxtMonthlyFare.setError("Monthly Fare Cannot be empty");
            flag=1;
        }
        else {
            editTxtMonthlyFare.setError(null);
            flag=0;
        }

        if(editTextOffPeak.getText().toString().length()==0){
            editTextOffPeak.setError("Off Peak Fare Cannot be empty");
            flag=1;
        }
        else {
            editTextOffPeak.setError(null);
            flag=0;
        }


        // If there is no error then do insertion


        if (flag == 0) {
            boolean isInserted = myDb.insertTrain_Fare(Passen_Type_Id ,Train_line_id, Source_Station, Dest_Station,editTxtStdFare.getText().toString() ,editTextTenTrip.getText().toString(),
                    editTxtMonthlyFare.getText().toString(),editTextOffPeak.getText().toString());

            if (isInserted = true) {
               // Toast.makeText(Insert_Update_Fare.this, "Train Fare is Inserted successfully", Toast.LENGTH_LONG).show();
                startActivity(getIntent());
            }
            else
                Toast.makeText(Insert_Update_Fare.this, "Train Fare not Inserted", Toast.LENGTH_LONG).show();
            clearInput();


        }
    }

    public void clearInput()
    {
        editTxtStdFare.setText("");
        editTextTenTrip.setText("");
        editTxtMonthlyFare.setText("");
        editTextOffPeak.setText("");

    }





    public void Btnback(View view)
    {
        Intent intent=new Intent(Insert_Update_Fare.this, AdminHome.class);
        startActivity(intent);
    }
*/
/* @Override
                public void onClick(View view) {
                boolean isInserted=myDb.insertPassenger_Type(editTxt2.getText().toString());
                    boolean isInserted=myDb.insertTrain_Line(editTxt1.getText().toString());
                    boolean isInserted=myDb.insertTrain_Station(editTxt1.getText().toString(), editTxt2.getText().toString());
                    if(isInserted=true) {
                        Toast.makeText(Insert_Update_Fare.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        startActivity(getIntent());
                    }
                    else
                        Toast.makeText(Insert_Update_Fare.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    clearInput();


                }

        public void clearInput()
        {
            editTxt2.setText("");

        }*//*


}
*/
