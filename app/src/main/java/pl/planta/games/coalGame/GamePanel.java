package pl.planta.games.coalGame;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.planta.R;
import pl.planta.app.AppConfiguration;
import pl.planta.app.AppController;
import pl.planta.helper.SQLiteHandler;
import pl.planta.helper.SessionManager;
import pl.planta.painter.Background;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener
{

    private static final String TAG = GamePanel.class.getSimpleName();
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    private long coalStartTime;
    private MainThread thread;
    private Background bg;
    private BitmapFactory.Options a;
    private Player player;
    private ArrayList<Coal> coals;
    private Random rand = new Random();
    private boolean newGameCreated;


    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;
    private int counter;
    private int counter2;
    private int uncatched=3;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private boolean end=false;

    SQLiteHandler sqliteHandler;
    HashMap<String, Integer> handler;
    Context mContext;

    public GamePanel(Context mContext)
    {
        super(mContext);
        this.mContext = mContext;
        sqliteHandler = new SQLiteHandler(mContext);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        handler = sqliteHandler.getCoalHighScore();
        best =  handler.get("coal_highscore");

        a = new BitmapFactory.Options();
        a.inScaled=false;
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.tlo,a));
        player = new Player(mContext,BitmapFactory.decodeResource(getResources(), R.drawable.wuzek,a),340,244,1);
        coals = new ArrayList<>();
        coalStartTime = System.nanoTime();
        thread = new MainThread(getHolder(), this);
        senSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if (!player.getPlaying() && newGameCreated && reset){
                player.setPlaying(true);
            }
            if(player.getPlaying()){
                if(!started)started=true;
                reset=false;
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update()
    {
        if(player.getPlaying()) {
            player.update();
            long coalElapsed = (System.nanoTime()- coalStartTime)/1000000;
            if(coalElapsed>(2000-player.getDifficulty()/4)) {
                coals.add(new Coal(BitmapFactory.decodeResource(getResources(), R.drawable.wungiel, a),
                        (int)(rand.nextDouble()* (WIDTH- (40*2))+40), -100 , 100, 100, player.getDifficulty(), 4));
                coalStartTime = System.nanoTime();
            }
            //loop through every coal and check collision and remove
            for(int i =0;i<coals.size();i++){
                coals.get(i).update();
                if(collision(coals.get(i),player)&&coals.get(i).getY()<470){
                    if(!coals.get(i).areCatched) {
                        coals.get(i).areCatched = true;
                        counter++;

                    }
                }
                if(coals.get(i).areCatched&&coals.get(i).getY()>500)
                {
                    coals.remove(i);
                    break;
                }
                if (coals.get(i).getY()>800) {
                    uncatched--;
                    coals.remove(i);
                    break;
                }
                if(uncatched<=-1){
                    player.setPlaying(false);
                    end=true;
                    break;
                }
            }
        } else{
            player.resetDX();
            if(!reset)
            {
                newGameCreated=false;
                startReset=System.nanoTime();
                reset = true;
                dissapear = true;
            }
            //long resetElapsed = (System.nanoTime()-startReset)/1000000;
            if(/*resetElapsed>500 &&*/ !newGameCreated)
            {
                newGame();
            }

        }
    }

    public boolean collision(GameObject a, GameObject b){
        return Rect.intersects(a.getRectangle(), b.getRectangle());
    }

    public void myDraw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        if (canvas != null) {
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            for (Coal c : coals) {
                c.draw(canvas, player.getX());
            }
            if(!dissapear) {
                player.draw(canvas);
            }
            drawText(canvas);
            if(end)
            {
                drawEnd(canvas);
            }
        }
    }

    public void newGame() {
        HashMap<String, String> userUID = sqliteHandler.getUserUid();
        String uid = userUID.get("uid");
        dissapear = false;
        coals.clear();
        player.resetDX();
        if(counter>best){
            best =counter;
            sqliteHandler.updateCoalHighScore(best);
            saveCoalHighscoreOnServer(uid, best);
        }
        counter2 = counter;
        counter=0;

        uncatched=3;
        player.resetScore();
        player.setX(WIDTH / 2 - 250);

        newGameCreated = true;
    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Wynik: " + counter, 10, HEIGHT - 10, paint);
        canvas.drawText("Najlepszy wynik: " + best, WIDTH - 290, HEIGHT - 10, paint);
        canvas.drawText("Dostępne próby: " + uncatched, WIDTH /2-150, HEIGHT - 10, paint);
        if(!player.getPlaying()&&newGameCreated&&reset&&!end)
        {
            Paint paint1 = new Paint();
            paint1.setColor(Color.BLACK);
            paint1.setTextSize(80);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ramka, a), 270, 200, null);
            canvas.drawText("Wciśnij by zacząć", WIDTH / 2 - 310, HEIGHT / 2 - 100, paint1);
            paint1.setTextSize(40);
            canvas. drawText("Aby sterować wózkiem poruszaj", WIDTH / 2 - 310, HEIGHT / 2 - 30, paint1);
            canvas. drawText("telefonem w lewo i w prawo", WIDTH / 2 - 310, HEIGHT / 2 + 30, paint1);
        }
    }

    public void drawEnd(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        if (!player.getPlaying() && newGameCreated && reset && end) {
            Paint paint1 = new Paint();
            paint1.setColor(Color.BLACK);
            paint1.setTextSize(80);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ramka, a), 270, 200, null);
            canvas.drawText("Twój wynik: " + counter2, WIDTH / 2 - 310, HEIGHT / 2 - 50, paint1);
            canvas.drawText("Najlepszy wynik: " + best, WIDTH / 2 - 310, HEIGHT / 2 + 50, paint1);
        }
    }

    @Override
    public void onSensorChanged( SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if(mySensor.getType() == 1) {
            float y = sensorEvent.values[1];
            player.move(y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void saveCoalHighscoreOnServer(final String uid, final int highscore) {
        String tag_string_req = "req_update_coal_highscore";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_COAL_HIGHSCORE_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("coal_highscore", String.valueOf(highscore));

                return params;
            }
        };
        AppController.getInstance(mContext).addToRequestQueue(stringRequest, tag_string_req);
    }
}
