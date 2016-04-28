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

public class PlantaActivity extends Activity  {


    private ImageButton but;
    private PopupWindow popUp;
    private LayoutInflater layoutInflater;
    private android.support.percent.PercentRelativeLayout relativeLayout;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_planta);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        final int width = dm.widthPixels;
        final int height = dm.heightPixels;

        relativeLayout = (android.support.percent.PercentRelativeLayout) findViewById(R.id.plantaLayout);

        but = (ImageButton)findViewById(R.id.furnaceButton);

        but.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {


                layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popwindow,null);
                popUp = new PopupWindow(container,400,400,true);
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
    }
}
