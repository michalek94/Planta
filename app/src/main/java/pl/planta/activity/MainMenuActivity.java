package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import pl.planta.planta.R;

public class MainMenuActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
    }
}
