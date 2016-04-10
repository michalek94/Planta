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
import pl.planta.helper.SessionManager;

public class RegisterActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister, btnLinkToLoginScreen;
    private EditText inputNick, inputEmail, inputPassword;
    private CheckBox checkPassword;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private RegEx regEx;
    private InternetConnection internetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);

        inputNick = (EditText) findViewById(R.id.etNick);
        inputEmail = (EditText) findViewById(R.id.etMeil);
        inputPassword = (EditText) findViewById(R.id.etPassword);

        checkPassword = (CheckBox) findViewById(R.id.cbPassword);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        sessionManager = new SessionManager(getApplicationContext());

        regEx = new RegEx();

        internetConnection = new InternetConnection();

        // Sprawdź czy użytkownik jest już zalogowany.
        // Jeśli tak, przenieś go do MenuActivity
        if (sessionManager.isLoggedIn()) {
            Log.w(TAG, "Starting MenuActivity");
            // Użytkownik jest zalogowany. Przenoszę go do MenuActivity
            Intent menuIntent = new Intent(RegisterActivity.this, MenuActivity.class);
            startActivity(menuIntent);
            finish();
        }

        // Przycisk rejestracji
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputNick.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        if (regEx.checkEmail(email)) {
                            registerUser(name, email, password);
                        } else {
                            Toast.makeText(getApplicationContext(), "Niepoprawny adres e-mail.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Prosze wprowadzic dane.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Brak polaczenia z Internetem.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Przycisk przenoszący do ekranu logowania
        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void registerUser(final String name, final String email, final String password) {
        String tag_string_req = "req_register";

        progressDialog.setMessage("Rejestrowanie...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Method.POST, AppConfiguration.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Odpowiedz rejestracji: " + response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Uzytkownik " + name + " zarejestrowany pomyslnie", Toast.LENGTH_LONG).show();

                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    } else {
                        String errorMessage = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem rejestracji: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        AppController.getInstance(this).addToRequestQueue(stringRequest, tag_string_req);
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
        if (checkPassword.isChecked()) {
            inputPassword.setTransformationMethod(null);
        } else {
            inputPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
    }
}
