package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import pl.planta.R;
import pl.planta.helper.SQLiteHandler;

public class CityActivity extends Activity {

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
        final int widthPop =(int) (width * .35);
        final int heightPop = (int) (height * .55);

        relativeLayout = (android.support.percent.PercentRelativeLayout) findViewById(R.id.cityLayout);

        buttonResidence = (ImageButton)findViewById(R.id.residenceButton);
        buttonIndustry = (ImageButton)findViewById(R.id.industryButton);
        buttonShop = (ImageButton) findViewById(R.id.shopButton);

        buttonResidence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                columnNr = 6;
                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow,null);
                popUp = new PopupWindow(container,widthPop,heightPop,true);

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

                        if(mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);
                            mSQLiteHandler.updateLevels(columnNr);
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś mieszkania do " +String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
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

        buttonIndustry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                columnNr = 5;
                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow,null);
                popUp = new PopupWindow(container,widthPop,heightPop,true);

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

                        if(mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);
                            mSQLiteHandler.updateLevels(columnNr);
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś fabryki do " +String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
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

        buttonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });


    }
}
