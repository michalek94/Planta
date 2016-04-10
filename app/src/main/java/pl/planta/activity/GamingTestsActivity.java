package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import pl.planta.R;

public class GamingTestsActivity extends Activity {

    private Button btnSecondGame, btnThirdGame, btnMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gamingtests);

        btnSecondGame = (Button) findViewById(R.id.btnSecondGame);
        btnThirdGame = (Button) findViewById(R.id.btnThirdGame);
        btnMainMenu = (Button) findViewById(R.id.btnMainManu);

        btnSecondGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondGame = new Intent(GamingTestsActivity.this, SecondGameActivity.class);
                startActivity(secondGame);
            }
        });
        btnThirdGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thirdGame = new Intent(GamingTestsActivity.this, ThirdGameActivity.class);
                startActivity(thirdGame);
            }
        });
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenu = new Intent(GamingTestsActivity.this, CoalGameActivity.class);
                startActivity(mainMenu);
            }
        });
    }
}
