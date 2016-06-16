package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import pl.planta.R;
import pl.planta.helper.SQLiteHandler;

public class MineActivity extends Activity {

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
    HashMap<String, Integer> handler;

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
        final int widthPop =(int) (width * .35);
        final int heightPop = (int) (height * .6);

        relativeLayout = (android.support.percent.PercentRelativeLayout) findViewById(R.id.mineLayout);

        buttonEntrance = (ImageButton)findViewById(R.id.entranceButton);
        buttonPipeline = (ImageButton)findViewById(R.id.pipelineButton);

        buttonEntrance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popcoalwindow,null);
                popUp = new PopupWindow(container,widthPop,heightPop,true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);
                tvPrice = (TextView) popUp.getContentView().findViewById(R.id.textViewPopSellPrice);
                buttonLvlUp = (ImageButton) popUp.getContentView().findViewById(R.id.lvlUpButton);
                buttonSell = (ImageButton) popUp.getContentView().findViewById(R.id.sellButton);

                tvMain.setText("Kopalnia jest na poziomie: ");
                tvLvl.setText("2");
                tvQue.setText("Czy chcesz ulepszyć kopalnię na kolejny poziom za: ");
                tvCost.setText("2000");

                HashMap<String,Double> coalPrice = mSQLiteHandler.getCoalBonusPrice();
                String coal_price = coalPrice.get("coal_price").toString();
                tvPrice.setText(coal_price);

                buttonLvlUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(money >= 2000) {
                            mSQLiteHandler.updateCoalBonus(1.3);
                            mSQLiteHandler.updateMoney(money-2000);
                            tvLvl.setText("3");
                            tvCost.setText("3500");
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś kopalnię do 3 poziomu!", Toast.LENGTH_SHORT).show();
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
                        double getCoal = coal*2.0;
                        mSQLiteHandler.updateMoney(money+(int) getCoal);
                        mSQLiteHandler.updateCoalHighScore(0);
                        Toast.makeText(getApplicationContext(), "Gratulacje! Sprzedałeś " + coal + " ton węgla!", Toast.LENGTH_SHORT).show();
                    }
                });

                final int widthFur =(int) (width * .7);
                final int heightFur =(int) (height * .2);
                popUp.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,widthFur, heightFur);


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


                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow,null);
                popUp = new PopupWindow(container,widthPop,heightPop,true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);
                buttonLvlUp = (ImageButton) popUp.getContentView().findViewById(R.id.lvlUpButton);

                tvMain.setText("Rurociąg jest na poziomie: ");
                tvLvl.setText("2");
                tvQue.setText("Czy chcesz ulepszyć rurociąg na kolejny poziom za: ");
                tvCost.setText("2000");

                buttonLvlUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(money >= 2000) {
                            mSQLiteHandler.updatePipePrice();
                            tvLvl.setText("3");
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPipeBonusPrice()));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś kopalnię do 3 poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur =(int) (width * .1);
                final int heightFur =(int) (height * .2);
                popUp.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,widthFur, heightFur);


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
}
