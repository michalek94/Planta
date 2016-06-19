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
    private static String TAG = Income.class.getSimpleName();

    private SessionManager sessionManager;

    private Context mContext;

    private SQLiteHandler mSQLiteHandler;
    private final Handler handler = new Handler();

    private HashMap<String,Integer> userAmounts;
    private HashMap<String,Integer> userIncome;

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
        int maximumAmount = mSQLiteHandler.getBuildingsLevels().get("storeroom_level") * 1000;
        int coalAmount = mSQLiteHandler.getAmounts().get("coal_amount");
        double coalBonusIncome = mSQLiteHandler.getCoalBonusPrice().get("coal_income_bonus");
        int coalIncome = (int) (((mSQLiteHandler.getBuildingsLevels().get("mine_level") - 1) * 10) * coalBonusIncome);
        int waterAmount = mSQLiteHandler.getAmounts().get("pipe_amount");
        double waterBonusIncome = mSQLiteHandler.getPipeBonusPrice().get("pipe_income_bonus");
        int waterIncome = (int) (((mSQLiteHandler.getBuildingsLevels().get("pipeline_level") - 1) * 10) * waterBonusIncome);
        int electricityAmount = mSQLiteHandler.getAmounts().get("electricity_amount");
        int electricityIncome = mSQLiteHandler.getBuildingsLevels().get("computer_level") * 10;
        int coalSub = 10 - (mSQLiteHandler.getBuildingsLevels().get("hook_level") - 1) * 2;
        int waterSub = 10 - (mSQLiteHandler.getBuildingsLevels().get("furnace_level") - 1) * 2;
        int moneyAmount = mSQLiteHandler.getUserMoney().get("money");
        int moneyIncome = (mSQLiteHandler.getBuildingsLevels().get("factory_level") + mSQLiteHandler.getBuildingsLevels().get("flats_level")) * 25;
        int electricitySub = mSQLiteHandler.getBuildingsLevels().get("factory_level") * 10;

        if(coalAmount + coalIncome < maximumAmount) {
            mSQLiteHandler.updateCoalAmount(coalAmount + coalIncome);
        }else {
            mSQLiteHandler.updateCoalAmount(maximumAmount);
        }
        if(waterAmount + waterIncome < maximumAmount) {
            mSQLiteHandler.updatePipeAmount(waterAmount + waterIncome);
        }else {
            mSQLiteHandler.updatePipeAmount(maximumAmount);
        }
        if(electricityAmount + electricityIncome < maximumAmount){
            if(coalAmount >= coalSub && waterAmount >= waterSub){
                mSQLiteHandler.updateElectricityAmount(electricityAmount + electricityIncome);
                mSQLiteHandler.updateCoalAmount(coalAmount - coalSub);
                mSQLiteHandler.updatePipeAmount(waterAmount - waterSub);
            }
        }
        else{
            mSQLiteHandler.updateElectricityAmount(maximumAmount);
        }
        if(electricityAmount >= electricitySub){
            mSQLiteHandler.updateMoney(moneyAmount + moneyIncome);
            mSQLiteHandler.updateElectricityAmount(electricityAmount - electricitySub);
        }
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
        System.out.println("Niszcze watek income");
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

