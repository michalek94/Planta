package pl.planta.games.dragAndDropGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import pl.planta.R;

public abstract class Pipe {
    protected Bitmap partsImage;
    protected Bitmap image;
    protected BitmapFactory.Options a;
    protected Rect pipeArea;
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected boolean[] outs = {false,false,false,false};
    protected boolean isChecked=false;

    public Pipe(Context context, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        pipeArea = new Rect(x,y,x+width,y+height);
        a = new BitmapFactory.Options();
        a.inScaled=false;
        partsImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.pipes_small,a);
    }

    public void rotateBitmap(float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        image = Bitmap.createBitmap(image , 0, 0,width, height, matrix, true);
    }

    public void updateArea(){
        pipeArea = new Rect(x,y,x+width,y+height);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image,x,y,null);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
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

    public Rect getPipeArea() {
        return pipeArea;
    }

    public void setPipeArea(Rect pipeArea) {
        this.pipeArea = pipeArea;
    }

    public boolean[] getOuts() {
        return outs;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
