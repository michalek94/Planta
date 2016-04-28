package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

import pl.planta.activity.PopUp;


import pl.planta.R;

public class PlantaActivity extends Activity  {


    ImageButton but;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_planta);

        but = (ImageButton)findViewById(R.id.furnaceButton);

        but.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                //PopUp.change(20,20,"test");

                startActivity(new Intent(PlantaActivity.this, PopUp.class));

            }
        });



    }
}
