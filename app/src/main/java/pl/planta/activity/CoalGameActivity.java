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
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;

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

        sessionManager = new SessionManager(this);
        sqLiteHandler = new SQLiteHandler(getApplicationContext());

        HashMap<String, Integer> scored = sqLiteHandler.getUserCoalScores();
        HashMap<String, String> user = sqLiteHandler.getUserDetails();

        String uid = user.get("uid");

        int score = scored.get("coal_score") + 15;
        int best = scored.get("coal_highscore") + 10;

        saveCoalScoresOnServer(uid, score);

        Log.d("Scores: ", "Score: " + String.valueOf(score) + "Best: " + String.valueOf(best));

    }

    private void saveCoalScoresOnServer(final String uid, final int score){
        String tag_string_req = "req_update_coal_scores";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_COAL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("coal_score", String.valueOf(score));

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
    }
}
