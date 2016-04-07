package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import pl.planta.R;

public class MainMenuActivity extends Activity {

    private ImageButton cityButton;
    private ImageButton januszButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        cityButton = (ImageButton)findViewById(R.id.cityButton);
        januszButton = (ImageButton)findViewById(R.id.januszButton);

        cityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainMenuActivity.this, CityActivity.class);
                startActivity(gameIntent);
            }
        });

       januszButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainMenuActivity.this, JanuszWorkActivity.class);
                startActivity(gameIntent);
            }
        });

    }
}
