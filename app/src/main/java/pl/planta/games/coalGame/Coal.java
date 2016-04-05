package pl.planta.games.coalGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Coal extends GameObject {

    private int score, speed;
    private Random rand = new Random();
    private Animations animations = new Animations();
    private Bitmap spriteSheet;
    public boolean areCatched = false;

    public Coal(Bitmap res, int x, int y, int w, int h, int s, int numFrames){
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        speed = 8 + (int)(rand.nextDouble()*score/30);

        if(speed >= 20){
            speed = 20;
        }

        Bitmap[] image = new Bitmap[numFrames];
        spriteSheet = res;

        for(int i=0; i<image.length; i++){
            image[i] = Bitmap.createBitmap(spriteSheet, i*width, 0, width, height);
        }

        animations.setFrames(image);
        animations.setDelay(100 - speed);
    }

    public void update(){
        y += speed;
        animations.update();
    }

    public void draw(Canvas canvas, int playerX){
        double helpx;
        int ad;
        helpx = (double)(playerX+170)/(double)(x+width/2);

        if(helpx < 1){
            ad = -6;
        }else{
            ad = 6;
        }

        if(areCatched){
            x = x + ad;
        }
        try{
            canvas.drawBitmap(animations.getImage(), x, y, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height - 25;
    }

    @Override
    public Rect getRectangle() {
        return new Rect(x+getWidth()/2-1, y, x+(getWidth()/2)+1, y+getHeight());
    }
}
