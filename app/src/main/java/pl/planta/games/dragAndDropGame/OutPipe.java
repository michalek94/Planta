package pl.planta.games.dragAndDropGame;


import android.content.Context;
import android.graphics.Bitmap;

public class OutPipe extends Pipe {

    private Bitmap image;

    public OutPipe(Context context, int x, int y, int width, int height) {
        super(context, x, y, width, height);
        outs[1]=true;
        image =  Bitmap.createBitmap(partsImage, 400, 0, 200, 200);
        super.image =  Bitmap.createScaledBitmap(image, width, height, false);
    }

    @Override
    public void rotateBitmap(float angle){

    }
    @Override
    public void setX(int x){

    }
    @Override
    public void setY(int y){

    }
}