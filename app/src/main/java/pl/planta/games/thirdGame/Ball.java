package pl.planta.games.thirdGame;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import pl.planta.R;


public class Ball {
    private Bitmap image;
    private Bitmap scaledImage;
    protected BitmapFactory.Options a;
    protected int x;
    protected int y;

    public Ball(Context context, int x, int y, int width, int height){
        a = new BitmapFactory.Options();
        a.inScaled=false;
        this.x = x;
        this.y = y;
        image =  BitmapFactory.decodeResource(context.getResources(), R.drawable.kula ,a);
        scaledImage = Bitmap.createScaledBitmap(image, width, height ,true);
    }

    public void getCollision() {

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(scaledImage,x,y,null);
    }
}
