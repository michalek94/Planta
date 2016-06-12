package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import pl.planta.R;
import pl.planta.adapters.CustomListView;
import pl.planta.helper.SQLiteHandler;

public class ShopActivity extends Activity {

    private ListView mListView;
    private CustomListView mCustomListView;
    private SQLiteHandler mSQLiteHandler;
    private TextView mMoney;

    private String[] itemName = {
            "Przyspieszacz vol.10",
            "Przyspieszacz vol.25",
            "Przyspieszacz vol.50"
    };

    private String[] itemDescription = {
            "Prędkość wózka zostanie zwiększona o 10% na 5 godzin",
            "Prędkość wózka zostanie zwiększona o 25% na 5 godzin",
            "Prędkość wózka zostanie zwiększona o 50% na 5 godzin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shop);

        mCustomListView = new CustomListView(this, itemName, itemDescription);
        mListView = (ListView) findViewById(R.id.shopListView);
        mListView.setAdapter(mCustomListView);

        mSQLiteHandler = new SQLiteHandler(getApplicationContext());
        mMoney = (TextView) findViewById(R.id.tvMoney);

        HashMap<String,Integer> userMoney = mSQLiteHandler.getUserMoney();

        String money = userMoney.get("money").toString();

        mMoney.setText(money);
    }
}
