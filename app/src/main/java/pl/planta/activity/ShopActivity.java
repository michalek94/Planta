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
            "100 złota",
            "500 złota",
            "1000 złota",
            "100 platyny",
            "500 platyny",
            "1000 platyny"
    };

    private String[] itemDescription = {
            "Otrzymasz 100 złota w grze",
            "Otrzymasz 500 złota w grze",
            "Otrzymasz 1000 złota w grze",
            "Otrzymasz 100 platyny w grze",
            "Otrzymasz 500 platyny w grze",
            "Otrzymasz 1000 platyny w grze"
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
