package nz.ac.app.metlink;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    DatabaseHelper myDb;
    private Session session;
    public static Context ctx;
    TextView txtLogin, txtmyLogout;
    Button smstxtview;
    ImageView ivMyAccount, ivLogout;
    String Email_string = "", loginstatus = "";
    AlertDialog.Builder alertDialog;
    String getImage_url = "http://tranzmetrometlink.com/GetImage.php";
    String status = null;
    Bitmap User_Image;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        txtmyLogout = (TextView) findViewById(R.id.textLogout);
        ivLogout = (ImageView) findViewById(R.id.imageViewLogout);
        ivMyAccount = (ImageView) findViewById(R.id.imageLogin);
        int whitecolor = Color.WHITE;
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);
        //smstxtview=(Button) findViewById(R.id.SMStxt);
        ctx = this;
        session = new Session(ctx);
        txtLogin = (TextView) findViewById(R.id.Login);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginOnClick();
            }
        });
        ivMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginOnClick();
            }
        });


        if (session.loggedIn() == true) {

            // 19.	http://stackoverflow.com/questions/8822553/null-value-in-getintent-getextras-getstring (Moving data through intent)
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Email_string = extras.getString("Email");
                // Toast.makeText(this, Email_string, Toast.LENGTH_SHORT).show();
                txtLogin.setText("My Account");
                status = "Session Logged In";
                GetImage(Email_string);
            } else {
                session.setLoggedin(false);
                txtLogin.setText("Log In");
                ivMyAccount.setImageResource(R.drawable.login);
            }

        } else {
            txtLogin.setText("Log In");
            ivMyAccount.setImageResource(R.drawable.login);
        }
    }
    // Back Button Function onKeyDown: http://stackoverflow.com/questions/4778754/kill-activity-on-back-button

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Do You want to exit the application and logout");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // dialogInterface.cancel();
                    if (session.loggedIn()) {
                        session.setLoggedin(false);
                        finish();
                    } else {
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());  // Kill The application and exit:  http://stackoverflow.com/questions/12611486/how-to-close-or-exit-android-activity
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // dialogInterface.cancel();
                    //17.	http://stackoverflow.com/questions/19286970/using-intents-to-pass-data-between-activities-in-android (value transfer from one intent to another)
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("Email", Email_string);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            final AlertDialog alertDialog1 = builder.create();
            alertDialog1.show();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void GetImage(String Email) {
        String email = Email;
        GetImageDetails getImageDetails = new GetImageDetails(this);
        getImageDetails.execute(email);
    }

    /**
     * Async task to insert Registration Details
     */
    private class GetImageDetails extends AsyncTask<String, Void, String> {
        Context ctx;
        private final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);

        GetImageDetails(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(String... arg0) {
            String DialogStatus = "";
            String email = arg0[0];
            URL url = null;
            String imageURLs, base64image = "";
            Bitmap bitmaps;


            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLGetImage(getImage_url, email);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("User_Image_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = categories.getJSONObject(i);

                            JSONObject cobj = catObj.getJSONObject("Image_Status");
                            base64image = cobj.getString("User_Image");
                            if (base64image.equals("No_Image")) {

                                status = "No_Image";
                            } else {
                                try {
                                    status = "Image_Exists";
                                    base64image = cobj.getString("User_Image");
                                    // Getting image from my sql server: https://www.youtube.com/watch?v=KmYJBhz1gmk
                                    url = new URL(cobj.getString("User_Image"));
                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setDoInput(true);
                                    connection.connect();
                                    InputStream input = connection.getInputStream();
                                    User_Image = BitmapFactory.decodeStream(input);
                                    User_Image = getRoundedShape(User_Image);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        }


                    }

                    return status;

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
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("No_Image") || result.equals("Session Logged In")) {

                ivMyAccount.setImageResource(R.drawable.admin);
                ivMyAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyAccountClicked();
                    }
                });
                ivLogout.setImageResource(R.drawable.logout);

                txtmyLogout.setText("Log Out");
                txtmyLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logout();
                    }
                });
            } else {
                ivMyAccount.setImageBitmap(User_Image);
                ivMyAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyAccountClicked();
                    }
                });
                txtmyLogout.setText("Log Out");
                ivLogout.setImageResource(R.drawable.logout);
                txtmyLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logout();
                    }
                });
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }

    // Convert Bitmap into circle image : 38.	http://stackoverflow.com/questions/18378741/how-to-make-an-imageview-in-circular-shape


    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 70;
        int targetHeight = 70;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }


    public void bookTicketClicked(View view) {

        if (session.loggedIn()) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Email_string = extras.getString("Email");
                // Toast.makeText(this, Email_string, Toast.LENGTH_SHORT).show();
            }

            //send the Email and Mobile to bookTicket Activity

            Intent intent = new Intent(MainActivity.this, BookTicket.class);
            Bundle extras1 = new Bundle();
            extras1.putString("Email", Email_string);
            intent.putExtras(extras1);
            startActivity(intent);


            // startActivity(new Intent(MainActivity.this, BookTicket.class));
        } else {

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

    }

    public void MyAccountClicked() {
        if (session.loggedIn() == true) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Email_string = extras.getString("Email");
                // Toast.makeText(this, Email_string, Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(MainActivity.this, My_Profile.class);
            Bundle extras1 = new Bundle();
            extras1.putString("Email", Email_string);
            intent.putExtras(extras1);
            startActivity(intent);
            finish();
        } else {

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    }


    public void LoginOnClick() {

        if (txtLogin.getText().toString().equals("My Account")) {

            if (session.loggedIn() == true) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Email_string = extras.getString("Email");
                    // Toast.makeText(this, Email_string, Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(MainActivity.this, My_Profile.class);
                Bundle extras1 = new Bundle();
                extras1.putString("Email", Email_string);
                intent.putExtras(extras1);
                startActivity(intent);
            }
        } else {

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }

    public void MyLogoutClicked(View view) {
        logout();
    }

    public void logout() {
        session.setLoggedin(false);

        Toast.makeText(MainActivity.this, "You have been successfully Logged Out", Toast.LENGTH_LONG).show();
        txtLogin.setText("Log In");
        startActivity(new Intent(MainActivity.this, MainActivity.class));

    }

    public void planJourneyClicked(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }
       /* public void adminOnClick(View view) {

            Intent intent = new Intent(MainActivity.this, AdminHome.class);
            startActivity(intent);
        }*/

    public void FareEnquiryCLicked(View view) {
        Intent intent = new Intent(MainActivity.this, Fare_Enquiry.class);
        if (session.loggedIn() == true) {
            Bundle extras=new Bundle();
            extras.putString("Email",Email_string);
            intent.putExtras(extras);
            startActivity(intent);
            finish();
        }
        else
        {
            startActivity(intent);
        }
    }

    public void Camera(View view) {
        Intent intent = new Intent(MainActivity.this, Camera.class);
        startActivity(intent);

    }

    public void ViewTicket(View view) {

        if (session.loggedIn()) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Email_string = extras.getString("Email");
                // Toast.makeText(this, Email_string, Toast.LENGTH_SHORT).show();
            }
            //send the Email and Mobile to bookTicket Activity
            Intent intent = new Intent(MainActivity.this, ViewTicket.class);
            Bundle extras1 = new Bundle();
            extras1.putString("Email", Email_string);
            intent.putExtras(extras1);
            startActivity(intent);
        } else {
            Toast.makeText(this, "You have log in to View Your Tickets..!!!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }

    public void thumbsupClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Like Us On Facebook!!!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent=new Intent("android.intent.action.VIEW",Uri.parse("https://www.facebook.com/TranzMetroMetlink/"));
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

