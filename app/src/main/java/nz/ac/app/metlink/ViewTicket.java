package nz.ac.app.metlink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewTicket extends AppCompatActivity {

    public static Context ctx;
    AlertDialog.Builder alertDialog;
    private ArrayList<PassengerTicketDetails> listItems;
    TableLayout l1;
    int status=0;

   // PassengerAdapter passengerAdapter;

    String Email_string = "",Mobile="";
    String Ticket_url="http://tranzmetrometlink.com/ViewTicket.php";
    String TicketDel_url="http://tranzmetrometlink.com/deleteticket.php";
    ListView listViewticket;
   // ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        int whitecolor = Color.WHITE;
        int cyan=Color.CYAN;
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);
        listItems=new ArrayList<PassengerTicketDetails>();

     //   passengerAdapter=new PassengerAdapter(this,R.layout.row_layout);
       // listViewticket.setAdapter(passengerAdapter);

        l1=(TableLayout)findViewById(R.id.TableLayoutTicket);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Email_string = extras.getString("Email");
               // Toast.makeText(ViewTicket.this, Email_string, Toast.LENGTH_SHORT).show();
            }
            loadTicketData();

    }

    private void loadTicketData()
    {
        GetTicketData getTicketData=new GetTicketData(this);
        getTicketData.execute(Email_string);
    }

    /**
     * Adding TrainTicket data
     * */
  private void populateTicketData() {

      List<String> lables = new ArrayList<String>();

      ImageView iv1 = new ImageView(this);
      ImageView ivbarcode=new ImageView(this);


      for (int i = 0; i < listItems.size(); i++) {
          lables.add(listItems.get(i).getticketNum());
          lables.add(listItems.get(i).getTicket_Type());
          lables.add(listItems.get(i).getTicket_Purchase_Date());
          lables.add(listItems.get(i).getTicket_Expiry_Date());
          lables.add(listItems.get(i).getTicket_Fare());
//31.	http://stackoverflow.com/questions/18207470/adding-table-rows-dynamically-in-android
          TableRow row = new TableRow(this);
          TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1);
          row.setLayoutParams(lp);
          row.setBackgroundColor(Color.DKGRAY);

          final TextView t1 = new TextView(this);
          TextView t2 = new TextView(this);
          TextView t3 = new TextView(this);
          final TextView t4 = new TextView(this);
          TextView t5 = new TextView(this);
          iv1 = new ImageView(this);
          ivbarcode=new ImageView(this);

          t1.setPadding(1, 1, 1, 1);
          t2.setPadding(1, 1, 1, 1);
          t3.setPadding(1, 1, 1, 1);
          t4.setPadding(1, 1, 1, 1);
          t5.setPadding(1, 1, 1, 1);
          iv1.setPadding(2, 2, 2, 2);
          iv1.setId(i + 1);
          ivbarcode.setPadding(2, 2, 2, 2);
          ivbarcode.setId(i + 1);
          t1.setId(i + 1);
          t4.setId(i + 1);

          t1.setBackgroundColor(Color.WHITE);
          t2.setBackgroundColor(Color.WHITE);
          t3.setBackgroundColor(Color.WHITE);
          t4.setBackgroundColor(Color.WHITE);
          t5.setBackgroundColor(Color.WHITE);

          t1.setTextColor(Color.BLACK);
          t2.setTextColor(Color.BLACK);
          t3.setTextColor(Color.BLACK);
          t4.setTextColor(Color.BLACK);
          t5.setTextColor(Color.BLACK);

          // Toast.makeText(this,listItems.get(i).getticketNum(),Toast.LENGTH_LONG).show();
          t1.setText(listItems.get(i).getticketNum());
          t2.setText(listItems.get(i).getTicket_Type());
          t3.setText(listItems.get(i).getTicket_Purchase_Date());
          t4.setText(listItems.get(i).getTicket_Expiry_Date());
          t5.setText(listItems.get(i).getTicket_Fare());
          iv1.setImageResource(R.drawable.del);
          iv1.setBackgroundColor(Color.RED);
          ivbarcode.setImageResource(R.drawable.barcode);
          ivbarcode.setBackgroundColor(Color.WHITE);

          // On click of delete image
          iv1.setOnClickListener(new View.OnClickListener() {
              public String Ticket_Num;

              @Override
              public void onClick(View iv1) {
                  String id = Integer.toString(iv1.getId());
                  String expiryDate = (String) t4.getText();
                  Calendar c = Calendar.getInstance();
                  Date Todaydate = c.getTime();
                  Ticket_Num = (String) t1.getText();
                  //convert expiry date into date datatype
                  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                  Date converteddateExpiry = new Date();
                  String convertdateToday = "";
                  String Dateexpiry = "";
                  try {
                      converteddateExpiry = dateFormat.parse(expiryDate);
                      Dateexpiry = dateFormat.format(converteddateExpiry);
                      convertdateToday = dateFormat.format(Todaydate);
                  } catch (ParseException e) {
                      e.printStackTrace();
                  }
                  createAndShowAlertDialog(Ticket_Num, Dateexpiry, convertdateToday);
                /*  Toast.makeText(ViewTicket.this, t4.getText(), Toast.LENGTH_SHORT).show();
                  Toast.makeText(ViewTicket.this, t1.getText(), Toast.LENGTH_SHORT).show();
                  Toast.makeText(ViewTicket.this, Dateexpiry, Toast.LENGTH_SHORT).show();
                  Toast.makeText(ViewTicket.this, convertdateToday, Toast.LENGTH_SHORT).show();
*/
              }
          });
          ivbarcode.setOnClickListener(new View.OnClickListener() {
              public String Ticket_Num;
              @Override
              public void onClick(View view) {

                  Ticket_Num = (String) t1.getText();
                  Intent intent=new Intent(ViewTicket.this, BarcodeGeneratorActivity.class);
                  Bundle extras=new Bundle();
                  extras.putString("TicketNum", Ticket_Num);
                  extras.putString("Email",Email_string);
                  intent.putExtras(extras);
                  startActivity(intent);
              }
          });


          row.addView(t1);
          row.addView(t2);
          row.addView(t3);
          row.addView(t4);
          row.addView(t5);
          row.addView(iv1);
          row.addView(ivbarcode);
          l1.addView(row, i);
      }
  }
    // Dialog interface to ask yes or no to delete ticket from ticket history.
      public void createAndShowAlertDialog(String ticket_Num, String dateexpiry, String convertdateToday) {
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle("Are you Sure you want to delete your Ticket?");
          final String Ticket=ticket_Num;
          final String expiryDate=dateexpiry;
          final String TodayDate=convertdateToday;
          builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  deleteTicket(Ticket, expiryDate, TodayDate);
                  dialog.dismiss();
              }
          });
          builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                  //TODO
                  dialog.dismiss();
              }
          });
          AlertDialog dialog = builder.create();
          dialog.show();
      }

     // TicketDetails.ad(listItems.get(i))


       // listViewticket.setAdapter(adapter);



