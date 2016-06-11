package pl.planta.games.dragAndDropGame;


import android.content.Context;
import android.graphics.Bitmap;

public class StraightPipe extends Pipe {

    private Bitmap image;

    public StraightPipe(Context context, int x, int y, int width, int height) {
        super(context, x, y, width, height);
        outs[3]=true;
        outs[1]=true;
        image =  Bitmap.createBitmap(partsImage, 0, 0, 200, 200);
        super.image =  Bitmap.createScaledBitmap(image, width, height,false);
    }

    @Override
    public void rotateBitmap(float angle){
        super.rotateBitmap(angle);
        boolean[] temp = new boolean[4];
        System.arraycopy(outs,0,temp,0,4);
        outs[0]=temp[3];
        outs[1]=temp[0];
        outs[2]=temp[1];
        outs[3]=temp[2];
    }
}

