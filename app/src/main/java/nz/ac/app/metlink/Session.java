package nz.ac.app.metlink;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 21600481 on 06-10-2016.
 */

//	https://www.youtube.com/watch?v=RWmbONsSg1E#t=52.29446 (shared Preferences)
public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx)
    {
        this.ctx=ctx;
        prefs=ctx.getSharedPreferences("MyAppPref",Context.MODE_PRIVATE);
        editor=prefs.edit();
    }
    public void setLoggedin(boolean loggedin)
    {
        editor.putBoolean("LoggedInMode",loggedin);
        editor.commit();
    }
    public boolean loggedIn()
    {
        return prefs.getBoolean("LoggedInMode",true);
    }
}
