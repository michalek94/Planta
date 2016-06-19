package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
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
            "Bonus +10%",
            "Bonus +10%",
            "Bonus +50%",
            "Bonus +50%"
    };

    private String[] itemDescription = {
            "Posiadając ten bonus będziesz otrzymywać +10% więcej węgla przez cały czas",
            "Posiadając ten bonus będziesz otrzymywać +10% więcej wody przez cały czas",
            "Posiadając ten bonus będziesz otrzymywać +50% więcej węgla",
            "Posiadając ten bonus będziesz otrzymywać +50% więcej wody"
    };

    private String[] itemPrice = {
            "Cena: 600 sztuk złota",
            "Cena: 600 sztuk złota",
            "Cena: 1000 sztuk złota",
            "Cena: 1000 sztuk złota"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shop);

        mCustomListView = new CustomListView(this, itemName, itemDescription, itemPrice);
        mListView = (ListView) findViewById(R.id.shopListView);
        mListView.setAdapter(mCustomListView);

        mSQLiteHandler = new SQLiteHandler(getApplicationContext());
        mMoney = (TextView) findViewById(R.id.tvMoney);

        HashMap<String, Integer> userMoney = mSQLiteHandler.getUserMoney();

        String money = userMoney.get("money").toString();

        mMoney.setText(money);
    }
}
