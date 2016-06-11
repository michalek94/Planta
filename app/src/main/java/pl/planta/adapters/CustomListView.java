package pl.planta.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import pl.planta.R;

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
        mBuy =(Button) mView.findViewById(R.id.itemBuy);

        mTitle.setText(itemName[position]);
        mDesc.setText(itemDescription[position]);

        mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 3) {
                    Log.d("CustomListView", "Kliknieta pozycja: " + 3);
                }
            }
        });

        return mView;
    }
}
