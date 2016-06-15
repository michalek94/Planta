package pl.planta.thread;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import java.util.HashMap;

import pl.planta.R;
import pl.planta.helper.SQLiteHandler;

public class Refresh {
    private Context mContext;
    private TextView mCoal;

    private SQLiteHandler mSQLiteHandler;
    private final Handler handler = new Handler();

    private HashMap<String,Integer> userCoal;

    private long time;


    public Refresh(Activity activity, long time) {
        this.mContext = activity;
        this.time = time;
        mSQLiteHandler = new SQLiteHandler(mContext);
        userCoal = mSQLiteHandler.getCoalHighScore();
        mCoal = (TextView) ((Activity)mContext).findViewById(R.id.coalTextView1);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, time);
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
                try {
                    System.out.println("Pobieram.");
                    String coal = userCoal.get("coal_highscore").toString();
                    mCoal.setText(coal);
                } catch (Exception e) {
                    System.out.println("Blad:" + e);
                } finally {
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, time);
                }
            }
    };
}
