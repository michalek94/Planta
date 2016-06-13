package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pl.planta.app.AppConfiguration;
import pl.planta.app.AppController;
import pl.planta.games.coalGame.GamePanel;
import pl.planta.helper.SQLiteHandler;
import pl.planta.helper.SessionManager;

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
