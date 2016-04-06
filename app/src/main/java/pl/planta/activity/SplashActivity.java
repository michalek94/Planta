package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import pl.planta.R;
import pl.planta.helper.SessionManager;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(getApplicationContext());

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (sessionManager.checkAppStart(getApplicationContext())) {
                    /**
                     * aplikacja uruchamia się normalnie (bez aktualizacji i instalacji)
                     */
                    case NORMAL:
                        Log.w("SplashActivity", "normal run");
                        if (sessionManager.isLoggedIn()) {
                            Log.w(TAG, "Starting MenuActivity");
                            Intent menuIntent = new Intent(SplashActivity.this, MenuActivity.class);
                            startActivity(menuIntent);
                            finish();
                        } else {
                            Log.w(TAG, "Starting ChooseActivity");
                            Intent chooseIntent = new Intent(SplashActivity.this, ChooseActivity.class);
                            startActivity(chooseIntent);
                            finish();
                        }
                        break;
                    /**
                     * po aktualizacji aplikacji może pokazać się okienko dialogowe informujące o zmianach
                     */
                    case FIRST_TIME_VERSION:
                        Log.w(TAG, "First time version run");
                        if (sessionManager.isLoggedIn()) {
                            Log.w(TAG, "Starting MenuActivity");
                            Intent menuIntent = new Intent(SplashActivity.this, MenuActivity.class);
                            startActivity(menuIntent);
                            finish();
                        } else {
                            Log.w(TAG, "Starting ChooseActivity");
                            Intent menuIntent = new Intent(SplashActivity.this, ChooseActivity.class);
                            startActivity(menuIntent);
                            finish();
                        }
                        break;
                    /**
                     * aplikacja uruchamia się na urządzeniu po raz pierwszy
                     */
                    case FIRST_TIME:
                        Log.w(TAG, "First time run");
                        Log.w(TAG, "Starting ChooseActivity");
                        Intent chooseIntent = new Intent(SplashActivity.this, ChooseActivity.class);
                        startActivity(chooseIntent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
