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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class My_Profile extends AppCompatActivity {
    TextView txtlogOut, txtName, txtEmail, txtMobile;

    private Session session;
    public static Context ctx;
    String Email_string = "";
    AlertDialog.Builder alertDialog;
    int status = 0;
    String MyProfile_url = "http://tranzmetrometlink.com/MyProfile.php";
    String UpdateoldMobile_url = "http://tranzmetrometlink.com/updateMobileNum.php";
    String UpdateoldPass_url = "http://tranzmetrometlink.com/updatePassword.php";

    String FirstName, LastName, Email, Mobile, Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);
        txtlogOut = (TextView) findViewById(R.id.Logout);
        txtName = (TextView) findViewById(R.id.textViewName);
        txtEmail = (TextView) findViewById(R.id.textViewEmail);
        txtMobile = (TextView) findViewById(R.id.textViewMobile);


        int whitecolor = Color.WHITE;
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);

        ctx = this;
        session = new Session(ctx);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Email_string = extras.getString("Email");
           // Toast.makeText(this, Email_string, Toast.LENGTH_SHORT).show();
        }
        loadMyProfile(Email_string);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(session.loggedIn())
            {
                Intent intent = new Intent(My_Profile.this, MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("Email", Email_string);
                intent.putExtras(extras);
                startActivity(intent);
                finish();

            }
            else
            {
                session.setLoggedin(false);
                Intent intent = new Intent(My_Profile.this, MainActivity.class);
                startActivity(intent);
                finish();

            }

        }
        return super.onKeyDown(keyCode, event);
    }




    //Use JSON object and php script file address to access the data from database
    // Function to load the Train Line Spinner from SQLite database

    private void loadMyProfile(String Email) {

        String email = Email;
        GetMyProfileDetails getMyProfileDetails = new GetMyProfileDetails(this);
        getMyProfileDetails.execute(email);
    }

    /**
     * Adding Profile Details
     */
    private void populateMyProfile() {
        txtName.setText(Name);
        txtEmail.setText(Email);
        txtMobile.setText(Mobile);

    }


    /**
     * Async task to get all Profile Details
     */
    private class GetMyProfileDetails extends AsyncTask<String, Void, String> {
        Context ctx;
        private final ProgressDialog pDialog = new ProgressDialog(My_Profile.this);

        GetMyProfileDetails(Context ctx) {

            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(My_Profile.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Your Profile Details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String DialogStatus = "";
            String Email_ID = params[0];
            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLMyProfile(MyProfile_url, Email_ID);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("My_Profile_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            if (catObj.getString("Profile_Status") != null) {
                                if (catObj.getString("Profile_Status").equals("Profile Not Found!!!")) {
                                    DialogStatus = "No Profile Details Found..!!!!";
                                    status = 1;

                                } else {
                                    JSONObject cobj = catObj.getJSONObject("Profile_Status");
                                    FirstName = cobj.getString("First_Name");
                                    LastName = cobj.getString("Last_Name");
                                    Mobile = cobj.getString("Mobile_num");
                                    Email_ID = cobj.getString("Email");
                                    Email = Email_ID;
                                    Name = FirstName + " " + LastName;

                                    DialogStatus = "Click ok to view Your Profile !!!! ";
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
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            if (status == 2) {

                                populateMyProfile();
                            } else {

                                Intent intent = new Intent(My_Profile.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        }
                    });
            AlertDialog alert = alertDialog.create();
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

    public void LogoutOnClick(View view) {
        if (txtlogOut.getText().toString().equals("Log Out")) {
            session.setLoggedin(false);
            finish();
            Toast.makeText(My_Profile.this, "You have been successfully Logged Out", Toast.LENGTH_LONG).show();
            startActivity(new Intent(My_Profile.this, MainActivity.class));
            finish();
        }
    }
// Password Update Button clicked..................................................................................
    //.................................................................................................................

    public void buttonUpdatePassClicked(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(My_Profile.this);
        alertDialog.setTitle("Update Password");

        final EditText newPass = new EditText(My_Profile.this);
        final EditText confirmPass = new EditText(My_Profile.this);
        final TextView txt = new TextView(My_Profile.this);
// 41.	http://stackoverflow.com/questions/30795665/how-to-make-password-change-prompt-confirmation-in-settingsactivity-for-android (update password)

        newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());


        newPass.setHint("New Password");
        confirmPass.setHint("Confirm New Password");
        txt.setText("Password Must be 6 to 8 characters long");

        LinearLayout l1 = new LinearLayout(My_Profile.this);
        l1.setOrientation(LinearLayout.VERTICAL);


        l1.addView(newPass);
        l1.addView(txt);
        l1.addView(confirmPass);
        alertDialog.setView(l1);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dialogInterface.cancel();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
        alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean wantToCloseDialog = false;
                int flag = 0;
                if (newPass.getText().toString().length() == 0 || confirmPass.getText().toString().length() == 0) {

                    newPass.setError("Cannot be empty");
                    confirmPass.setError("Cannot be empty");
                    flag = 1;
                }

                if (!validatePassword(newPass.getText().toString())) {
                    newPass.setError("Password Must be 6 to 8 charaters long");
                    flag = 1;
                }
                if (!validatePassword(confirmPass.getText().toString())) {
                    confirmPass.setError("Password Must be 6 to 8 charaters long");
                    flag = 1;
                }
                if (!(newPass.getText().toString().equals(confirmPass.getText().toString()))) {
                    newPass.setError("New Password and confirm Password does not match ");
                    confirmPass.setError("New Password and confirm Password does not match");
                    flag = 1;
                }
                if (flag == 0) {
                    newPass.setError(null);

                    confirmPass.setError(null);
                    wantToCloseDialog = true;
                    updatePassword(Email, newPass.getText().toString());

                }

                //Do stuff, possibly set wantToCloseDialog to true then...
                if (wantToCloseDialog)
                    alertDialog1.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.

            }
        });

    }

    // Validate Password
    protected boolean validatePassword(String pass) {
        if (pass != null && pass.length() >= 6 && pass.length() <= 8) {
            return true;
        } else
            return false;

    }

    // Update old Password...........................................................................................
    //..................................................................................
    public void updatePassword(String Email, String newPassword) {

        String email = Email;
        String newpass = newPassword;
        UpdateoldPassword updateoldPass = new UpdateoldPassword(this);
        updateoldPass.execute(email, newpass);
    }

    /**
     * Async task to update old Password
     */
    private class UpdateoldPassword extends AsyncTask<String, Void, String> {
        Context ctx;
        private final ProgressDialog pDialog = new ProgressDialog(My_Profile.this);

        UpdateoldPassword(Context ctx) {

            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(My_Profile.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Your Profile Details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String DialogStatus = "";
            String Email_ID = params[0];
            String newPass = params[1];
            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLupdatePass(UpdateoldPass_url, Email_ID, newPass);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("My_Profile_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            if (catObj.getString("Profile_Status") != null) {
                                if (catObj.getString("Profile_Status").equals("Password Updated Successfully")) {
                                    DialogStatus = "Password Updated Successfully..!!!!";
                                    status = 1;

                                } else {
                                    DialogStatus = "Error Updating Password..!!!!";
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
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            if (status == 1) {
                                Intent intent = new Intent(My_Profile.this, My_Profile.class);
                                Bundle extras1 = new Bundle();
                                extras1.putString("Email", Email_string);
                                intent.putExtras(extras1);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
            AlertDialog alert = alertDialog.create();
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

//..............................Mobile Number Update Button Clicked............................................
    //........................................................................................................

    public void buttonUpdateClicked(View view) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(My_Profile.this);
        alertDialog.setTitle("Update Mobile Number");
        final EditText oldNum = new EditText(My_Profile.this);
        final EditText newNum = new EditText(My_Profile.this);
        final EditText confirmNum = new EditText(My_Profile.this);

        oldNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        newNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        confirmNum.setInputType(InputType.TYPE_CLASS_NUMBER);


        oldNum.setHint("Old Mobile Number");
        newNum.setHint("New Mobile Number");
        confirmNum.setHint("Confirm New Mobile Number");


        LinearLayout l1 = new LinearLayout(My_Profile.this);
        l1.setOrientation(LinearLayout.VERTICAL);

        l1.addView(oldNum);
        l1.addView(newNum);
        l1.addView(confirmNum);
        alertDialog.setView(l1);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //dialogInterface.cancel();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing here because we override this button later to change the close behaviour.
                //However, we still need this because on older versions of Android unless we
                //pass a handler the button doesn't get instantiated
                //dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
        alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean wantToCloseDialog = false;
                int flag = 0;
                if (oldNum.getText().toString().length() == 0 || newNum.getText().toString().length() == 0 || confirmNum.getText().toString().length() == 0) {
                    oldNum.setError("Cannot be empty");
                    newNum.setError("Cannot be empty");
                    confirmNum.setError("Cannot be empty");
                    flag = 1;
                }
                if (!(oldNum.getText().toString().equals(txtMobile.getText().toString()))) {
                    oldNum.setError("Old Mobile Number entered is Wrong");
                    flag = 1;
                }
                if (!validateMobile(oldNum.getText().toString())) {
                    oldNum.setError("Invalid Mobile number");
                    flag = 1;
                }
                if (!validateMobile(newNum.getText().toString())) {
                    newNum.setError("Invalid Mobile number");
                    flag = 1;
                }
                if (!validateMobile(confirmNum.getText().toString())) {
                    confirmNum.setError("Invalid Mobile number");
                    flag = 1;
                }
                if (!(newNum.getText().toString().equals(confirmNum.getText().toString()))) {
                    newNum.setError("New Mobile num and confirm mobile number does not match ");
                    confirmNum.setError("New Mobile num and confirm mobile number does not match");
                    flag = 1;
                }
                if (flag == 0) {
                    newNum.setError(null);
                    oldNum.setError(null);
                    confirmNum.setError(null);
                    wantToCloseDialog = true;
                    updateMobileNum(Email, newNum.getText().toString());

                }

                //Do stuff, possibly set wantToCloseDialog to true then...
                if (wantToCloseDialog)
                    alertDialog1.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.

            }
        });

    }

    // Validate phone number
    protected boolean validateMobile(String mobile) {
        String regexStr = "^[+]?[0-9]{10,13}$";
        if (mobile.length() < 10 || mobile.length() > 13 || mobile.matches(regexStr) == false) {
            return false;
        } else
            return true;
    }

    // Update old mobile number...........................................................................................
    //..................................................................................
    public void updateMobileNum(String Email, String newMobilenum) {

        String email = Email;
        String newmobile = newMobilenum;
        UpdateoldMobile updateoldMobile = new UpdateoldMobile(this);
        updateoldMobile.execute(email, newmobile);
    }

    /**
     * Async task to update old Mobile Number
     */
    private class UpdateoldMobile extends AsyncTask<String, Void, String> {
        Context ctx;
        private final ProgressDialog pDialog = new ProgressDialog(My_Profile.this);

        UpdateoldMobile(Context ctx) {

            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(My_Profile.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Fetching Your Profile Details..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String DialogStatus = "";
            String Email_ID = params[0];
            String newMobile = params[1];
            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLupdateMobile(UpdateoldMobile_url, Email_ID, newMobile);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("My_Profile_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            if (catObj.getString("Profile_Status") != null) {
                                if (catObj.getString("Profile_Status").equals("Mobile Number Updated Successfully")) {
                                    DialogStatus = "Mobile Number Updated Successfully..!!!!";
                                    status = 1;

                                } else {
                                    DialogStatus = "Error Updating Mobile Number..!!!!";
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
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            if (status == 1) {
                                Intent intent = new Intent(My_Profile.this, My_Profile.class);
                                Bundle extras1 = new Bundle();
                                extras1.putString("Email", Email_string);
                                intent.putExtras(extras1);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
            AlertDialog alert = alertDialog.create();
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

    public void homeClicked(View view) {
        Intent intent = new Intent(My_Profile.this, MainActivity.class);
        Bundle extras1 = new Bundle();
        extras1.putString("Email", Email_string);
        intent.putExtras(extras1);
        startActivity(intent);
        finish();
    }
    public void thumbsupClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(My_Profile.this);
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









