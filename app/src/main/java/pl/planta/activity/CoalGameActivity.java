package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import pl.planta.games.coalGame.GamePanel;

public class CoalGameActivity extends Activity {

    private static final String TAG = CoalGameActivity.class.getSimpleName();

    private GamePanel view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Wyłącz górny tytuł
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Przełącz na pełny ekran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        view = new GamePanel(this);
        setContentView(view);
    }
}
