package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import pl.planta.R;

/**
 * Created by Jakub on 2016-04-12.
 */
public class MineActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mine);
    }
}
