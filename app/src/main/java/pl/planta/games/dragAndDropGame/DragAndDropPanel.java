package pl.planta.games.dragAndDropGame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import pl.planta.R;
import pl.planta.painter.Background;


public class DragAndDropPanel extends SurfaceView implements SurfaceHolder.Callback
{
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


    Context mContext;

    public DragAndDropPanel(Context mContext)
    {
        super(mContext);
        this.mContext = mContext;
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
        a = new BitmapFactory.Options();
        a.inScaled=false;
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.rury,a));
        myBoard = new Board(0,0,WIDTH,HEIGHT,getContext());
        thread = new DragAndDropThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int positionX = (int)(event.getX()/scaleFactorX);
        int positionY = (int)(event.getY()/scaleFactorY);
        if(canMove){
            myBoard.movePipes(positionX, positionY);
            return true;
        }

        System.out.println(myBoard.getPipeArea().contains(50,350));
        //  myBoard.movePipes(positionX, positionY);
        if(event.getAction()==MotionEvent.ACTION_DOWN&&myBoard.getPipeArea().contains(positionX, positionY)){
            canMove=true;
            return true;
        }

        if(event.getAction()==MotionEvent.ACTION_UP){
            System.out.println("actuin up");
            canMove=false;
            return true;
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
        }
    }

    public void newGame() {
    }





}