//Delete the ticket on click event
    private void deleteTicket(String Ticket_Num,String expiryDate, String TodayDate)
    {
        DeleteTicketData deleteTicketData=new DeleteTicketData(this);
        deleteTicketData.execute(Ticket_Num,expiryDate,TodayDate);
    }

    /**
     * Async task to get Ticket details of a particular passenger
     * */
    private class DeleteTicketData extends AsyncTask<String, Void, String> {
        // String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        private final ProgressDialog pDialog=new ProgressDialog(ViewTicket.this);
        Context ctx;

        DeleteTicketData(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(ViewTicket.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Ticket Status..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            String DialogStatus="";
            String Ticket_Num=params[0];
            String expiryDate=params[1];
            String TodayDate=params[2];


            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLTicketDel(TicketDel_url,Ticket_Num,expiryDate,TodayDate);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);


                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Train_Ticket_Result");

                        for (int i = 0; i < categories.length(); i++) {


                            JSONObject catObj = categories.getJSONObject(i);
                            if (catObj.getString("Ticket Status") != null) {
                                if (catObj.getString("Ticket Status").equals("Tickets cannot be deleted!!")) {
                                    DialogStatus = "Tickets cannot be deleted as the Ticket's Expiry Date is after or equals to Current Date.!!";
                                    status = 1;

                                } else {
                                    DialogStatus="Ticket Deleted Successfully..!!!";
                                    status=2;

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
                            if(status==2)
                            {


                                Intent intent = new Intent(ViewTicket.this, ViewTicket.class);
                                startActivity(intent);
                            }

                            dialog.dismiss();
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






    /**
     * Async task to get Ticket details of a particular passenger
     * */
    private class GetTicketData extends AsyncTask<String, Void, String> {
        // String Train_line_Name = spinnerTrainLIne.getSelectedItem().toString();
        private final ProgressDialog pDialog=new ProgressDialog(ViewTicket.this);
        Context ctx;

        GetTicketData(Context ctx)
        {

            this.ctx=ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(ViewTicket.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Ticket Details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            String DialogStatus="";


            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLTicket(Ticket_url,Email_string);

            Log.e("Response: ", "> " + json);

                    if (json != null) {
                try {
                    String Ticket_Num,Ticket_Type,Ticket_Purchase_Date,Ticket_Expiry_Date,Ticket_Fare;
                    JSONObject jsonObj = new JSONObject(json);


                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Train_Ticket_Result");

                        for (int i = 0; i < categories.length(); i++) {

                //29.	http://stackoverflow.com/questions/29469149/android-json-parsing-object-inside-another-object-and-array
                            JSONObject catObj = categories.getJSONObject(i);
                            if (catObj.getString("Ticket Status") != null) {
                                if (catObj.getString("Ticket Status").equals("No Tickets Booked!!")) {
                                    DialogStatus = "No Tickets Booked!!. You can Go to Book Tickets Page to start your journey with METLINK..";
                                    status = 1;
                                    // Reference : Fetching json object insode another object--> http://stackoverflow.com/questions/29469149/android-json-parsing-object-inside-another-object-and-array
                                } else {
                                    DialogStatus = "Click OK to View your Tickets..!!!";
                                    JSONObject cobj = catObj.getJSONObject("Ticket Status");
                                    Ticket_Num = cobj.getString("Ticket_Num");
                                    Ticket_Type = cobj.getString("Ticket_Type");
                                    Ticket_Purchase_Date = cobj.getString("Ticket_Purchase_Date");
                                    Ticket_Expiry_Date = cobj.getString("Ticket_Expiry_Date");
                                    Ticket_Fare = cobj.getString("Ticket_Fare");
                                    PassengerTicketDetails cat = new PassengerTicketDetails(Ticket_Num, Ticket_Type, Ticket_Purchase_Date, Ticket_Expiry_Date, Ticket_Fare);
                                    listItems.add(cat);

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
                            if(status==1)
                            {

                                Intent intent = new Intent(ViewTicket.this, MainActivity.class);
                                Bundle extras=new Bundle();
                                extras.putString("Email",Email_string);
                                intent.putExtras(extras);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                populateTicketData();
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

        Intent intent=new Intent(ViewTicket.this, MainActivity.class);
        Bundle extras=new Bundle();
        extras.putString("Email",Email_string);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }
}



