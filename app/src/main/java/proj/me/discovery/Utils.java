package proj.me.discovery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by root on 20/12/15.
 */
public class Utils {
    private static final String TAG = "Log";
    public static boolean IS_LOGGED_IN = false;
    public static int defineUser = 0;
    public static void logMessage(String message){
        Log.i(TAG, message);
    }
    public static void logError(String message){
        Log.e(TAG, message);
    }
    public static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static String formatMessage(String message, Object ... values){
        return String.format(message, values);
    }
    public static boolean isInternetConnected(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}
