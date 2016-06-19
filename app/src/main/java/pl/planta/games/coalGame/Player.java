package pl.planta.games.coalGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.HashMap;

import pl.planta.helper.SQLiteHandler;

public class Player extends GameObject {
    private Bitmap spritesheet;
    private int difficulty;
    private boolean playing;
    private Animations animation = new Animations();
    private long startTime;



    public Player(Context context, Bitmap res, int w, int h, int numFrames){

        x=GamePanel.WIDTH/2-250;
        y=GamePanel.HEIGHT-290;
        dx=0;
        difficulty =0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        for(int i =0; i<image.length; i++){
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();
    }

    public void move(float b)
    {

        dx += b;
    }

    public void update(){
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100)
        {
            difficulty++;
            startTime =System.nanoTime();
        }
        animation.update();
        if(dx>14)dx=14;
        if(dx<-14)dx=-14;
        x += dx*2;
        if(x<-20){
            x=-20;
            dx=0;
        }
        if(x>890) {
            x=890;
            dx=0;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getDifficulty(){return difficulty;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing=b;}
    public void resetDX(){dx=0;}
    public void resetScore(){
        difficulty =0;}
    @Override
    public Rect getRectangle(){
        return new Rect(x+20, y, x+getWidth()-20, y+getHeight());
    }

}
