package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import pl.planta.R;

public class JanuszWorkActivity extends Activity {

    private ImageButton heapButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_januszw);

        heapButton = (ImageButton) findViewById(R.id.heapButton);

        heapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(JanuszWorkActivity.this, GamingTestsActivity.class);
                startActivity(gameIntent);
            }
        });

    }

}
