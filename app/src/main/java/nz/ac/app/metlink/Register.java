package nz.ac.app.metlink;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity  {
    private static final int RC_PERMISSIONS = 1;
    private static final int permsRequestCode=11;

    DatabaseHelper myDb;
    private final String TAG = this.getClass().getName();
    EditText editFirstName, editLastName, editMobile, editEmail, editPass, editConfirmPass;
    Spinner spinnerTitle;
    Button btnRegister, btnupload;
    ImageView imageViewHome, ivcamera, ivgallery, ivimage;
    int flag = 0;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final static int CAMERA_REQUEST = 21200;
    final static int GALLERY_REQUEST=12546;
    public static final int CAMERA_REQUEST_CODE = 10;
    public static final int GALLERY_REQUEST_CODE = 11;
    int status = 0;
    AlertDialog.Builder alertDialog;
    String register_url = "http://tranzmetrometlink.com/register.php";
    String Title, First_Name, Last_Name, Mobile_num, Email, User_Pass, ConfirmPass;

    String selectedPhoto, ImageName, User_Image="No_Image";
    Bitmap bitmapImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDb = new DatabaseHelper(this);
        int whitecolor = Color.WHITE;
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);
        editFirstName = (EditText) findViewById(R.id.editTextFirst);
        editLastName = (EditText) findViewById(R.id.editTextLast);
        editMobile = (EditText) findViewById(R.id.editTextMobile);
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPass = (EditText) findViewById(R.id.editTextPass);
        editConfirmPass = (EditText) findViewById(R.id.editTextConfirmPass);
        spinnerTitle = (Spinner) findViewById(R.id.spinnerTitle);
        imageViewHome = (ImageView) findViewById(R.id.homeimg);

        ivcamera = (ImageView) findViewById(R.id.imageViewcamera);
        ivgallery = (ImageView) findViewById(R.id.imageViewGallery);


        btnupload = (Button) findViewById(R.id.buttonupload);
        btnRegister = (Button) findViewById(R.id.buttonMobileChg);
        ivimage = (ImageView) findViewById(R.id.ivimage);
        cameraPhoto = new CameraPhoto(this);
        galleryPhoto=new GalleryPhoto(this);

