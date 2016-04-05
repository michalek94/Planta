package pl.planta.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class InternetConnection {

    private static final String TAG = InternetConnection.class.getSimpleName();

    public InternetConnection() {
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()){
            Log.d(TAG, "Internet connection available.");
            return true;
        }
        else{
            Log.d(TAG, "There is no an Internet connection.");
            return false;
        }
    }
}
