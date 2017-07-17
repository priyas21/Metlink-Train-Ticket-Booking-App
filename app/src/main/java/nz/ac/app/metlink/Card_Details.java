package nz.ac.app.metlink;

import android.*;
import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.CustomRenderedAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Card_Details extends AppCompatActivity implements View.OnClickListener{
    EditText editName,editCardNum,editCVVNum,editMobile;
    Spinner spinnerMon,spinnerYear;
    Button btnPay;
    String Email_string="",Fare="",MonthSelected="",TicketType="",PurchaseTicketDate="",ExpiryTicketDate="",PaymentStatus="",Ticket_Num="",MobileNum="";
    int YearSelected, Ticket_ID,status=0;
    int flag;
    AlertDialog.Builder alertDialog;
    String Card_url="http://tranzmetrometlink.com/PassengerBookingDetails.php";
    private final static int SMS_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card__details);
        int whitecolor = Color.WHITE;
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Email_string = extras.getString("Email");
            Fare = extras.getString("Fare");
            MonthSelected = extras.getString("MonthSelected");
            TicketType=extras.getString("TicketType");
        }
          //  Toast.makeText(Card_Details.this,Email_string,Toast.LENGTH_LONG).show();

        //Random Number generator class
        Random ran = new Random();
        Ticket_ID = (1000 + ran.nextInt(9000));
        Ticket_Num = Integer.toString(Ticket_ID);

        //Seperate the Month selected and put it in array to get the Month Name.

        String parts[]=MonthSelected.split("\\,");
        MonthSelected=parts[0];
        YearSelected=Integer.parseInt(parts[1]);


        editName = (EditText) findViewById(R.id.editTextName);
        editCardNum = (EditText) findViewById(R.id.editTextCardNum);
        editCVVNum = (EditText) findViewById(R.id.editTextCVV);
        editMobile = (EditText) findViewById(R.id.editTextMobile);
        spinnerMon = (Spinner) findViewById(R.id.spinnermonth);
        spinnerYear = (Spinner) findViewById(R.id.spinneryear);
        btnPay = (Button) findViewById(R.id.btnPayFare);
        btnPay.setOnClickListener(this);

        //Set Expiry date by checking which month is selected.
        switch (MonthSelected)
        {
            case "January":
                ExpiryTicketDate="31/01/"+YearSelected;
                break;
            case "February":
                int imonth=Calendar.FEBRUARY;
                Calendar calendar=new GregorianCalendar(YearSelected,imonth,1);
                int lastDay=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                ExpiryTicketDate=Integer.toString(lastDay)+"/02/"+YearSelected;
               // Toast.makeText(Card_Details.this,ExpiryTicketDate, Toast.LENGTH_LONG).show();

            case "March":
                ExpiryTicketDate="31/03/"+YearSelected;
                break;
            case "April":
                ExpiryTicketDate="30/04/"+YearSelected;
                break;
            case "May":
                ExpiryTicketDate="31/05/"+YearSelected;
                break;
            case "June":
                ExpiryTicketDate="30/06/"+YearSelected;
                break;
            case "July":
                ExpiryTicketDate="31/07/"+YearSelected;
                break;
            case "August":
                ExpiryTicketDate="31/08/"+YearSelected;
                break;
            case "September":
                ExpiryTicketDate="30/09/"+YearSelected;
                break;
            case "October":
                ExpiryTicketDate="31/10/"+YearSelected;
                break;
            case "November":
                ExpiryTicketDate="30/11/"+YearSelected;
                break;
            case "December":
                ExpiryTicketDate="31/12/"+YearSelected;
                break;

        }

        // Populate the spinner with next month till next 6 months
        List<String> year = new ArrayList<String>();


        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int prevYear = thisYear - 1;
        //Toast.makeText(Card_Details.this, thisYear, Toast.LENGTH_LONG).show();
        for (int i = prevYear; i <= prevYear + 12; i++) {
            int lables=i;
            year.add(Integer.toString(lables));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, year);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
    }



    public void backClicked(View view)
    {
        Intent intent=new Intent(Card_Details.this, BookTicket.class);
        Bundle extras=new Bundle();
        extras.putString("Email",Email_string);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View view) {
        String Name = editName.getText().toString();
        // String CardNum = editCardNum.getText().toString();
        // String CvvNum = editCVVNum.getText().toString();
         MobileNum = editMobile.getText().toString();


        String type = "CardDetails";
        flag=0;

        //Name
        if (editName.getText().toString().length() == 0) {
            editName.setError("Name cannot be Empty");
            editName.requestFocus();
            flag = 1;
        }
        //Card Number
        if (editCardNum.getText().toString().length() == 0) {
            editCardNum.setError("Card Number cannot be Empty");
            editCardNum.requestFocus();
            flag = 1;
        }  if (editCardNum.length() != 16) {
            editCardNum.setError("Card Number must be of 16 digits");
            editCardNum.requestFocus();
            flag = 1;
        }

        //CVV Number
        if (editCVVNum.getText().toString().length() == 0) {
            editCVVNum.setError("CVV Number cannot be Empty");
            editCVVNum.requestFocus();
            flag = 1;
        }  if (editCVVNum.length() != 3) {
            editCVVNum.setError("CVV Number must be of 3 digits");
            editCVVNum.requestFocus();
            flag = 1;
        }

        //Mobile Num Validate

        if (!validateMobile(editMobile.getText().toString())) {
            editMobile.setError("Invalid Mobile number");
            // editMobile.requestFocus();
            flag = 1;
        } else if (editMobile.getText().toString().length() == 0) {
            editMobile.setError("Mbbile Number cannot be empty");
            //editMobile.requestFocus();
            flag = 1;
        }

        if (flag == 0) {
            editName.setError(null);
            editCardNum.setError(null);
            editCVVNum.setError(null);
            editMobile.setError(null);
            Calendar c=Calendar.getInstance();
            Date Todaydate=c.getTime();
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
            String convertdateToday = null;
            convertdateToday=dateFormat.format(Todaydate);
            PurchaseTicketDate = convertdateToday;
           // Toast.makeText(Card_Details.this, Ticket_ID, Toast.LENGTH_LONG).show();
            PaymentStatus = "Payment Done Successfully";
            CheckInsertStatus();
        }



    }
    private void CheckInsertStatus()
    {

        GetInsertStatus getInsertStatus=new GetInsertStatus(this);
        getInsertStatus.execute();
    }

    /**
     * Async task to get all TrainLine categories
     * */
    private class GetInsertStatus extends AsyncTask<Void, Void, String> {
        Context ctx;
        private final ProgressDialog pDialog=new ProgressDialog(Card_Details.this);

        GetInsertStatus(Context ctx)
        {

            this.ctx=ctx;
        }



        @Override
        protected String doInBackground(Void... arg0) {
            String DialogStatus="";
            int f1=0;
            String firstAlpha="M";
            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLInsertCardDetails(Card_url, Ticket_Num, Email_string, MobileNum, PaymentStatus, TicketType, PurchaseTicketDate, ExpiryTicketDate, Fare);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Insert_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = categories.getJSONObject(i);


                            if (catObj.getString("InsertStatus") != null) {
                                if (catObj.getString("InsertStatus").equals("Insert Not Sucessfull!!!")) {
                                    DialogStatus = "Card Deatils are incorrect..Try Again!!!!";
                                    status=1;

                                } else {
                                    CheckSmsPermission();
                                    DialogStatus = "Payment Successfull..An SMS has been sent on your mobile number regarding Ticket Details...Thanks for taking Metlink Services!!!!";
                                   // status = 2;


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
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(Card_Details.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Wait till the payment is done..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            alertDialog.setMessage(result);

            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            CheckSmsPermission();
                            dialog.dismiss();
                            if(status==2)
                            {
                                sendSMS();
                                Intent intent = new Intent(Card_Details.this, ViewTicket.class);
                                Bundle extras=new Bundle();
                                extras.putString("Email",Email_string);
                                extras.putString("Mobile",MobileNum);
                                intent.putExtras(extras);
                                startActivity(intent);
                                clearInput();
                                finish();
                            }
                            else
                            {

                                Intent intent=new Intent(Card_Details.this,Card_Details.class);
                                Bundle extras=new Bundle();
                                extras.putString("Email",Email_string);
                                intent.putExtras(extras);
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

    private void CheckSmsPermission() {
        if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED) {
           status = 2;
        }
        else {
            String[] permissionRequested = {Manifest.permission.SEND_SMS};
            requestPermissions(permissionRequested,SMS_CODE );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == SMS_CODE) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                status = 2;
            }
            else {
                Toast.makeText(this, "Sms permission was not granted. Cannot send Ticket as sms" , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Card_Details.this, ViewTicket.class);
                Bundle extras=new Bundle();
                extras.putString("Email",Email_string);
                extras.putString("Mobile",MobileNum);
                intent.putExtras(extras);
                startActivity(intent);
                clearInput();
                finish();
            }
        }
    }

    // Reference: 30.	http://web.archive.org/web/20150303013635/http://mobiforge.com/design-development/sms-messaging-android
    public void sendSMS()
    {
        String message="Your Ticket Details:\n Ticket Num: "+ Ticket_Num +"\n" +
                "Ticket Type:"+ TicketType+"\n" +
                "Purchase Date:"+ PurchaseTicketDate+"\n" +
                "Expiry Date of Ticket:"+ ExpiryTicketDate+"\n" +
                "Ticket of Month:"+ MonthSelected+"\n" +
                "Fare:"+ Fare+"\n" +
                "Thanks for using Metlink Services!!!!"+"\n";
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,new Intent(this, Telephony.Sms.class),0);
        SmsManager sms=SmsManager.getDefault();

        ArrayList<String> parts = sms.divideMessage(message);


        sms.sendMultipartTextMessage(MobileNum,null,parts,null,null);
    }


    protected boolean validateMobile(String mobile)
    {
        String regexStr = "^[+]?[0-9]{10,13}$";



        if(mobile.length()<10 || mobile.length()>13 || mobile.matches(regexStr)==false  ) {
           return false;
        }


        else
            return true;


    }
    public void clearInput()
    {
        editName.setText("");
        editCardNum.setText("");
        editCVVNum.setText("");
        editMobile.setText("");

    }

    public void thumbsupClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Card_Details.this);
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
