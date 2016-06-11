package pl.planta.games.thirdGame;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import pl.planta.R;


public class MapGamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    public static final int WIDTH = 3000;
    public static final int HEIGHT = 2999;
    private float scaleFactorX;
    private float scaleFactorY;
    private MapGameThread thread;
    private Map map;
    private Ball ball;
    private BitmapFactory.Options a;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;


    Context mContext;

    public MapGamePanel(Context mContext)
    {
        super(mContext);
        this.mContext = mContext;
        getHolder().addCallback(this);
        setFocusable(true);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        a = new BitmapFactory.Options();
        a.inScaled=false;
        map = new Map(BitmapFactory.decodeResource(getResources(), R.drawable.map, a),getWidth(),getHeight());
        ball = new Ball(mContext,getWidth()/2,getHeight()/2,30,30);
        senSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        thread = new MapGameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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

    public void update() {
        map.update();
    }

    public void myDraw(Canvas canvas) {
        //scaleFactorX = (getWidth()/(WIDTH * 1.f));
        //scaleFactorY = (getHeight()/(HEIGHT * 1.f));
        if (canvas != null) {
          //  canvas.scale(scaleFactorX, scaleFactorY);
            map.draw(canvas);
            ball.draw(canvas);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType() == 1) {
            float y = event.values[0];
            float x = event.values[1];
           // float z = event.values[2];
            map.move(x,y);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
