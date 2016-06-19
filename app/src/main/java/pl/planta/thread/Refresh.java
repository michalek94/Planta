package pl.planta.thread;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;


import java.util.HashMap;

import pl.planta.R;
import pl.planta.activity.LoginActivity;
import pl.planta.helper.SQLiteHandler;
import pl.planta.helper.SessionManager;

public class Refresh {
    private static String TAG = LoginActivity.class.getSimpleName();

    private SessionManager sessionManager;

    private Context mContext;
    private TextView mCoal;
    private TextView mWater;
    private TextView mElectricity;
    private TextView mMoney;

    private SQLiteHandler mSQLiteHandler;
    private final Handler handler = new Handler();

    private HashMap<String,Integer> userAmounts;

    private int coalAmount;
    private int waterAmount;
    private int electricityAmount;
    private int moneyAmount;

    private long time;


    public Refresh(Activity activity, long time) {
        this.mContext = activity;
        this.time = time;

        sessionManager = new SessionManager(mContext);
        mSQLiteHandler = new SQLiteHandler(mContext);
        //Getting amounts from sqlite database
        userAmounts = mSQLiteHandler.getAmounts();
        //Getting activity components-----------------------
        mCoal = (TextView) ((Activity)mContext).findViewById(R.id.coalTextView1);
        mWater = (TextView) ((Activity)mContext).findViewById(R.id.waterTextView1);
        mElectricity = (TextView) ((Activity)mContext).findViewById(R.id.electricityTextView1);
        mMoney = (TextView) ((Activity)mContext).findViewById(R.id.cashTextView1);
        //--------------------------------------------------------
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, time);
    }


    public void updateTextView(){
        coalAmount =  mSQLiteHandler.getAmounts().get("coal_amount");
        mCoal.setText(""+coalAmount);
        waterAmount =  mSQLiteHandler.getAmounts().get("pipe_amount");
        mWater.setText(""+waterAmount);
        electricityAmount =  mSQLiteHandler.getAmounts().get("electricity_amount");
        mElectricity.setText(""+electricityAmount);
        moneyAmount = mSQLiteHandler.getUserMoney().get("money");
        mMoney.setText(""+moneyAmount);
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
        System.out.println("Niszcze watek refresh");
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
                try {
                    System.out.println("Wątek refresh: odswiezam textView.");
                    updateTextView();
                } catch (Exception e) {
                    System.out.println("Blad:" + e);
                } finally {
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, time);
                }
            }
    };
}
