package pl.planta.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import pl.planta.R;
import pl.planta.helper.SQLiteHandler;

/**
 * Created by panko on 11.06.2016.
 */
public class CustomListView extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemName;
    private final String[] itemDescription;
    private View        mView;
    private TextView    mTitle;
    private TextView    mDesc;

    private Button      mBuy;
    private SQLiteHandler mSQLiteHandler;

    public CustomListView(Activity context, String[] itemName, String[] itemDescription) {
        super(context, R.layout.shop_list, itemName);

        this.context = context;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        mView = inflater.inflate(R.layout.shop_list, null, true);

        mTitle = (TextView) mView.findViewById(R.id.itemName);
        mDesc = (TextView) mView.findViewById(R.id.itemDesc);
        final TextView mMoney =  (TextView) ((Activity)context).findViewById(R.id.tvMoney);
        mBuy =(Button) mView.findViewById(R.id.itemBuy);
        mSQLiteHandler = new SQLiteHandler(context);

        mTitle.setText(itemName[position]);
        mDesc.setText(itemDescription[position]);

        final HashMap<String,Integer> userMoney = mSQLiteHandler.getUserMoney();

        final int money = userMoney.get("money");

        mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0) {
                    if(money >= 300) {
                        mSQLiteHandler.updateCoalBonus(1, 1.1);
                        mSQLiteHandler.updateMoney(1, money-300);
                        String money1 = userMoney.get("money").toString();
                        mMoney.setText(money1);
                    } else {
                        Toast.makeText(context, "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 1) {
                    if(money >= 600) {
                        mSQLiteHandler.updateCoalBonus(1, 1.25);
                        mSQLiteHandler.updateMoney(1, money-600);
                    } else {
                        Toast.makeText(context, "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 2) {
                    if(money >= 1000) {
                        mSQLiteHandler.updateCoalBonus(1, 1.50);
                        mSQLiteHandler.updateMoney(1, money-1000);
                    } else {
                        Toast.makeText(context, "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return mView;
    }
}
