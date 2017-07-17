package nz.ac.app.metlink;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by 21600481 on 16-09-2016.
 */


//8.	https://www.youtube.com/watch?v=cp2rL3sAFmI( SQLlite database tutorial)
//9.	https://www.youtube.com/watch?v=p8TaTgr4uKM (Sql lite tutorial)

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = DatabaseHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "Metlink.db";
    private static final int DATABASE_VERSION = 5;

    //Table Names
    public static final String TABLE_REGISTER = "User_Reg_Table";
    public static final  String TABLE_LOGIN_DETIALS="Login_Details";

    //Table-Login details- column names
    public static final String Email="Emil_ID";
    public static final String Password="Password";

    //User_Register- column names
    public static final String COLID = "User_ID";
    public static final String COL1 = "Title";
    public static final String COL2 = "First_Name";
    public static final String COL3 = "Last_Name";
    public static final String COL4 = "Mobile_num";
    public static final String COL5 = "Email";
    public static final String COL6 = "Password";
    public static final String COL7 = "ConfirmPass";





    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table " + TABLE_LOGIN_DETIALS + "( Emil_ID TEXT, Password TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_DETIALS);
        // db.execSQL(CREATE_TABLE_TRAIN_FARE);

            //onCreate(db);

    }


    //Insert data into User_register table
    public boolean insertData(String title, String firstName, String lastName, String Mobile, String Email, String Password, String ConfirmPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, title);
        contentValues.put(COL2, firstName);
        contentValues.put(COL3, lastName);
        contentValues.put(COL4, Mobile);
        contentValues.put(COL5, Email);
        contentValues.put(COL6, Password);
        contentValues.put(COL7, ConfirmPass);
        long result = db.insert(TABLE_REGISTER, null, contentValues);
        if (result == -1)
            return false;
        else {
            Log.d(TAG, "User Inserted" + result);
            return true;
        }
    }

    //Insert data into User_Login_Details table
    public boolean insertLoginDetails(String Email_ID, String Pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Email, Email_ID);
        contentValues.put(Password, Pass);
        long result = db.insert(TABLE_LOGIN_DETIALS, null, contentValues);
        if (result == -1)
            return false;
        else {
            Log.d(TAG, "Login Details Inserted" + result);
            return true;
        }
    }


    // Check Login Credentials
    public Cursor CheckLoginDetails(String Username, String Password) {

         SQLiteDatabase db = this.getWritableDatabase();
        //14.	http://stackoverflow.com/questions/903343/cursor-get-the-field-value-android (Cursor )

        //Cursor res=db.rawQuery("select Email, Password from "+TABLE_NAME+" where "+COL5.toString()+" = "+Username+" and "+COL6.toString()+" = "+Password,null);
        Cursor res = db.rawQuery("select Emil_ID, Password from " + TABLE_LOGIN_DETIALS + " where Emil_ID=? AND Password=? ", new String[]{Username, Password});
        return res;

    }


}





