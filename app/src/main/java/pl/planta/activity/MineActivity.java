package pl.planta.activity;

import android.app.Activity;
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
    private SQLiteHandler mSQLiteHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mine);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mSQLiteHandler = new SQLiteHandler(getApplicationContext());

        final int width = dm.widthPixels;
        final int height = dm.heightPixels;

        relativeLayout = (android.support.percent.PercentRelativeLayout) findViewById(R.id.mineLayout);

        buttonEntrance = (ImageButton)findViewById(R.id.entranceButton);
        buttonPipeline = (ImageButton)findViewById(R.id.pipelineButton);

        buttonEntrance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View container = layoutInflater.inflate(R.layout.popcoalwindow,null);
                popUp = new PopupWindow(container,550,600,true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);
                tvPrice = (TextView) popUp.getContentView().findViewById(R.id.textViewPopSellPrice);

                tvMain.setText("Kopalnia jest na poziomie: ");
                tvLvl.setText("2");
                tvQue.setText("Czy chcesz ulepszyć kopalnię na kolejny poziom za: ");
                tvCost.setText("2000");

                HashMap<String,Double> coalPrice = mSQLiteHandler.getCoalPrice();
                String coal_price = coalPrice.get("coal_price").toString();
                tvPrice.setText(coal_price);

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
                popUp = new PopupWindow(container,550,500,true);

                tvMain = (TextView) popUp.getContentView().findViewById(R.id.textViewPopMain);
                tvLvl = (TextView) popUp.getContentView().findViewById(R.id.textViewPopLvl);
                tvQue = (TextView) popUp.getContentView().findViewById(R.id.textViewPopQue);
                tvCost = (TextView) popUp.getContentView().findViewById(R.id.textViewPopCost);

                tvMain.setText("Rurociąg jest na poziomie: ");
                tvLvl.setText("2");
                tvQue.setText("Czy chcesz ulepszyć rurociąg na kolejny poziom za: ");
                tvCost.setText("2000");

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
