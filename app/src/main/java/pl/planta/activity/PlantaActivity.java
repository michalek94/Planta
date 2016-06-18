package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


import pl.planta.R;
import pl.planta.helper.SQLiteHandler;

public class PlantaActivity extends Activity  {


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

        mSQLiteHandler = new SQLiteHandler(getApplicationContext());


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int width = dm.widthPixels;
        final int height = dm.heightPixels;
        final int widthPop =(int) (width * .35);
        final int heightPop = (int) (height * .55);

        relativeLayout = (android.support.percent.PercentRelativeLayout) findViewById(R.id.plantaLayout);

        buttonFurnace = (ImageButton)findViewById(R.id.furnaceButton);
        buttonHook = (ImageButton)findViewById(R.id.hookButton);
        buttonComputer = (ImageButton)findViewById(R.id.computerButton);
        buttonDoor = (ImageButton)findViewById(R.id.doorButton);

        buttonFurnace.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                columnNr = 4;

                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow,null);
                popUp = new PopupWindow(container,widthPop,heightPop,true);

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

                        if(mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);
                            mSQLiteHandler.updateLevels(columnNr);
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś piec do " +String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur =(int) (width * .7);
                final int heightFur =(int) (height * .35);
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

        buttonHook.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                columnNr = 2;

                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow,null);
                popUp = new PopupWindow(container,widthPop,heightPop,true);

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

                        if(mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);
                            mSQLiteHandler.updateLevels(columnNr);
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś dźwig do " +String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur =(int) (width * .4);
                final int heightFur =(int) (height * .05);
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

        buttonComputer.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                columnNr = 1;

                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow,null);
                popUp = new PopupWindow(container,widthPop,heightPop,true);

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

                        if(mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);
                            mSQLiteHandler.updateLevels(columnNr);
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś panel sterowania do " +String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur =(int) (width * .10);
                final int heightFur =(int) (height * .3);
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

        buttonDoor.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                columnNr = 3;

                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popwindow,null);
                popUp = new PopupWindow(container,widthPop,heightPop,true);

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

                        if(mSQLiteHandler.checkMoney(mSQLiteHandler.getPrice(columnNr))) {
                            mSQLiteHandler.subMoney(mSQLiteHandler.getPrice(columnNr));
                            mSQLiteHandler.updatePrice(columnNr);
                            mSQLiteHandler.updateLevels(columnNr);
                            tvCost.setText(String.valueOf(mSQLiteHandler.getPrice(columnNr)));
                            tvLvl.setText(String.valueOf(mSQLiteHandler.getLevels(columnNr)));
                            Toast.makeText(getApplicationContext(), "Gratulacje! Powiekszyłeś panel sterowania do " +String.valueOf(mSQLiteHandler.getLevels(columnNr)) + " poziomu!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Masz za mało środków na koncie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final int widthFur =(int) (width * .45);
                final int heightFur =(int) (height * .35);
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
