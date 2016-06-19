package pl.planta.games.dragAndDropGame;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DragAndDropThread extends Thread
{
    private int FPS = 30;
    private double averageFPS;
    private final SurfaceHolder surfaceHolder;
    private DragAndDropPanel dragAndDropPanel;
    private boolean running;
    public static Canvas canvas;

    public DragAndDropThread(SurfaceHolder surfaceHolder, DragAndDropPanel dragAndDropPanel)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.dragAndDropPanel = dragAndDropPanel;
    }
    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount =0;
        long targetTime = 1000/FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.dragAndDropPanel.update();
                    this.dragAndDropPanel.myDraw(canvas);
                }
            } catch (Exception e) {
            }
            finally{
                    if(canvas!=null)
                    {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                        catch(Exception e){e.printStackTrace();}
                    }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;
            try{
                sleep(waitTime);
            }catch(Exception e){
                e.printStackTrace();
            }

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == FPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount =0;
                totalTime = 0;
            }
        }
    }
    public void setRunning(boolean b)
    {
        running=b;
    }
}