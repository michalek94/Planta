package pl.planta.games.dragAndDropGame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.ThemedSpinnerAdapter;
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
import pl.planta.painter.Background;


public class DragAndDropPanel extends SurfaceView implements SurfaceHolder.Callback
{
    private static final String TAG = DragAndDropPanel.class.getSimpleName();
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    private float scaleFactorX;
    private float scaleFactorY;
    private DragAndDropThread thread;
    private Background bg;
    private BitmapFactory.Options a;
    private Board myBoard;
    private Random rand = new Random();
    private boolean canMove=false;
    private boolean isTrue=false;
    private long startTime;
    private int endTime;
    private int bestTime;
    private boolean firstGame;

    SQLiteHandler sqliteHandler;
    HashMap<String, Integer> handler;
    Context mContext;

    public DragAndDropPanel(Context mContext)
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
    firstGame = true;
        handler = sqliteHandler.getPipeScore();
        bestTime =  handler.get("pipe_score");
        a = new BitmapFactory.Options();
        a.inScaled=false;
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.rury,a));
        myBoard = new Board(0,0,WIDTH,HEIGHT,getContext());
        startTime=System.nanoTime();
        thread = new DragAndDropThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

    }


    boolean firstTouch = false;
    long time=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (firstGame){
            int positionX = (int) (event.getX() / scaleFactorX);
        int positionY = (int) (event.getY() / scaleFactorY);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (myBoard.fitPipe()) {
                if (myBoard.check()) {
                    System.out.println("GRA UKONCZONA");
                    endTime = (int) ((System.nanoTime() - startTime) / 1000000000);
                    firstGame = false;
                    if (bestTime > endTime && firstGame) {
                        //  firstGame=false;
                        myBoard.setENDED(true);
                        HashMap<String, String> userUID = sqliteHandler.getUserUid();
                        String uid = userUID.get("uid");
                        bestTime = endTime;
                        sqliteHandler.updatePipeScore(1, bestTime);
                        savePipeScoreOnServer(uid, bestTime);
                    }
                    isTrue = true;
                } else {
                    isTrue = false;
                }
            }
            canMove = false;
            return true;
        }
        if (canMove) {
            myBoard.movePipes(positionX, positionY);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN && myBoard.isPipeClicked(positionX, positionY)) {
            if (firstTouch && (System.currentTimeMillis() - time) <= 300 && myBoard.getPipes().peek().getPipeArea().contains(positionX, positionY)) {
                myBoard.peekPipe(positionX, positionY);
                firstTouch = false;
                myBoard.rotatePipe();
            } else {
                myBoard.peekPipe(positionX, positionY);
                firstTouch = true;
                time = System.currentTimeMillis();
                canMove = true;
                return true;
            }
            return true;
        }
    }
        return super.onTouchEvent(event);

    }

    public void update()
    {

    }

    public void myDraw(Canvas canvas) {
        scaleFactorX = getWidth() / (WIDTH * 1.f);
        scaleFactorY = getHeight() / (HEIGHT * 1.f);
        if (canvas != null) {
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            myBoard.draw(canvas);
            drawText(canvas);
            if(isTrue)
                drawEnd(canvas);
        }
    }

    public void newGame() {
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Najlepszy czas: " + bestTime, WIDTH - 250,  25, paint);


    }

    public void drawEnd(Canvas canvas) {
            Paint paint1 = new Paint();
            paint1.setColor(Color.BLACK);
            paint1.setTextSize(70);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ramka, a), 50, 0, null);
        canvas.drawText("Ukończyłeś poziom!", WIDTH / 2 - 300, HEIGHT / 2 -80 , paint1);
            canvas.drawText("Obecny czas: "+endTime, WIDTH / 2 - 280, HEIGHT / 2+70 , paint1);
                canvas.drawText("Najlepszy czas: " + bestTime, WIDTH / 2 - 280,   HEIGHT / 2+150, paint1);

    }

    private void savePipeScoreOnServer(final String uid, final int score) {
        String tag_string_req = "req_update_pipe_score";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_PIPE_UPDATE, new Response.Listener<String>() {
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
                params.put("pipe_score", String.valueOf(score));

                return params;
            }
        };
        AppController.getInstance(mContext).addToRequestQueue(stringRequest, tag_string_req);
    }





}