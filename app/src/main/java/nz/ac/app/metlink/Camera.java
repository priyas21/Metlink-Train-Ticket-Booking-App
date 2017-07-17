package nz.ac.app.metlink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;


public class Camera extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    Button btnRegister, btnupload;
    ImageView imageViewHome, ivcamera, ivgallery, ivimage;
    int flag = 0;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 21200;
    final int GALLERY_REQUEST = 12546;
    int status = 0;
    AlertDialog.Builder alertDialog;
    String register_url = "http://tranzmetrometlink.com/register.php";

    private Bitmap User_Image;
    private Uri file_uri;
    private File file;
    String picturePath;
    Uri selectedPhoto;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private String encoded_string, image_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int whitecolor = Color.WHITE;
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(whitecolor);


        ivcamera = (ImageView) findViewById(R.id.imageViewcamera);
        ivgallery = (ImageView) findViewById(R.id.imageViewGallery);


        btnupload = (Button) findViewById(R.id.buttonupload);

        ivimage = (ImageView) findViewById(R.id.ivimage);
        cameraPhoto = new CameraPhoto(this);
        galleryPhoto = new GalleryPhoto(this);

        // buttonRegisterCicked();
        ivcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(Camera.this, "Something Wrong whiile taking photos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivgallery.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            String photoPath=cameraPhoto.getPhotoPath();
            try {
                Bitmap bitmap= ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                ivimage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(Camera.this, "Something Wrong whiile loading photos", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG,photoPath);

        }
    }
}



