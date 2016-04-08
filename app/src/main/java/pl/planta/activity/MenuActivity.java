package pl.planta.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import pl.planta.R;
import pl.planta.helper.ChangeLog;
import pl.planta.helper.SQLiteHandler;
import pl.planta.helper.SessionManager;

public class MenuActivity extends Activity {

    private Button btnLogout, btnGraj, btnMenuTesting, btnPositive, btnNegative;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;
    private LayoutInflater layoutInflater;
    private View view;
    private CallbackManager callbackManager;
    private LoginButton btnFacebook;

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
        setContentView(R.layout.activity_menu);

        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnGraj = (Button) findViewById(R.id.btnGraj);
        btnMenuTesting = (Button) findViewById(R.id.btnUstawienia);

        sessionManager = new SessionManager(getApplicationContext());
        sqLiteHandler = new SQLiteHandler(getApplicationContext());

        ChangeLog changeLog = new ChangeLog(this);
        if (changeLog.firstRun()) {
            changeLog.getLogDialog().show();
        }

        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }

        btnGraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MenuActivity.this, MainMenuActivity.class);
                startActivity(gameIntent);
            }
        });

        btnMenuTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gamingTests = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(gamingTests);
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setLogin(false);
                LoginManager.getInstance().logOut();
                Intent chooseIntent = new Intent(MenuActivity.this, ChooseActivity.class);
                startActivity(chooseIntent);
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

    }

    private void logoutUser(){
        sessionManager.setLogin(false);

        sqLiteHandler.deleteUsers();

        Intent chooseIntent = new Intent(MenuActivity.this, ChooseActivity.class);
        startActivity(chooseIntent);
        finish();
    }

    private void showConfirmExitDialog(){
        layoutInflater = getLayoutInflater();
        view = layoutInflater.inflate(R.layout.exit_alertdialog, null);

        btnPositive = (Button) view.findViewById(R.id.btnPositive);
        btnNegative = (Button) view.findViewById(R.id.btnNegative);

        // Use the Builder class for convenient dialog construction
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setView(view);
        builder.setCancelable(false);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                finish();
            }
        });
        builder.show();
    }

    /**
     * Po naciśnięciu przycisku Back pojawi się AlertDialog
     * informujący użytkownika czy zamierza wyjść z aplikacji czy też zostać.
     */
    @Override
    public void onBackPressed() {
        showConfirmExitDialog();
    }
    /**
     * Forward the login results to the callbackManager created in onCreate()
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
