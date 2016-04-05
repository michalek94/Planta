package pl.planta.games.dragAndDropGame;


import android.content.Context;
import android.graphics.Bitmap;

public class StraightPipe extends Pipe {

    private Bitmap image;

    public StraightPipe(Context context, int x, int y, int width, int height) {
        super(context, x, y, width, height);
        image =  Bitmap.createBitmap(partsImage, 0, 0, 200, 200);
        super.image =  Bitmap.createScaledBitmap(image, width, height,false);
    }
}

