package pl.planta.games.dragAndDropGame;


import android.content.Context;
import android.graphics.Bitmap;

public class InOutPipe extends Pipe {

    private Bitmap image;

    public InOutPipe(Context context, int x, int y, int width, int height) {
        super(context, x, y, width, height);
        image =  Bitmap.createBitmap(partsImage, 400, 0, 200, 200);
        super.image =  Bitmap.createScaledBitmap(image, width, height, false);
    }


}