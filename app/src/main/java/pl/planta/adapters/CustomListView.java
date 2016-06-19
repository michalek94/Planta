package pl.planta.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pl.planta.R;
import pl.planta.app.AppConfiguration;
import pl.planta.app.AppController;
import pl.planta.helper.SQLiteHandler;

/**
 * Created by panko on 11.06.2016.
 */
public class CustomListView extends ArrayAdapter<String> {

    private static final String TAG = CustomListView.class.getSimpleName();

    private final Activity context;
    private final String[] itemName;
    private final String[] itemDescription;
    private final String[] itemPrice;
    private View        mView;
    private TextView    mTitle;
    private TextView    mDesc;
    private TextView    mPrice;
    private TextView    mMoney;
    int                 money;

    private Button      mBuy;
    private SQLiteHandler mSQLiteHandler;

    public CustomListView(Activity context, String[] itemName, String[] itemDescription, String[] itemPrice) {
        super(context, R.layout.shop_list, itemName);

        this.context = context;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        mView = inflater.inflate(R.layout.shop_list, null, true);

        mTitle = (TextView) mView.findViewById(R.id.itemName);
        mDesc = (TextView) mView.findViewById(R.id.itemDesc);
        mMoney = (TextView) (context).findViewById(R.id.tvMoney);
        mPrice = (TextView) mView.findViewById(R.id.itemPrice);
        mBuy = (Button) mView.findViewById(R.id.itemBuy);
        mSQLiteHandler = new SQLiteHandler(context);

        mTitle.setText(itemName[position]);
        mDesc.setText(itemDescription[position]);
        mPrice.setText(itemPrice[position]);

        money = mSQLiteHandler.getUserMoney().get("money");

        HashMap<String, String> userUID = mSQLiteHandler.getUserUid();
        final String uid = userUID.get("uid");

        mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = mSQLiteHandler.getUserMoney().get("money");
                if(position == 0) {
                    if(money >= 600) {
                        mSQLiteHandler.updateCoalBonus(1.5);
                        saveCoalBonusOnServer(uid, 1.5);
                        mSQLiteHandler.updateMoney(money-600);
                        String money1 = mSQLiteHandler.getUserMoney().get("money").toString();
                        mMoney.setText(money1);
                    } else {
                        Toast.makeText(context, "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 1) {
                    if(money >= 600) {
                        mSQLiteHandler.updatePipeBonus(1.5);
                        savePipeBonusOnServer(uid, 1.5);
                        mSQLiteHandler.updateMoney(money-600);
                        String money1 = mSQLiteHandler.getUserMoney().get("money").toString();
                        mMoney.setText(money1);
                    } else {
                        Toast.makeText(context, "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 2) {
                    if(money >= 1000) {
                        mSQLiteHandler.updateCoalBonus(1.1);
                        saveCoalBonusOnServer(uid, 1.1);
                        mSQLiteHandler.updateMoney(money-1000);
                        String money1 = mSQLiteHandler.getUserMoney().get("money").toString();
                        mMoney.setText(money1);
                    } else {
                        Toast.makeText(context, "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 3) {
                    if(money >= 1000) {
                        mSQLiteHandler.updatePipeBonus(1.1);
                        savePipeBonusOnServer(uid, 1.1);
                        mSQLiteHandler.updateMoney(money-1000);
                        String money1 = mSQLiteHandler.getUserMoney().get("money").toString();
                        mMoney.setText(money1);
                    } else {
                        Toast.makeText(context, "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return mView;
    }

    private void saveCoalBonusOnServer(final String uid, final double coalBonus) {
        String tag_string_req = "req_update_coal_bonus";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_COAL_BONUS_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("coal_bonus", String.valueOf(coalBonus));

                return params;
            }
        };
        AppController.getInstance(context).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void savePipeBonusOnServer(final String uid, final double pipeBonus) {
        String tag_string_req = "req_update_pipe_bonus";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_PIPE_BONUS_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("pipe_bonus", String.valueOf(pipeBonus));

                return params;
            }
        };
        AppController.getInstance(context).addToRequestQueue(stringRequest, tag_string_req);
    }
}
