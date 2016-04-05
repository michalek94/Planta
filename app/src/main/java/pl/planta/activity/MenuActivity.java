package pl.planta.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import pl.planta.planta.R;
import pl.planta.helper.ChangeLog;
import pl.planta.helper.SQLiteHandler;
import pl.planta.helper.SessionManager;

public class MenuActivity extends Activity {

    private Button btnLogout, btnGraj, btnMenuTesting, btnPositive, btnNegative;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;
    private LayoutInflater layoutInflater;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnGraj = (Button)findViewById(R.id.btnGraj);
        btnMenuTesting = (Button)findViewById(R.id.btnUstawienia);

        sessionManager = new SessionManager(getApplicationContext());
        sqLiteHandler = new SQLiteHandler(getApplicationContext());

        ChangeLog changeLog = new ChangeLog(this);
        if (changeLog.firstRun()){
            changeLog.getLogDialog().show();
        }

        if(!sessionManager.isLoggedIn()){
            logoutUser();
        }

        btnGraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(MenuActivity.this, CoalGameActivity.class);
                startActivity(gameIntent);
            }
        });

        btnMenuTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gamingTests = new Intent(MenuActivity.this, GamingTestsActivity.class);
                startActivity(gamingTests);
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

        Intent loginIntent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "onResume() method used", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause() method used", Toast.LENGTH_LONG).show();
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
}
