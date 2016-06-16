package pl.planta.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.planta.R;
import pl.planta.app.AppConfiguration;
import pl.planta.app.AppController;
import pl.planta.helper.InternetConnection;
import pl.planta.helper.RegEx;
import pl.planta.helper.SQLiteHandler;
import pl.planta.helper.SessionManager;

public class LoginActivity extends Activity {

    private static String TAG = LoginActivity.class.getSimpleName();

    private Button btn_LogIn;
    private EditText inputEmail, inputPassword;
    private CheckBox cbPassword;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLiteHandler SQLiteHandler;
    private RegEx regEx;
    private InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btn_LogIn = (Button) findViewById(R.id.btn_LogIn);

        inputEmail = (EditText) findViewById(R.id.etMeil);
        inputPassword = (EditText) findViewById(R.id.etPassword);

        cbPassword = (CheckBox) findViewById(R.id.cbPassword);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sessionManager = new SessionManager(getApplicationContext());

        SQLiteHandler = new SQLiteHandler(getApplicationContext());

        regEx = new RegEx();

        internetConnection = new InternetConnection();

        if (sessionManager.isLoggedIn()) {
            // Użytkownik już jest zalogowany. Przenoszę go do MenuActivity
            Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(menuIntent);
            finish();
        }

        btn_LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Jeśli nie są puste pola, wywołaj metodę checkLogin(par1, par2);
                if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                    if (!email.isEmpty() && !password.isEmpty()) {
                        if (regEx.checkEmail(email)) {
                            // zaloguj użyytkownika
                            checkLogin(email, password);
                        } else {
                            Toast.makeText(getApplicationContext(), "Niepoprawny adres e-mail.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Poproś użytkownika o wpisanie danych
                        Toast.makeText(getApplicationContext(), "Prosze wprowadz dane!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Brak polaczenia z Internetem.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
    Metoda sprawdzająca pprawność danych wprowadzonych przez użytkownika
    Z danymi znajdującymi się w bazie MySQL
     */
    private void checkLogin(final String email, final String password) {
        // TAG używany do anulowania prośby
        String tagStringRequest = "req_login";

        progressDialog.setMessage("Logowanie...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Method.POST, AppConfiguration.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedź logowania: " + response);
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (!error) {
                        // Użytkownik pomyslnie zalogowany
                        // Utwórz jego sesję
                        sessionManager.setLogin(true);

                        //// Przechowaj jego dane w SQLite
                        String uid          = jsonObject.getString("uid");
                        JSONObject user     = jsonObject.getJSONObject("user");
                        String name         = user.getString("name");
                        String email        = user.getString("email");
                        int money           = user.getInt("money");
                        int level           = user.getInt("level");

                        int coalHighScore   = user.getInt("coal_highscore");
                        double coalBonus    = user.getDouble("coal_bonus");
                        double coalPrice    = user.getDouble("coal_price");

                        int pipeHighScore   = user.getInt("pipe_highscore");
                        double pipeBonus    = user.getDouble("pipe_bonus");
                        double pipePrice    = user.getDouble("pipe_price");

                        int computerLevel   = user.getInt("computer_level");
                        int hookLevel       = user.getInt("hook_level");
                        int storeroomLevel  = user.getInt("storeroom_level");
                        int furnaceLevel    = user.getInt("furnace_level");
                        int factory_level   = user.getInt("factory_level");
                        int flatsLevel      = user.getInt("flats_level");
                        int pipelineLevel   = user.getInt("pipeline_level");
                        int mineLevel       = user.getInt("mine_level");

                        int coalAmount      = user.getInt("coal_amount");
                        int coalMax         = user.getInt("coal_max");
                        int pipeAmount      = user.getInt("pipe_amount");
                        int pipeMax         = user.getInt("pipe_max");
                        int elecAmount      = user.getInt("elec_amount");
                        int elecMax         = user.getInt("elec_max");

                        String created_at   = user.getString("created_at");

                        SQLiteHandler.addUser(uid, name, email, money, level, created_at);
                        SQLiteHandler.addCoal(coalHighScore, coalBonus, coalPrice);
                        SQLiteHandler.addPipe(pipeHighScore, pipeBonus, pipePrice);
                        SQLiteHandler.addLevels(computerLevel, hookLevel, storeroomLevel, furnaceLevel, factory_level, flatsLevel, pipelineLevel, mineLevel);
                        SQLiteHandler.addStoreValues(coalAmount, coalMax, pipeAmount, pipeMax, elecAmount, elecMax);

                        Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                        finish();
                    } else {
                        String errorMessage = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Blad JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Blad logowania: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tagStringRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /*
    Funkcja służąca do pokazywania hasła, jeśli użytkownik zaznaczy Checkbox'a
     */
    public void showPassword(View view) {
        if (cbPassword.isChecked()) {
            inputPassword.setTransformationMethod(null);
        } else {
            inputPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}