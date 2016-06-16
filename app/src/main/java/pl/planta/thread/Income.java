package pl.planta.thread;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import java.util.HashMap;

import pl.planta.R;
import pl.planta.activity.LoginActivity;
import pl.planta.helper.SQLiteHandler;
import pl.planta.helper.SessionManager;

public class Income {
    private static String TAG = LoginActivity.class.getSimpleName();

    private SessionManager sessionManager;

    private Context mContext;

    private SQLiteHandler mSQLiteHandler;
    private final Handler handler = new Handler();

    private HashMap<String,Integer> userAmounts;
    private HashMap<String,Integer> userIncome;

    private int coalAmount;
    private int coalIncome;
    private int maximumAmount;

    private long time;


    public Income(Activity activity, long time) {
        this.mContext = activity;
        this.time = time;

        sessionManager = new SessionManager(mContext);
        mSQLiteHandler = new SQLiteHandler(mContext);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, time);
    }

    public void income(){
        coalAmount =mSQLiteHandler.getAmounts().get("coal_amount");
        coalIncome = mSQLiteHandler.getBuildingsLevels().get("mine_level") * 50;
        maximumAmount = mSQLiteHandler.getBuildingsLevels().get("storeroom_level")*1000;
        if(coalAmount+coalIncome<maximumAmount) {
            mSQLiteHandler.updateCoalAmount(coalAmount + coalIncome);
        }else {
            mSQLiteHandler.updateCoalAmount(maximumAmount);
        }
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
        System.out.println("Niszcze watek income");;
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                System.out.println("Wątek Income: Zwiekszam wartosci.");
                income();
            } catch (Exception e) {
                System.out.println("Blad:" + e);
            } finally {
                //also call the same runnable to call it at regular interval
                handler.postDelayed(this, time);
            }
        }
    };
}

