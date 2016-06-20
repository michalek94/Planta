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
import pl.planta.thread.Save;

public class MainMenuActivity extends Activity {

    private ImageButton cityButton;
    private ImageButton januszButton;
    private ImageButton plantaButton;
    private ImageButton mineButton;
    private TextView    mMoney;
    private TextView    mWater;
    private TextView    mElectricity;
    private TextView    mCoal;
    private SQLiteHandler mSQLiteHandler;
    private Refresh refresh;
    private long refreshTime = 10000;

    private Save save;

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
        mWater =(TextView) findViewById(R.id.waterTextView1);
        mElectricity = (TextView) findViewById(R.id.electricityTextView1);

        save = new Save(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String,Integer> userMoney = mSQLiteHandler.getUserMoney();
        HashMap<String,Integer> userAmounts = mSQLiteHandler.getAmounts();

        String money = userMoney.get("money").toString();
        String coal = userAmounts.get("coal_amount").toString();
        String water = userAmounts.get("pipe_amount").toString();
        String electricity = userAmounts.get("electricity_amount").toString();

        mMoney.setText(money);
        mCoal.setText(coal);
        mWater.setText(water);
        mElectricity.setText(electricity);

        refresh = new Refresh(this,refreshTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        HashMap<String,Integer> userAmounts = mSQLiteHandler.getAmounts();
        HashMap<String, String> userUID = mSQLiteHandler.getUserUid();
        String uid = userUID.get("uid");
        save.saveCoalAmountOnServer(uid,userAmounts.get("coal_amount"));
        save.saveElecAmountOnServer(uid,userAmounts.get("electricity_amount"));
        save.savePipeAmountOnServer(uid,userAmounts.get("pipe_amount"));
        refresh.stop();
    }
}
