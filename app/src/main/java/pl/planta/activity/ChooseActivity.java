package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.facebook.FacebookSdk;

import pl.planta.helper.SessionManager;
import pl.planta.R;

public class ChooseActivity extends Activity {

    private Button btnRegister;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose);

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        // Sprawdź czy użytkownik jest już zalogowany.
        // Jeśli tak, przenieś go do MenuActivity
        if(sessionManager.isLoggedIn()){
            // Użytkownik jest zalogowany. Przenoszę go do MenuActivity
            Intent menuIntent = new Intent(ChooseActivity.this, MenuActivity.class);
            startActivity(menuIntent);
            finish();
        }
    }
}
