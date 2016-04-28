package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import pl.planta.R;



/**
 * Created by Jakub on 2016-04-27.
 */
 public class PopUp extends Activity {

    public PopupWindow popUp;
    public  WindowManager.LayoutParams params;
    public  TextView txtTest;

    public  void change(int x, int y, String txt)
    {
        params.x=x;
        params.y=y;
        txtTest.setText(txt);

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        txtTest = (TextView)findViewById(R.id.textViewPop);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        params = getWindow().getAttributes();

        getWindow().setLayout((int) (width * .3), (int) (height * .3));
        //getWindow().setGravity(Gravity.BOTTOM);

//
        params.x=50;
        params.y=50;
        getWindow().setAttributes(params);


    }
}
