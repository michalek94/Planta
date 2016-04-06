package pl.planta.games.dragAndDropGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import pl.planta.R;

public abstract class Pipe {
    protected Bitmap partsImage;
    protected Bitmap image;
    protected BitmapFactory.Options a;
    protected boolean isMatched;
    protected int x;
    protected int y;

    public Pipe(Context context, int x, int y, int width, int height) {
        a = new BitmapFactory.Options();
        a.inScaled=false;
        partsImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipes,a);
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image,x,y,null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getIsMatched() {
        return isMatched;
    }

    public void setIsMatched(boolean isMatched) {
        this.isMatched = isMatched;
    }

    public BitmapFactory.Options getA() {
        return a;
    }

    public void setA(BitmapFactory.Options a) {
        this.a = a;
    }

    public Bitmap getPartsImage() {
        return partsImage;
    }

    public void setPartsImage(Bitmap partsImage) {
        this.partsImage = partsImage;
    }





}
