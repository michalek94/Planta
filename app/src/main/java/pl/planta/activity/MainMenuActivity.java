package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import pl.planta.R;
import pl.planta.helper.SQLiteHandler;

public class MainMenuActivity extends Activity {

    private ImageButton cityButton;
    private ImageButton januszButton;
    private ImageButton plantaButton;
    private ImageButton mineButton;
    private TextView    mMoney;
    private SQLiteHandler mSQLiteHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        cityButton = (ImageButton) findViewById(R.id.cityButton);
        januszButton = (ImageButton) findViewById(R.id.januszButton);
        plantaButton = (ImageButton) findViewById(R.id.plantaButton);
        mineButton = (ImageButton) findViewById(R.id.mineButton);

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

        plantaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainMenuActivity.this, PlantaActivity.class);
                startActivity(gameIntent);
            }
        });

        mineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MainMenuActivity.this, MineActivity.class);
                startActivity(gameIntent);
            }
        });

        mSQLiteHandler = new SQLiteHandler(getApplicationContext());
        mMoney = (TextView) findViewById(R.id.cashTextView1);

        HashMap<String,Integer> userMoney = mSQLiteHandler.getUserMoney();

        String money = userMoney.get("money").toString();

        mMoney.setText(money);
    }
}
