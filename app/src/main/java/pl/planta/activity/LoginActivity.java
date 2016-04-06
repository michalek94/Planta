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

    private Button btnLogin, btnLinkToRegisterScreen;
    private EditText inputEmail, inputPassword;
    private CheckBox cbPassword;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;
    private RegEx regEx;
    private InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLinkToRegisterScreen = (Button)findViewById(R.id.btnLinkToRegisterScreen);

        inputEmail = (EditText)findViewById(R.id.etMeil);
        inputPassword = (EditText)findViewById(R.id.etPassword);

        cbPassword = (CheckBox)findViewById(R.id.cbPassword);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sessionManager = new SessionManager(getApplicationContext());

        sqLiteHandler = new SQLiteHandler(getApplicationContext());

        regEx = new RegEx();

        internetConnection = new InternetConnection();

        if(sessionManager.isLoggedIn()){
            // Użytkownik już jest zalogowany. Przenoszę go do MenuActivity
            Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(menuIntent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Jeśli nie są puste pola, wywołaj metodę checkLogin(par1, par2);
                if(internetConnection.isNetworkAvailable(getApplicationContext())){
                    if (!email.isEmpty() && !password.isEmpty()) {
                        if(regEx.checkEmail(email)) {
                            // zaloguj użyytkownika
                            checkLogin(email, password);
                        }else{
                            Toast.makeText(getApplicationContext(), "Niepoprawny adres e-mail.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Poproś użytkownika o wpisanie danych
                        Toast.makeText(getApplicationContext(), "Prosze wprowadz dane!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Brak polaczenia z Internetem.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Przeniesienie do ekranu Rejestracji
        btnLinkToRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
                finish();
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
            public void onResponse(String response){
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
                        String uid = jsonObject.getString("uid");
                        JSONObject user = jsonObject.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        int coal_score = user.getInt("coal_score");
                        int coal_highscore = user.getInt("coal_highscore");
                        String created_at = user.getString("created_at");

                        sqLiteHandler.addUser(uid, name, email, coal_score, coal_highscore, created_at);

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
        }){
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
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    private void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    /*
    Funkcja służąca do pokazywania hasła, jeśli użytkownik zaznaczy Checkbox'a
     */
    public void showPassword(View view){
        if(cbPassword.isChecked()){
            inputPassword.setTransformationMethod(null);
        }else{
            inputPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
    }
}