/** 32.	https://www.youtube.com/watch?v=4LCnoVqQ6N4 (Camera)
 33.	http://www.androidhive.info/2013/09/android-working-with-camera-api/ (Camera)*
 34.	https://www.simplifiedcoding.net/android-upload-image-using-php-mysql-android-studio/
 */
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                    byte[] b = baos.toByteArray();
                    //Bitmap bitmap= ImageLoader.init().from(selectedPhoto).requestSize(512,512).getBitmap();
                    User_Image= Base64.encodeToString(b, Base64.DEFAULT);
                    Log.d(TAG,User_Image);
            }
        });
    }


    //Camera Permission
    public void takePhoto(View view) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            callCameraApp();
        } else {
            String[] permissionRequested = {Manifest.permission.CAMERA};
            requestPermissions(permissionRequested, CAMERA_REQUEST_CODE);
        }
    }
    //Gallery Permission
    public void selectPhotoFromGallery(View view) {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) {
            callGalleryApp();
        }
        else {
            String[] permissionRequested = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissionRequested, GALLERY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCameraApp();
            } else {
                Toast.makeText(this, "External write permission has not been granted", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryApp();
            } else {
                Toast.makeText(this, "External read permission has not been granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  void callCameraApp() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Get picture Name
        ImageName = getPictureName();
        File imageFile = new File(pictureDirectory, ImageName);
       // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        //Fetch the path of image captured
        selectedPhoto = imageFile.getAbsolutePath();
        startActivityForResult(intent, CAMERA_REQUEST);
        btnupload.setBackgroundColor(Color.MAGENTA);
        btnupload.setTextColor(Color.WHITE);
    }

    public void callGalleryApp(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Get picture name
        ImageName = getPictureName();
        //get picture location
        File imageFile = new File(pictureDirectory, ImageName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        selectedPhoto = imageFile.getAbsolutePath();
        //get Uri representation
        Uri data = Uri.parse(selectedPhoto);
        //get all image types
        intent.setDataAndType(data, "image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
        btnupload.setBackgroundColor(Color.MAGENTA);
        btnupload.setTextColor(Color.WHITE);
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "Profile Pic" + timestamp +".jpg";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {

                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                ivimage.setImageBitmap(bitmap);
                bitmapImage = bitmap;

                String path =selectedPhoto;
                Toast.makeText(this, path, Toast.LENGTH_LONG).show();
                Log.d(TAG, path);
            } else if (requestCode == GALLERY_REQUEST) {
                //The address of the image on sdcard
                Uri imageUri = data.getData();
                //declare a stream to read the image
                InputStream inputStream;
                //We are getting input stream based on uri of the image
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Toast.makeText(this,selectedPhoto, Toast.LENGTH_LONG ).show();
                    ivimage.setImageBitmap(bitmap);
                    bitmapImage=bitmap;
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open Image", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, selectedPhoto);
            }
        }

    }




    public void buttonRegisterClicked(View view) {
        //Toast.makeText(Register.this,User_Image,Toast.LENGTH_SHORT).show();
        Title = spinnerTitle.getSelectedItem().toString();
        First_Name = editFirstName.getText().toString();
        Last_Name = editLastName.getText().toString();
        Mobile_num = editMobile.getText().toString();
        Email = editEmail.getText().toString();
        User_Pass = editPass.getText().toString();
        ConfirmPass = editConfirmPass.getText().toString();
        flag = 0;

        if (editFirstName.getText().toString().length() == 0) {
            editFirstName.setError("First Name cannot be Empty");
            editFirstName.requestFocus();
            flag = 1;
        }
        if (editLastName.getText().toString().length() == 0) {
            editLastName.setError("Last Name cannot be Empty");
            //editLastName.requestFocus();
            flag = 1;
        }
        if (!validateMobile(editMobile.getText().toString())) {
            editMobile.setError("Invalid Mobile number");
            // editMobile.requestFocus();
            flag = 1;
        }
        if (editMobile.getText().toString().length() == 0) {
            editMobile.setError("Mbbile Number cannot be empty");
            //editMobile.requestFocus();
            flag = 1;
        }

        if (!validateEmail(editEmail.getText().toString())) {
            editEmail.setError("Invalid Email");
            //editEmail.requestFocus();
            flag = 1;
        }
        if (editEmail.getText().toString().length() == 0) {
            editEmail.setError("Email cannot be empty");
            //editEmail.requestFocus();
            flag = 1;
        }
        if (!validatePassword(editPass.getText().toString())) {
            editPass.setError("Password must be atleast 6 to 8 characters or digits");
            //editPass.requestFocus();
            flag = 1;
        }
        if (editPass.getText().toString().length() == 0) {
            editPass.setError("Password cannot be empty");
            //editPass.requestFocus();
            flag = 1;
        }
        if (!editPass.getText().toString().equals(editConfirmPass.getText().toString())) {
            editConfirmPass.setError("Password Does Not Match");
            //editConfirmPass.requestFocus();
            flag = 1;
        }
        if (editConfirmPass.getText().toString().length() == 0) {
            editConfirmPass.setError("Confirm Password cannot be empty");
            //editConfirmPass.requestFocus();
            flag = 1;
        }
        if (flag == 0) {
            editFirstName.setError(null);
            editLastName.setError(null);
            editMobile.setError(null);
            editEmail.setError(null);
            editPass.setError(null);
            editConfirmPass.setError(null);
            CheckRegistrationDetails();
            //  backgroundWorker backgroundWorker=new backgroundWorker(this);
            // backgroundWorker.execute(type,Title,First_Name,Last_Name,Mobile_num,Email,User_Pass,ConfirmPass);

          /*  boolean isInserted = myDb.insertData(spinnerTitle.getSelectedItem().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), editMobile.getText().toString(), editEmail.getText().toString(),
                    editPass.getText().toString(), editConfirmPass.getText().toString());
            if (isInserted = true)
                Toast.makeText(Register.this, "Data Inserted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(Register.this, "Data not Inserted", Toast.LENGTH_LONG).show();
          */
            clearInput();

        }
    }

    //Validate Email
    protected boolean validateEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher((CharSequence) email);
        return matcher.matches();

    }

    // Validate Password
    protected boolean validatePassword(String pass) {
        if (pass != null && pass.length() >= 6 && pass.length() <= 8) {
            return true;
        } else
            return false;

    }

    // Validate phone number
    protected boolean validateMobile(String mobile) {
        String regexStr = "^[+]?[0-9]{10,13}$";
        if (mobile.length() < 10 || mobile.length() > 13 || mobile.matches(regexStr) == false) {
            return false;
        } else
            return true;
    }

    public void homeClicked(View view) {

        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void clearInput() {
        editFirstName.setText("");
        editLastName.setText("");
        editMobile.setText("");
        editEmail.setText("");
        editPass.setText("");
        editConfirmPass.setText("");

    }


    private void CheckRegistrationDetails() {

        GetRegDetails getRegDetails = new GetRegDetails(this);
        getRegDetails.execute();
    }

    /**
     * Async task to insert Registration Details
     */
    private class GetRegDetails extends AsyncTask<Void, Void, String> {
        Context ctx;
        private final ProgressDialog pDialog = new ProgressDialog(Register.this);

        GetRegDetails(Context ctx) {

            this.ctx = ctx;
        }


        @Override
        protected String doInBackground(Void... arg0) {
            String DialogStatus = "";

            JSONfunctionsStation jsonParser = new JSONfunctionsStation();
            String json = jsonParser.getJSONformURLRegistration(register_url, Title, First_Name, Last_Name, Mobile_num, Email, User_Pass, ConfirmPass,User_Image);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("Registration_Result");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = categories.getJSONObject(i);


                            if (catObj.getString("Registration_Status") != null) {
                                if (catObj.getString("Registration_Status").equals("Insert Unsuccessful")) {
                                    DialogStatus = "Registration not successful... Enter valid details.!!!!";
                                    status = 1;

                                } else if (catObj.getString("Registration_Status").equals("User already exists with same Email Id. Try Another Email Address. !!!!")) {
                                    DialogStatus = "User already exists with same Email Id. Try with Another Email Address. !!!!";
                                    status = 2;

                                } else {

                                    DialogStatus = "Registaion Successfull...Welcome to Metlink Services: " + Title + " " + First_Name;
                                    status = 3;
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
            alertDialog = new AlertDialog.Builder(Register.this);
            alertDialog.setTitle("Alert Dialog");
            pDialog.setMessage("Registration in Process..");
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
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            if (status == 1) {

                                Intent intent = new Intent(Register.this, Register.class);
                                startActivity(intent);
                                finish();
                            }  else if (status == 2) {

                                Intent intent = new Intent(Register.this, Register.class);
                                startActivity(intent);
                                finish();
                            } else  {

                                Intent intent = new Intent(Register.this, MainActivity.class);
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
    public void thumbsupClick(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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