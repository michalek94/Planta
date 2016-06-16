package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import pl.planta.R;
import pl.planta.helper.SessionManager;

public class ChooseActivity extends Activity {

    private static final String TAG = ChooseActivity.class.getSimpleName();

    private Button btn_Login, btn_Register;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Set flags to make full screen on device
         */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**
         * Set screen's view (layout)
         */
        setContentView(R.layout.activity_choose);
        /**
         * Initialize instance of SessionManager
         */
        sessionManager = new SessionManager(getApplicationContext());
        /**
         * Initialize required buttons
         */
        btn_Login = (Button) findViewById(R.id.btn_Login);
        btn_Register = (Button) findViewById(R.id.btn_Register);
        /**
         *
         */
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ChooseActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
        /**
         * Default register
         */
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(ChooseActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
        /**
         * If user is already logged in, take him to MenuActivity
         */
        if (sessionManager.isLoggedIn()) {
            // Użytkownik jest zalogowany. Przenoszę go do MenuActivity
            Intent menuIntent = new Intent(ChooseActivity.this, MenuActivity.class);
            startActivity(menuIntent);
            finish();
        }
    }
}
