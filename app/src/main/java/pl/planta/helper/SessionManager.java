package pl.planta.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private Editor editor;
    private static Context mContext;
    private PackageInfo packageInfo;

    /**
     * Tryb SharedPreferences |
     * 0 = prywatny, tylko ta aplikacja ma do nich dostęp
    */
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "android_planta";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_LAST_APP_VERSION = "lastAppVersion";
    private static final String KEY_FACEBOOK_USER_ID = "fb_user_id";
    private static final String KEY_FACEBOOK_USER_NAME = "fb_user_name";
    private static final String KEY_FACEBOOK_USER_EMAIL = "fb_user_email";

    public SessionManager(Context context){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();

        Log.d(TAG, "Sesja logowania uzytkownika zmieniona.");
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void saveFacebookCredentials(int id, String name, String email){
        editor.putInt(KEY_FACEBOOK_USER_ID, id);
        editor.putString(KEY_FACEBOOK_USER_NAME, name);
        editor.putString(KEY_FACEBOOK_USER_EMAIL, email);
        editor.commit();

        Log.d(TAG, "Zapisano dane uzytkownika z FB do SharedPreferences");
    }

    public void setLastAppVersion(int currentVersioncode){
        editor.putInt(KEY_LAST_APP_VERSION, currentVersioncode);
        editor.commit();

        Log.d(TAG, "Ustawiono ostatnia wersje aplikacji.");
    }

    /**
     * public enum AppStart
     * It is used to create variables that can take only certain predetermined values:
     * params FIRST_TIME - for app's first time run
     * params FIRST_TIME_VERSIOB - for app's first time run when the new app's code is greather than last one
     * params NORMAL - for app's in normal activity
     */
    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL
    }

    /**
     * Caches the result of {@link #checkAppStart(Context context)}.
     * To allow idempotent method calls.
     */
    private static AppStart appStart = null;

    /**
     * Finds out started for the first time (ever or in the current version).
     * @return the type of app start
     */
    public AppStart checkAppStart(Context context) {

        SessionManager sessionManager = new SessionManager(context);

        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            int lastVersionCode = sharedPreferences.getInt(KEY_LAST_APP_VERSION, -1);
            int currentVersionCode = packageInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);

            // Zaktualizuj wersję w SharedPreferences
           sessionManager.setLastAppVersion(currentVersionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Nie można rozpoznać aktualnej wersji kodu. Uruchamianie normalne aplikacji.");
        }
        return appStart;
    }

    /**
     * @param currentVersionCode current version of app
     * @param lastVersionCode last version, before update
     * @return first_time || first_time_version || normal
     */
    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else if (lastVersionCode > currentVersionCode) {
            Log.w(TAG, "Aktualna wersja kodu (" + currentVersionCode
                    + ") jest mniejsza niż ostatnio rozpoznana ("
                    + lastVersionCode
                    + "). Uruchamianie normalne aplikacji.");
            return AppStart.NORMAL;
        } else {
            return AppStart.NORMAL;
        }
    }
}
