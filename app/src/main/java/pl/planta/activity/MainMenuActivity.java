package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

import pl.planta.R;
import pl.planta.helper.SQLiteHandler;
import pl.planta.thread.Refresh;

public class MainMenuActivity extends Activity {

    private ImageButton cityButton;
    private ImageButton januszButton;
    private ImageButton plantaButton;
    private ImageButton mineButton;
    private TextView    mMoney;
    private TextView    mCoal;
    private SQLiteHandler mSQLiteHandler;
    private Refresh refresh;
    private long refreshTime = 1000;

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
        mCoal = (TextView) findViewById(R.id.coalTextView1);

        HashMap<String,Integer> userMoney = mSQLiteHandler.getUserMoney();
        HashMap<String,Integer> userCoal = mSQLiteHandler.getUserCoalScores();

        String money = userMoney.get("money").toString();
        String coal = userCoal.get("coal_highscore").toString();

        mMoney.setText(money);
        mCoal.setText(coal);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String,Integer> userMoney = mSQLiteHandler.getUserMoney();
        HashMap<String,Integer> userCoal = mSQLiteHandler.getUserCoalScores();

        String money = userMoney.get("money").toString();
        String coal = userCoal.get("coal_highscore").toString();

        mMoney.setText(money);
        mCoal.setText(coal);
        refresh = new Refresh(this,refreshTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        refresh.stop();
    }



}
