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

import pl.planta.helper.SessionManager;
import pl.planta.R;

public class ChooseActivity extends Activity {

    private static final String TAG = ChooseActivity.class.getSimpleName();

    private Button btnRegister;
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
         * Initialize instance of CallbackManager
         */
        callbackManager = CallbackManager.Factory.create();
        /**
         * Set flags to make full screen on device
         */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**
         * Set screen's view (layout)
         */
        setContentView(R.layout.activity_choose);

        btnFacebook = (LoginButton)findViewById(R.id.btnFacebook);
        btnFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.w(TAG, "Login Success");
                sessionManager.setLogin(true);

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();
                        try{
                            if(jsonObject != null){
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                Log.d("ID: ", String.valueOf(id));
                                Log.d("Name: ", name);
                                Log.d("Email: ", email);
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, link, email");
                request.setParameters(parameters);
                request.executeAsync();


                Intent menuIntent = new Intent(ChooseActivity.this, MenuActivity.class);
                startActivity(menuIntent);
                finish();
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sessionManager = new SessionManager(getApplicationContext());
        // Sprawdź czy użytkownik jest już zalogowany.
        // Jeśli tak, przenieś go do MenuActivity
        if(sessionManager.isLoggedIn()){
            // Użytkownik jest zalogowany. Przenoszę go do MenuActivity
            Intent menuIntent = new Intent(ChooseActivity.this, MenuActivity.class);
            startActivity(menuIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
