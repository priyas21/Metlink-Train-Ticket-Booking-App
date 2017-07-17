package nz.ac.app.metlink;

import android.graphics.Bitmap;

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

/**
 * Created by 21600481 on 15-10-2016.
 */

/**References
 * 1. https://www.youtube.com/watch?v=UqY4DY2rHOs (Connecting database with php server (cloud)
2. https://www.youtube.com/watch?v=eldh8l8yPew (Connecting the android with php scripts via HTTP connection)
 3.https://www.youtube.com/watch?v=age2l7Rrwtc (Inserting data)


 */

public class JSONfunctionsStation {
    public JSONfunctionsStation() {

    }


    public static String getJSONformURL(String url, String TrainLineName) {
        String result = "";
        JSONObject jArray = null;
        String TrainLineStation_url = url;

        try {

            URL link = new URL(TrainLineStation_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("TrainLineName", "UTF-8") + "=" + URLEncoder.encode(TrainLineName, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }

    public static String getJSONformURLFare(String url, String TrainLineName, String Passen_Type_Name, String Source_Station, String Dest_Station) {
        String result = "";
        JSONObject jArray = null;
        String TrainFareStation_url = url;

        try {

            URL link = new URL(TrainFareStation_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("TrainLineName", "UTF-8") + "=" + URLEncoder.encode(TrainLineName, "UTF-8") + "&"
                    + URLEncoder.encode("Passen_Type_Name", "UTF-8") + "=" + URLEncoder.encode(Passen_Type_Name, "UTF-8") + "&"
                    + URLEncoder.encode("Source_Station", "UTF-8") + "=" + URLEncoder.encode(Source_Station, "UTF-8") + "&"
                    + URLEncoder.encode("Dest_Station", "UTF-8") + "=" + URLEncoder.encode(Dest_Station, "UTF-8") + "&";
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }

    public static String getJSONformURLTicket(String Ticket_url, String Email) {
        String result = "";
        JSONObject jArray = null;
        String ViewTicket_url = Ticket_url;

        try {

            URL link = new URL(ViewTicket_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8") + "&";
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }

    public static String getJSONformURLTicketDel(String TicketDel_url, String Ticket_Num, String expiryDate, String TodayDate) {
        String result = "";
        JSONObject jArray = null;
        String url = TicketDel_url;
        String expirydate = expiryDate;
        String todaydate = TodayDate;

        try {

            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Ticket_Num", "UTF-8") + "=" + URLEncoder.encode(Ticket_Num, "UTF-8") + "&"
                    + URLEncoder.encode("expirydate", "UTF-8") + "=" + URLEncoder.encode(expirydate, "UTF-8") + "&"
                    + URLEncoder.encode("todaydate", "UTF-8") + "=" + URLEncoder.encode(todaydate, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }

    public static String getJSONformURLLogin(String Login_url, String Email, String Password) {
        String result = "";
        JSONObject jArray = null;
        String url = Login_url;

        try {

            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8") + "&"
                    + URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(Password, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }

    public static String getJSONformURLInsertCardDetails(String Card_url, String Ticket_Num, String Email_string, String MobileNum, String PaymentStatus, String TicketType, String PurchaseTicketDate, String ExpiryTicketDate, String Fare) {
        String result = "";
        JSONObject jArray = null;
        String url = Card_url;

        try {

            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Ticket_ID", "UTF-8") + "=" + URLEncoder.encode(Ticket_Num, "UTF-8") + "&"
                    + URLEncoder.encode("Email_string", "UTF-8") + "=" + URLEncoder.encode(Email_string, "UTF-8") + "&"
                    + URLEncoder.encode("MobileNum", "UTF-8") + "=" + URLEncoder.encode(MobileNum, "UTF-8") + "&"
                    + URLEncoder.encode("PaymentStatus", "UTF-8") + "=" + URLEncoder.encode(PaymentStatus, "UTF-8") + "&"
                    + URLEncoder.encode("TicketType", "UTF-8") + "=" + URLEncoder.encode(TicketType, "UTF-8") + "&"
                    + URLEncoder.encode("PurchaseTicketDate", "UTF-8") + "=" + URLEncoder.encode(PurchaseTicketDate, "UTF-8") + "&"
                    + URLEncoder.encode("ExpiryTicketDate", "UTF-8") + "=" + URLEncoder.encode(ExpiryTicketDate, "UTF-8") + "&"
                    + URLEncoder.encode("TicketFare", "UTF-8") + "=" + URLEncoder.encode(Fare, "UTF-8") + "&";
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;
    }

    public static String getJSONformURLFareEnquiry(String Ticket_url, String Train_line_Name, String Source_Station, String Dest_Station) {
        String result = "";
        JSONObject jArray = null;
        String ViewTicket_url = Ticket_url;

        try {

            URL link = new URL(ViewTicket_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("TrainLineName", "UTF-8") + "=" + URLEncoder.encode(Train_line_Name, "UTF-8") + "&"
                    + URLEncoder.encode("Source_Station", "UTF-8") + "=" + URLEncoder.encode(Source_Station, "UTF-8") + "&"
                    + URLEncoder.encode("Dest_Station", "UTF-8") + "=" + URLEncoder.encode(Dest_Station, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }

    public static String getJSONformURLRegistration(String register_url, String Title, String First_Name, String Last_Name, String Mobile_num, String Email, String User_Pass, String ConfirmPass, String User_Image) {
        String result = "";
        JSONObject jArray = null;
        String url = register_url;
        String title = Title;
        String first_Name = First_Name;
        String last_Name = Last_Name;
        String mobile_num = Mobile_num;
        String email = Email;
        String user_Pass = User_Pass;
        String confirmPass = ConfirmPass;
        String user_image=User_Image;


        try {

            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8") + "&"
                    + URLEncoder.encode("First_name", "UTF-8") + "=" + URLEncoder.encode(first_Name, "UTF-8") + "&"
                    + URLEncoder.encode("Last_name", "UTF-8") + "=" + URLEncoder.encode(last_Name, "UTF-8") + "&"
                    + URLEncoder.encode("Mobile_num", "UTF-8") + "=" + URLEncoder.encode(mobile_num, "UTF-8") + "&"
                    + URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                    + URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(user_Pass, "UTF-8") + "&"
                    + URLEncoder.encode("ConfirmPass", "UTF-8") + "=" + URLEncoder.encode(confirmPass, "UTF-8")+"&"
                    +URLEncoder.encode("User_Image", "UTF-8") + "=" + URLEncoder.encode(user_image, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }

    public static String getJSONformURLMyProfile(String MyProfile_url, String Email) {
        String result = "";
        JSONObject jArray = null;
        String url = MyProfile_url;
        String Email_String = Email;

        try {

            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email_String, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }
    public static String getJSONformURLGetImage(String getImage_url, String Email) {
        String result = "";
        JSONObject jArray = null;
        String url = getImage_url;
        String Email_String = Email;

        try {

            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email_String, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString().trim();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }
    public static String getJSONformURLupdateMobile(String updatemobile_url, String Email ,String newMobile) {
        String result = "";
        JSONObject jArray = null;
        String url = updatemobile_url;
        String Email_String = Email;
        String New_Mobile=newMobile;

        try {

            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email_String, "UTF-8")+"&"
                    +URLEncoder.encode("newMobile", "UTF-8") + "=" + URLEncoder.encode(New_Mobile, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }
    public static String getJSONformURLupdatePass(String updatemobile_url, String Email ,String newPass) {
        String result = "";
        JSONObject jArray = null;
        String url = updatemobile_url;
        String Email_String = Email;
        String New_Pass=newPass;

        try {

            URL link = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email_String, "UTF-8")+"&"
                    +URLEncoder.encode("newPass", "UTF-8") + "=" + URLEncoder.encode(New_Pass, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String JSON_STRING = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            result = stringBuilder.toString();
            // return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //parse json data

        return result;

    }
}
