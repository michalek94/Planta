package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

public class PlantaActivity extends Activity {

    private static final String TAG = PlantaActivity.class.getSimpleName();

    private ImageButton buttonFurnace;
    private ImageButton buttonHook;
    private ImageButton buttonDoor;
    private ImageButton buttonComputer;
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
        setContentView(R.layout.activity_planta);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int width = dm.widthPixels;
        final int height = dm.heightPixels;
        final int widthPop = (int) (width * .35);
        final int heightPop = (int) (height * .55);

        relativeLayout = (android.support.percent.PercentRelativeLayout) findViewById(R.id.plantaLayout);

        buttonFurnace = (ImageButton) findViewById(R.id.furnaceButton);
        buttonHook = (ImageButton) findViewById(R.id.hookButton);
        buttonComputer = (ImageButton) findViewById(R.id.computerButton);
        buttonDoor = (ImageButton) findViewById(R.id.doorButton);

        mSQLiteHandler = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> userUID = mSQLiteHandler.getUserUid();
        final String uid = userUID.get("uid");

        buttonFurnace.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                columnNr = 4;

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow, null);
                popUp = new PopupWindow(container, widthPop, heightPop, true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);

                tvMain.setText("Piec jest na poziomie: ");
                tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                tvQue.setText("Czy chcesz ulepszyć piec na kolejny poziom za: ");
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
                            saveFurnaceLevelOnServer(uid, mSQLiteHandler.getLevels(columnNr));
                            saveFurnacePriceOnServer(uid, mSQLiteHandler.getPrice(columnNr));

                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś piec do " + String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur = (int) (width * .7);
                final int heightFur = (int) (height * .35);
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

        buttonHook.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                columnNr = 2;

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow, null);
                popUp = new PopupWindow(container, widthPop, heightPop, true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);

                tvMain.setText("Hak jest na poziomie: ");
                tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                tvQue.setText("Czy chcesz ulepszyć hak na kolejny poziom za: ");
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
                            saveHookLevelOnServer(uid, mSQLiteHandler.getLevels(columnNr));
                            saveHookPriceOnServer(uid, mSQLiteHandler.getPrice(columnNr));

                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś dźwig do " + String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur = (int) (width * .4);
                final int heightFur = (int) (height * .05);
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

        buttonComputer.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                columnNr = 1;

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow, null);
                popUp = new PopupWindow(container, widthPop, heightPop, true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);

                tvMain.setText("Komputer jest na poziomie: ");
                tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                tvQue.setText("Czy chcesz ulepszyć komputer na kolejny poziom za: ");
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
                            saveComputerLevelOnServer(uid, mSQLiteHandler.getLevels(columnNr));
                            saveComputerPriceOnServer(uid, mSQLiteHandler.getPrice(columnNr));

                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś panel sterowania do " + String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur = (int) (width * .10);
                final int heightFur = (int) (height * .3);
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

        buttonDoor.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                columnNr = 3;

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow, null);
                popUp = new PopupWindow(container, widthPop, heightPop, true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);

                tvMain.setText("Magazyn jest na poziomie: ");
                tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                tvQue.setText("Czy chcesz ulepszyć magazyn na kolejny poziom za: ");
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
                            saveStoreroomLevelOnServer(uid, mSQLiteHandler.getLevels(columnNr));
                            saveStoreroomPriceOnServer(uid, mSQLiteHandler.getPrice(columnNr));

                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś magazyn do " + String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur = (int) (width * .45);
                final int heightFur = (int) (height * .35);
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

    private void saveFurnaceLevelOnServer(final String uid, final int furnaceLevel) {
        String tag_string_req = "req_update_furnace_level";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_FURNACE_LEVEL_UPDATE, new Response.Listener<String>() {
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
                params.put("furnace_level", String.valueOf(furnaceLevel));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveFurnacePriceOnServer(final String uid, final int furnacePrice) {
        String tag_string_req = "req_update_furnace_price";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_FURNACE_PRICE_UPDATE, new Response.Listener<String>() {
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
                params.put("furnace_price", String.valueOf(furnacePrice));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveHookLevelOnServer(final String uid, final int hookLevel) {
        String tag_string_req = "req_update_hook_level";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_HOOK_LEVEL_UPDATE, new Response.Listener<String>() {
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
                params.put("hook_level", String.valueOf(hookLevel));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveHookPriceOnServer(final String uid, final int hookPrice) {
        String tag_string_req = "req_update_hook_price";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_HOOK_PRICE_UPDATE, new Response.Listener<String>() {
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
                params.put("hook_price", String.valueOf(hookPrice));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveComputerLevelOnServer(final String uid, final int computerLevel) {
        String tag_string_req = "req_update_computer_level";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_COMPUTER_LEVEL_UPDATE, new Response.Listener<String>() {
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
                params.put("computer_level", String.valueOf(computerLevel));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveComputerPriceOnServer(final String uid, final int computerPrice) {
        String tag_string_req = "req_update_computer_price";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_COMPUTER_PRICE_UPDATE, new Response.Listener<String>() {
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
                params.put("computer_price", String.valueOf(computerPrice));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveStoreroomLevelOnServer(final String uid, final int storeroomLevel) {
        String tag_string_req = "req_update_storeroom_level";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_STOREROOM_LEVEL_UPDATE, new Response.Listener<String>() {
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
                params.put("storeroom_level", String.valueOf(storeroomLevel));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }

    private void saveStoreroomPriceOnServer(final String uid, final int storeroomPrice) {
        String tag_string_req = "req_update_storeroom_price";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_STOREROOM_PRICE_UPDATE, new Response.Listener<String>() {
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
                params.put("storeroom_price", String.valueOf(storeroomPrice));

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
