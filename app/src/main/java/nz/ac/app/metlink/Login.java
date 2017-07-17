package nz.ac.app.metlink;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper myDb;
    Button btnLogin;
    EditText editEmail, editPass;
    ImageView imgViewHome;
    TextView TextAttempt;
    int attempt_counter = 3;
    int flag = 0, check=0;
    Bundle extras;
    private Session session;
    public static Context ctx;
    String Email_ID ="";
    String Password="";
    int status=0;
    AlertDialog.Builder alertDialog;
    private ArrayList<LoginDetails> listItems;
    String login_url = "http://tranzmetrometlink.com/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int whitecolor = Color.WHITE;
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);
        listItems = new ArrayList<LoginDetails>();
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPass = (EditText) findViewById(R.id.editTextPass);
        btnLogin = (Button) findViewById(R.id.buttonSignIn);
        imgViewHome = (ImageView) findViewById(R.id.imgviewhome);
        TextAttempt = (TextView) findViewById(R.id.attempts);
        ctx = this;
        session = new Session(ctx);
        myDb = new DatabaseHelper(this);
        btnLogin.setOnClickListener(this);
        if (session.loggedIn()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            Bundle extras = new Bundle();
            extras.putString("Email", editEmail.getText().toString());
            intent.putExtras(extras);
            startActivity(intent);
            finish();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(session.loggedIn())
            {
                Intent intent = new Intent(Login.this, MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("Email", editEmail.getText().toString());
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(Login.this, MainActivity.class);               ;
                startActivity(intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }




    public void homeClicked(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void RegisterLinkClicked(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        //Store Email and password in a variable

        Email_ID = editEmail.getText().toString();
        Password = editPass.getText().toString();
        String type = "Login";
        // Toast.makeText(Login.this, Email_ID, Toast.LENGTH_LONG).show();
        // Toast.makeText(Login.this, Password, Toast.LENGTH_LONG).show();

        //Email Validate

        if (editEmail.getText().toString().length() == 0) {
            editEmail.setError("Email cannot be empty");
            //editEmail.requestFocus();
            flag = 1;
        }
        if (!validateEmail(editEmail.getText().toString())) {
            editEmail.setError("Invalid Email");
            editEmail.requestFocus();
            flag = 1;
        }
        //Password Validate

        if (editPass.getText().toString().length() == 0) {
            editPass.setError("Password cannot be empty");
            //editPass.requestFocus();
            flag = 1;
        }
        if (flag == 0) {
            editEmail.setError(null);
            editPass.setError(null);
            //14.	http://stackoverflow.com/questions/903343/cursor-get-the-field-value-android (Cursor )



            //First check the credentials from mobile's database; If not exists then check from server( This is done to speed up the log in process)
            Cursor res=null;
            res = myDb.CheckLoginDetails(editEmail.getText().toString(), editPass.getText().toString());

            if (res.getCount() > 0 && flag == 0) {
                session.setLoggedin(true);
                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                if (res != null) {
                    res.moveToFirst();
                    Email_ID = res.getString(res.getColumnIndex("Emil_ID"));
                }

              //     Toast.makeText(Login.this, Email_ID, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Login.this, MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("Email", Email_ID);
                intent.putExtras(extras);
                startActivity(intent);
                finish();



            } else {
                CheckLoginDetails();
            }

        }
    }

    private void CheckLoginDetails()
    {

        GetLoginDetails getlogindetails=new GetLoginDetails(this);
        getlogindetails.execute();
    }
 
    /**
     * Async task to get Login Details
     * */
    private class GetLoginDetails extends AsyncTask<Void, Void, String> {
        Context ctx;
        private final ProgressDialog pDialog=new ProgressDialog(Login.this);

        GetLoginDetails(Context ctx)
        {

            this.ctx=ctx;
        }



        @Override
        protected String doInBackground(Void... arg0) {
            String Name="",DialogStatus="",Email_string="",Login_Status;
            int f1=0;
            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLLogin(login_url,Email_ID,Password);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Login_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = categories.getJSONObject(i);


                            if (catObj.getString("Login Status") != null) {
                                if (catObj.getString("Login Status").equals("Login Failed....No such user exists!!!")) {
                                    DialogStatus = "Login Failed.No Such User exists... Email/Password Incorrect....Try Again!!!!";
                                    status=1;

                                } else {
                                        JSONObject cobj=catObj.getJSONObject("Login Status");
                                        Name = cobj.getString("First_Name");
                                        Email_string = cobj.getString("Email");
                                        DialogStatus = "Login Sucessful.. Welcome " + Name;
                                        Email_ID = Email_string;
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
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog=new AlertDialog.Builder(Login.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Login Details..");
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

                            dialog.dismiss();
                            if(status==2)
                            {
                               session.setLoggedin(true);

                                Cursor res=null;
                                res = myDb.CheckLoginDetails(editEmail.getText().toString(), editPass.getText().toString());

                                if (res.getCount() == 0 ) {

                                    boolean isInserted = myDb.insertLoginDetails(editEmail.getText().toString(), editPass.getText().toString());
                                    if (isInserted == true) {
                                        Log.e("Insertion Status:-->", "Insertion Done Successfully in Mobile's Database");
                                    } else {
                                        Log.e("Insertion Status:-->", "Insertion Unsuccessful in Mobile's Database");
                                    }
                                }

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("Email", Email_ID);
                                intent.putExtras(extras);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {

                                Intent intent=new Intent(Login.this,Login.class);
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


      /*  Cursor res = myDb.LoginData(editEmail.getText().toString(), editPass.getText().toString());
        if (res.getCount() == 0 && flag == 0) {
            Toast.makeText(Login.this, "No such User Exist", Toast.LENGTH_LONG).show();
            attempt_counter--;
            if (attempt_counter == 0) {
                TextAttempt.setText("Login attempts exceeds 3");
                //Toast.makeText(Login.this,"Login attempts exceeds 3",Toast.LENGTH_LONG).show();
                btnLogin.setEnabled(false);
            }
        } if(res.getCount()>0 && flag==0) {
            session.setLoggedin(true);
           Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
            if(res!=null) {
                res.moveToFirst();
                Email = res.getString(res.getColumnIndex("Email"));
                Mobile = res.getString(res.getColumnIndex("Mobile_num"));
            }

            Toast.makeText(Login.this, Email,Toast.LENGTH_LONG).show();
            Toast.makeText(Login.this, Mobile,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Login.this, MainActivity.class);
            Bundle extras=new Bundle();
            extras.putString("Email",Email);
            extras.putString("Mobile_Num",Mobile);
            intent.putExtras(extras);
            startActivity(intent);
        }
*/


    //Validate Email
    protected boolean validateEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher((CharSequence) email);
        return matcher.matches();

    }
    public void thumbsupClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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


