package pl.planta.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import pl.planta.R;
import pl.planta.dialogs.ExitDialog;
import pl.planta.helper.ChangeLog;
import pl.planta.helper.SQLiteHandler;
import pl.planta.helper.SessionManager;
import pl.planta.thread.Income;
import pl.planta.thread.Save;

public class MenuActivity extends Activity {

    private Button btnLogout;
    private Button btnGraj;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;
    private ChangeLog changeLog;
    private ExitDialog exitDialog;
    private FragmentManager fragmentManager = getFragmentManager();

    private Income income;
    private long refreshTime = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Set flags to make full screen on device
         */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        /**
         * Initialize button's
         */
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnGraj = (Button) findViewById(R.id.btnGraj);

        sessionManager = new SessionManager(getApplicationContext());
        sqLiteHandler = new SQLiteHandler(getApplicationContext());

        changeLog = new ChangeLog(this);
        exitDialog = new ExitDialog();

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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        sessionManager.setLogin(false);

        sqLiteHandler.deleteUsers();

        Intent chooseIntent = new Intent(MenuActivity.this, ChooseActivity.class);
        startActivity(chooseIntent);
        finish();
    }

    private void showConfirmExitDialog() {
        exitDialog.show(fragmentManager, "Sample Fragment");
    }

    /**
     * Po naciśnięciu przycisku Back pojawi się AlertDialog
     * informujący użytkownika czy zamierza wyjść z aplikacji czy też zostać.
     */
    @Override
    public void onBackPressed() {
        showConfirmExitDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (income == null)
            income = new Income(this, refreshTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        income.stop();
    }
}
