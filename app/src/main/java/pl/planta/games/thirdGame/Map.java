package pl.planta.games.thirdGame;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Map {
private Bitmap allMap;
private Bitmap viewMap;
    private Bitmap mapDraw;
    private int width,height;
int x=0,y=0;
int dx,dy;

        public Map(Bitmap res, int width, int height)
        {
            allMap = res;
            viewMap =  Bitmap.createBitmap(allMap, 0, 0, 500, 500);
            mapDraw= Bitmap.createScaledBitmap(viewMap,width , height,false);
            this.width=width;
            this.height=height;
        }

        public void move(float x, float y)
        {
            dx += x;
            dy += y;
        }

        public void update()
        {
            //System.out.println(image.getPixel(-x,-y)) ;
            //if(image.getPixel(-215,-250)==0xffffff00)
           // {
            //    dx=-dx;
            //    dy=-dy;
           // }
            if(dx>5)dx=5;
            if(dx<-5)dx=-5;
            if(dy>5)dy=5;
            if(dy<-5)dy=-5;
            x += dx;
            y += dy;
           if(x>2500){
                x=2500;
                dx=0;
            }
            if(x<0) {
                x=0;
                dx=0;
            }
            if(y>2499){
                y=2499;
                dy=0;
            }
            if(y<0) {
                y=0;
                dy=0;
            }
            //System.out.println(x+ "  +  "+y);
            viewMap =  Bitmap.createBitmap(allMap, x, y, 500, 500);
            mapDraw= Bitmap.createScaledBitmap(viewMap,width, height,false);
        }
        public void draw(Canvas canvas)
        {
            canvas.drawBitmap(mapDraw,0 , 0, null);
        }
}
