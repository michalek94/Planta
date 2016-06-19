package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
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

public class CityActivity extends Activity {

    private static final String TAG = CityActivity.class.getSimpleName();

    private ImageButton buttonResidence;
    private ImageButton buttonIndustry;
    private ImageButton buttonShop;
    private PopupWindow popUp;
    private LayoutInflater layoutInflater;
    private android.support.percent.PercentRelativeLayout relativeLayout;
    private TextView tvMain;
    private TextView tvLvl;
    private TextView tvQue;
    private TextView tvCost;
    private int columnNr;
    private SQLiteHandler mSQLiteHandler;
    private ImageButton buttonLvlUp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_city);

        mSQLiteHandler = new SQLiteHandler(getApplicationContext());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int width = dm.widthPixels;
        final int height = dm.heightPixels;
        final int widthPop = (int) (width * .35);
        final int heightPop = (int) (height * .55);

        relativeLayout = (android.support.percent.PercentRelativeLayout) findViewById(R.id.cityLayout);

        buttonResidence = (ImageButton) findViewById(R.id.residenceButton);
        buttonIndustry = (ImageButton) findViewById(R.id.industryButton);
        buttonShop = (ImageButton) findViewById(R.id.shopButton);

        HashMap<String, String> userUID = mSQLiteHandler.getUserUid();
        final String uid = userUID.get("uid");

        buttonResidence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                columnNr = 6;
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow, null);
                popUp = new PopupWindow(container, widthPop, heightPop, true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);

                tvMain.setText("Mieszkania są na poziomie: ");
                tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                tvQue.setText("Czy chcesz ulepszyć mieszkania na kolejny poziom za: ");
                tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));

                buttonLvlUp = (ImageButton) popUp.getContentView().findViewById(R.id.lvlUpButton);

                buttonLvlUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);
                            HashMap<String, Integer> userMoney = mSQLiteHandler.getUserMoney();
                            saveMoneyOnServer(uid, userMoney.get("money"));
                            mSQLiteHandler.updateLevels(columnNr);
                            saveFlatsLevelOnServer(uid, mSQLiteHandler.getLevels(columnNr));
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś mieszkania do " + String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur = (int) (width * .7);
                final int heightFur = (int) (height * .2);
                popUp.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, widthFur, heightFur);


                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popUp.dismiss();
                        return false;
                    }
                });

            }

        });

        buttonIndustry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                columnNr = 5;
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow, null);
                popUp = new PopupWindow(container, widthPop, heightPop, true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);

                tvMain.setText("Fabryka jest na poziomie: ");
                tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                tvQue.setText("Czy chcesz ulepszyć fabrykę na kolejny poziom za: ");
                tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));

                buttonLvlUp = (ImageButton) popUp.getContentView().findViewById(R.id.lvlUpButton);

                buttonLvlUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);
                            HashMap<String, Integer> userMoney = mSQLiteHandler.getUserMoney();
                            saveMoneyOnServer(uid, userMoney.get("money"));
                            mSQLiteHandler.updateLevels(columnNr);
                            saveFactoryLevelOnServer(uid, mSQLiteHandler.getLevels(columnNr));
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś fabryki do " + String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur = (int) (width * .1);
                final int heightFur = (int) (height * .2);
                popUp.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, widthFur, heightFur);


                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        popUp.dismiss();
                        return false;
                    }
                });

            }

        });

        buttonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveFlatsLevelOnServer(final String uid, final int flatsLevel) {
        String tag_string_req = "req_update_flats_level";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_FLATS_LEVEL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("flats_level", String.valueOf(flatsLevel));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveFactoryLevelOnServer(final String uid, final int factoryLevel) {
        String tag_string_req = "req_update_factory_level";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_FACTORY_LEVEL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("factory_level", String.valueOf(factoryLevel));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveMoneyOnServer(final String uid, final int money) {
        String tag_string_req = "req_update_money";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_MONEY_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("money", String.valueOf(money));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

}
