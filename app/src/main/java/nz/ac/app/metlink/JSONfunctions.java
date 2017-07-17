package nz.ac.app.metlink;

import android.util.Log;

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

/**
 * Created by 21600481 on 14-10-2016.
 */
public class JSONfunctions {

    public JSONfunctions()
    {

    }
    public static String getJSONformURL(String url) {
        String result = "";
        JSONObject jArray = null;
        String TrainLine_url = url;

        try {

            URL link = new URL(TrainLine_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) link.openConnection();
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
