package pl.planta.games.dragAndDropGame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;

import java.util.ArrayList;

public class Board  {
    final private int FieldsOnXAxis=7             +1;//Jeden wiersz wiecej na panel gdzie beda wygenerowane
    final private int FieldsOnYAxis=4;
    private int BoardX, BoardY, BoardWidth, BoardHeight;
    private ArrayList<Pipe> pipes;
    private String map="3 0 1 1 ";
    private int amount;
    private int typeOfPart;
    private int liczbaKolanek;
    private int liczbaProstych;
    private int startXKolanek;
    private int startYKolanek;
    private int startXProstych;
    private int startYProstych;
    private int partWidth;
    private int partHeight;

    public Region getPipeArea() {
        return pipeArea;
    }

    private Rect pipeRect;
    private Region pipeArea;

    public Board(int x,int y, int width, int height, Context context) {
        partWidth=width/FieldsOnXAxis;
        partHeight=height/FieldsOnYAxis;
        BoardX=x; BoardY=y; BoardWidth=width; BoardHeight=height;
        amount=2;
        pipes =new ArrayList<>();
        typeOfPart =0;
        for(int i=0;i<amount;i++){
            if(typeOfPart==0) {
                pipes.add(new StraightPipe(context, 0, height-partHeight, partWidth, partHeight));
                pipeRect= new Rect(0, height-partHeight, partWidth, partHeight);
                pipeArea=new Region();
                pipeArea.set(pipeRect);
                //liczbaProstych++;
                typeOfPart =1;
            }
            else if(typeOfPart==1) {
                pipes.add(new KneePipe(context,0, height-partHeight*2, partWidth, partHeight));
                //liczbaKolanek++;
            }
        }
    }

    public void movePipes(int x, int y){
        pipes.get(0).setX(x - partWidth / 2);
        pipes.get(0).setY(y - partHeight / 2);
        //System.out.println(pipes.get(0).getX());
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(BoardX, BoardY, BoardWidth, BoardHeight, paint);
        for (int i=1;i<=FieldsOnXAxis;i++)
        {
            for (int j=0;j<=FieldsOnYAxis;j++) {
                canvas.drawRect(i * partWidth, j * partHeight, partWidth, partHeight, paint);
            }
        }
     //   paint.setTextSize(30);
       // paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        for (int i=0;i< pipes.size();i++)
        {
            pipes.get(i).draw(canvas);
        }
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,100,100,paint);
        //canvas.drawText(""+liczbaKolanek,startXKolanek+100,startYKolanek+100,paint);
        //canvas.drawText(""+liczbaProstych,startXProstych+100,startYProstych+100,paint);
    }
}
