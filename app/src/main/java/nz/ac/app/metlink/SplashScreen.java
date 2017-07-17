package nz.ac.app.metlink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread timeThread=new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {


                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
            }

        };
        timeThread.start();

    }

    @Override
    protected void onPause() {

        super.onPause();
        finish();
    }
}
