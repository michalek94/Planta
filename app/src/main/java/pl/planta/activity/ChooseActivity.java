package pl.planta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import pl.planta.R;
import pl.planta.helper.SessionManager;

public class ChooseActivity extends Activity {

    private static final String TAG = ChooseActivity.class.getSimpleName();

    private Button btn_Login, btn_Register;
    private LoginButton btnFacebook;
    private SessionManager sessionManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Facebook SDK Initialize
         */
        FacebookSdk.sdkInitialize(getApplicationContext());
        /**
         * Set flags to make full screen on device
         */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**
         * Set screen's view (layout)
         */
        setContentView(R.layout.activity_choose);
        /**
         * Initialize instance of CallbackManager
         */
        callbackManager = CallbackManager.Factory.create();
        /**
         * Initialize instance of SessionManager
         */
        sessionManager = new SessionManager(getApplicationContext());
        /**
         * Initialize required buttons
         */
        btn_Login = (Button) findViewById(R.id.btn_Login);
        btn_Register = (Button) findViewById(R.id.btn_Register);
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        /**
         * Setting read permission for Facebook Login
         * public_profile - for id, name, first_name, last_name
         * email - for facebook's user email
         */
        btnFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnFacebook.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.w(TAG, "Login Success");
                        btnFacebook.setVisibility(View.INVISIBLE);
                        /**
                         * GraphRequest to get Facebook's user data like: name, email or id.
                         */
                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                JSONObject jsonObject = response.getJSONObject();
                                try {
                                    if (jsonObject != null) {
                                        int id = jsonObject.getInt("id");
                                        String name = jsonObject.getString("first_name");
                                        String email = jsonObject.getString("email");
                                        Log.d("ID: ", String.valueOf(id));
                                        Log.d("Name: ", name);
                                        Log.d("Email: ", email);
                                        sessionManager.saveFacebookCredentials(id, name, email);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, link, email");
                        request.setParameters(parameters);
                        request.executeAsync();

                        sessionManager.setLogin(true);
                        loginAfterFacebookLogin();
                    }

                    /**
                     * When Facebook Login was cancelled by user
                     */
                    @Override
                    public void onCancel() {
                        Log.w(TAG, "Login Cancelled");
                    }

                    /**
                     * When Facebook Login error occurred
                     * onError method, which handle the error, is called
                     * @param error error message
                     */
                    @Override
                    public void onError(FacebookException error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
        /**
         *
         */
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ChooseActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
        /**
         * Default register
         */
        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(ChooseActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
        /**
         * If user is already logged in, take him to MenuActivity
         */
        if (sessionManager.isLoggedIn()) {
            // Użytkownik jest zalogowany. Przenoszę go do MenuActivity
            Intent menuIntent = new Intent(ChooseActivity.this, MenuActivity.class);
            startActivity(menuIntent);
            finish();
        }
    }

    /**
     * Forward the login results to the callbackManager created in onCreate()
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loginAfterFacebookLogin(){
        Intent menuIntent = new Intent(ChooseActivity.this, MenuActivity.class);
        startActivity(menuIntent);
        finish();
    }
}
