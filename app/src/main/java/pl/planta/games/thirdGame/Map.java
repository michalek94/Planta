package pl.planta.games.thirdGame;


import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Map {
private Bitmap image;
//int x=-1170-350,y=-1000-350;
int x=-215,y=-250;
int dx,dy;

        public Map(Bitmap res)
        {
            image = res;
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
            x -= dx;
            y -= dy;
            if(x<-2250){
                x=-2250;
                dx=0;
            }
            if(x>0) {
                x=0;
                dx=0;
            }
            if(y<-2240){
                y=-2240;
                dy=0;
            }
            if(y>0) {
                y=0;
                dy=0;
            }
            //System.out.println(x+ "  +  "+y);
        }
        public void draw(Canvas canvas)
        {
            canvas.drawBitmap(image,x , y, null);
        }
}
