package pl.planta.games.dragAndDropGame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Board  {
    final private int FieldsOnXAxis=7             +1;//Jeden wiersz wiecej na panel gdzie beda wygenerowane
    final private int FieldsOnYAxis=4;
    private int BoardX, BoardY, BoardWidth, BoardHeight;
    private Stack<Pipe> pipes;
    private String map="2 2";//pierwsza liczba to liczba prostych, druga to kolanka
    private int liczbaProstych;
    private int liczbaKolanek;
    private int startXKolanek;
    private int startYKolanek;
    private int startXProstych;
    private int startYProstych;
    private int partWidth;
    private int partHeight;


    public Board(int x,int y, int width, int height, Context context) {
        partWidth=width/FieldsOnXAxis;
        partHeight=height/FieldsOnYAxis;
        startXProstych=0; startYProstych= height-partHeight;
        startXKolanek=0; startYKolanek= height-partHeight*2;
        BoardX=x; BoardY=y; BoardWidth=width; BoardHeight=height;
        liczbaProstych =  Character.getNumericValue(map.charAt(0));
        liczbaKolanek =  Character.getNumericValue(map.charAt(2));
        pipes =new Stack<>();
        for(int i=0;i<liczbaProstych;i++){
                pipes.add(new StraightPipe(context, startXProstych, startYProstych, partWidth, partHeight));
        }
        for(int i=0;i<liczbaKolanek;i++){
                pipes.add(new KneePipe(context, startXKolanek, startYKolanek, partWidth, partHeight));
        }
    }

    public void movePipes(int x, int y){
        for(int i = pipes.size()-1; i>=0; i--) {
            if (pipes.get(i).getPipeArea().contains(x, y)) {
                pipes.push(pipes.get(i));
                pipes.peek().setX(x - partWidth / 2);
                pipes.peek().setY(y - partHeight / 2);
                pipes.peek().updateArea();
                pipes.remove(pipes.get(i));
                break;
            }
        }
    }

    public boolean isPipeClicked(int positionX, int positionY){
        for(Pipe e : pipes) {
            if (e.getPipeArea().contains(positionX, positionY)) {
                return true;
            }
        }
        return false;
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
        for (int i=0;i< pipes.size();i++)
        {
            pipes.get(i).draw(canvas);
        }

    }
}
