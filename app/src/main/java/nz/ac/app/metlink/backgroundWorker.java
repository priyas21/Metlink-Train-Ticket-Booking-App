/*
package nz.ac.app.metlink;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

*/
/**
 * Created by 21600481 on 12-10-2016.
 *//*

public class backgroundWorker extends AsyncTask<String,Void,String> {
    Context ctx;
    AlertDialog alertDialog;

    backgroundWorker(Context ctx)
    {

        this.ctx=ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];


        String login_url = "http://tranzmetrometlink.com/login.php";
        String register_url = "http://tranzmetrometlink.com/register.php";
        String Card_url="http://tranzmetrometlink.com/PassengerBookingDetails.php";
         if (type.equals("register")) {
            try {
                String Title = params[1];
                String First_Name = params[2];
                String Last_Name = params[3];
                String Mobile_num = params[4];
                String Email = params[5];
                String User_Pass = params[6];
                String ConfirmPass = params[7];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("Title", "UTF-8") + "=" + URLEncoder.encode(Title, "UTF-8") + "&"
                        +URLEncoder.encode("First_name", "UTF-8") + "=" + URLEncoder.encode(First_Name, "UTF-8") + "&"
                        +URLEncoder.encode("Last_name", "UTF-8") + "=" + URLEncoder.encode(Last_Name, "UTF-8") + "&"
                        +URLEncoder.encode("Mobile_num", "UTF-8") + "=" + URLEncoder.encode(Mobile_num, "UTF-8") + "&"
                        + URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8")+"&"
                        +URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(User_Pass, "UTF-8") + "&"
                        +URLEncoder.encode("ConfirmPass", "UTF-8") + "=" + URLEncoder.encode(ConfirmPass, "UTF-8") + "&";
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return "Registration Success";
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



        return null;
    }


    @Override
    protected void onPreExecute() {

        alertDialog=new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Alert Dialog");

    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null){

            if (result.equals("Registration Success")) {
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            }
            else
            {
                alertDialog.setMessage(result);
                alertDialog.show();
            }
    }else {


            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }






        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

*/
