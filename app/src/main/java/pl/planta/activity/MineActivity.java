package pl.planta.activity;

import android.app.Activity;
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

public class MineActivity extends Activity {

    private static final String TAG = MineActivity.class.getSimpleName();
    private HashMap<String, Integer> handler;
    private ImageButton buttonEntrance;
    private ImageButton buttonPipeline;
    private PopupWindow popUp;
    private LayoutInflater layoutInflater;
    private android.support.percent.PercentRelativeLayout relativeLayout;
    private TextView tvMain;
    private TextView tvLvl;
    private TextView tvQue;
    private TextView tvCost;
    private TextView tvPrice;
    private ImageButton buttonLvlUp;
    private ImageButton buttonSell;
    private SQLiteHandler mSQLiteHandler;
    private int money;
    private int coal;
    private int columnNr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mine);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mSQLiteHandler = new SQLiteHandler(getApplicationContext());

        money = mSQLiteHandler.getUserMoney().get("money");

        final int width = dm.widthPixels;
        final int height = dm.heightPixels;
        final int widthPop = (int) (width * .35);
        final int heightPop = (int) (height * .6);

        relativeLayout = (android.support.percent.PercentRelativeLayout) findViewById(R.id.mineLayout);

        buttonEntrance = (ImageButton) findViewById(R.id.entranceButton);
        buttonPipeline = (ImageButton) findViewById(R.id.pipelineButton);

        HashMap<String, String> userUID = mSQLiteHandler.getUserUid();
        final String uid = userUID.get("uid");

        buttonEntrance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                columnNr = 8;
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popcoalwindow, null);
                popUp = new PopupWindow(container, widthPop, heightPop, true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);
                tvPrice = (TextView) popUp.getContentView().findViewById(R.id.textViewPopSellPrice);
                buttonLvlUp = (ImageButton) popUp.getContentView().findViewById(R.id.lvlUpButton);
                buttonSell = (ImageButton) popUp.getContentView().findViewById(R.id.sellButton);

                tvMain.setText("Kopalnia jest na poziomie: ");
                tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                tvQue.setText("Czy chcesz ulepszyć kopalnię na kolejny poziom za: ");
                tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));

                HashMap<String, Double> coalPrice = mSQLiteHandler.getCoalBonusPrice();
                String coal_price = coalPrice.get("coal_price").toString();
                tvPrice.setText(coal_price);

                buttonLvlUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);

                            HashMap<String, Integer> userMoney = mSQLiteHandler.getUserMoney();
                            saveMoneyOnServer(uid, userMoney.get("money"));
                            mSQLiteHandler.updateLevels(columnNr);
                            saveMineLevelOnServer(uid, mSQLiteHandler.getLevels(columnNr));

                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś kopalnię do " + String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                buttonSell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler = mSQLiteHandler.getAmounts();
                        coal = handler.get("coal_amount");
                        double getCoal = coal * 2.0;
                        mSQLiteHandler.updateMoney(money + (int) getCoal);
                        mSQLiteHandler.updateCoalHighScore(0);
                        Toast.makeText(getApplicationContext(), "Gratulacje! Sprzedałeś " + coal + " ton węgla!", Toast.LENGTH_SHORT).show();
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

        buttonPipeline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                columnNr = 7;
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow, null);
                popUp = new PopupWindow(container, widthPop, heightPop, true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);
                buttonLvlUp = (ImageButton) popUp.getContentView().findViewById(R.id.lvlUpButton);

                tvMain.setText("Rurociąg jest na poziomie: ");
                tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                tvQue.setText("Czy chcesz ulepszyć rurociąg na kolejny poziom za: ");
                tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));

                buttonLvlUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);

                            HashMap<String, Integer> userMoney = mSQLiteHandler.getUserMoney();
                            saveMoneyOnServer(uid, userMoney.get("money"));
                            mSQLiteHandler.updateLevels(columnNr);
                            savePipelineLevelOnServer(uid, mSQLiteHandler.getLevels(columnNr));

                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś rurociąg do " + String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
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
    }

    private void saveMineLevelOnServer(final String uid, final int mineLevel) {
        String tag_string_req = "req_update_mine_level";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_MINE_LEVEL_UPDATE, new Response.Listener<String>() {
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
                params.put("mine_level", String.valueOf(mineLevel));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void savePipelineLevelOnServer(final String uid, final int pipelineLevel) {
        String tag_string_req = "req_update_pipeline_level";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_PIPELINE_LEVEL_UPDATE, new Response.Listener<String>() {
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
                params.put("pipeline_level", String.valueOf(pipelineLevel));

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
