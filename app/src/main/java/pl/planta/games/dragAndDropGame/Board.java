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
    private String map="9 2";//pierwsza liczba to liczba prostych, druga to kolanka
    private int liczbaProstych;
    private int liczbaKolanek;
    private int startXKolanek;
    private int startYKolanek;
    private int startXProstych;
    private int startYProstych;
    private int startXIn;
    private int startYIn;
    private int startXOut;
    private int startYOut;
    private int partWidth;
    private int partHeight;


    public Board(int x,int y, int width, int height, Context context) {
        partWidth=width/FieldsOnXAxis;
        partHeight=height/FieldsOnYAxis;
        startXProstych=0; startYProstych= height-partHeight*2;
        startXKolanek=0; startYKolanek= height-partHeight;
        startXOut=-partWidth+10; startYOut=0;
        startXIn=(FieldsOnXAxis*partWidth)-10; startYIn=height-partHeight;
        BoardX=x; BoardY=y; BoardWidth=width; BoardHeight=height;
        liczbaProstych =  Character.getNumericValue(map.charAt(0));
        liczbaKolanek =  Character.getNumericValue(map.charAt(2));
        pipes =new Stack<>();


        pipes.add(new OutPipe(context, startXOut, startYOut, partWidth, partHeight));
        pipes.add(new InPipe(context, startXIn, startYIn, partWidth, partHeight));

        for(int i=0;i<liczbaProstych;i++){
                pipes.add(new StraightPipe(context, startXProstych, startYProstych, partWidth, partHeight));
        }
        for(int i=0;i<liczbaKolanek;i++){
                pipes.add(new KneePipe(context, startXKolanek, startYKolanek, partWidth, partHeight));
        }
    }

    public void movePipes(int x, int y){
        for(int i = pipes.size()-1; i>=2; i--) {
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

    public void peekPipe(int x, int y){
        for(int i = pipes.size()-1; i>=0; i--) {
            if (pipes.get(i).getPipeArea().contains(x, y)) {
                pipes.push(pipes.get(i));
                pipes.remove(pipes.get(i));
                break;
            }
        }
    }

    public boolean fitPipe(){
        Pipe myObj = pipes.peek();
        double xRegion =(int)((double)myObj.getPipeArea().centerX()/(double)BoardWidth*FieldsOnXAxis);
        myObj.setX((int)(xRegion*myObj.getWidth()));
        double yRegion =(int)((double)myObj.getPipeArea().centerY()/(double)BoardHeight*FieldsOnYAxis);
        myObj.setY((int)(yRegion*myObj.getHeight()));
        myObj.updateArea();
        return true;
    }

    public void rotatePipe(){
        Pipe myObj = pipes.peek();
        myObj.rotateBitmap(90);
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

    public boolean check(){
        boolean isGood=false;
            for(int i = pipes.size()-1; i>=2; i--) {
                if (pipes.get(i).getPipeArea().contains(pipes.get(0).getPipeArea().centerX()+partWidth,pipes.get(0).getPipeArea().centerY())) {
                    if (pipes.get(i).getOuts()[3]) {
                        isGood = true;
                        pipes.get(i).setChecked(true);
                    }
                }
            }
        if (!isGood) return false;
        isGood = false;
            for(int i = pipes.size()-1; i>=2; i--) {
                if (pipes.get(i).getPipeArea().contains(pipes.get(1).getPipeArea().centerX()-partWidth,pipes.get(1).getPipeArea().centerY())) {
                    if (pipes.get(i).getOuts()[1]) {
                        isGood = true;
                        pipes.get(i).setChecked(true);
                    }
                }
            }
        if(!isGood) return false;
        isGood = false;
        for(Pipe e : pipes.subList( 2, pipes.size() )) {
            if(e.isChecked()) {
                if (e.getOuts()[0]) {
                    for (int i = pipes.size() - 1; i >= 0; i--) {
                        if (pipes.get(i).getPipeArea().contains(e.getPipeArea().centerX(), e.getPipeArea().centerY() - partHeight)) {
                            if (pipes.get(i).getOuts()[2]) {
                                isGood=true;
                                pipes.get(i).setChecked(true);
                            }
                        }

                    }
                    if(!isGood) return false;
                    isGood = false;
                }
                if (e.getOuts()[1]) {
                    for (int i = pipes.size() - 1; i >= 0; i--) {
                        if (pipes.get(i).getPipeArea().contains(e.getPipeArea().centerX() + partWidth, e.getPipeArea().centerY())) {
                            if (pipes.get(i).getOuts()[3]) {
                                isGood=true;
                                pipes.get(i).setChecked(true);
                            }
                        }
                    }
                    if(!isGood) return false;
                    isGood = false;
                }
                if (e.getOuts()[2]) {
                    for (int i = pipes.size() - 1; i >= 0; i--) {
                        if (pipes.get(i).getPipeArea().contains(e.getPipeArea().centerX(), e.getPipeArea().centerY() + partHeight)) {
                            if (pipes.get(i).getOuts()[0]) {
                                isGood=true;
                                pipes.get(i).setChecked(true);
                            }
                        }
                    }
                    if(!isGood) return false;
                    isGood = false;
                }
                if (e.getOuts()[3]) {
                    for (int i = pipes.size() - 1; i >= 0; i--) {
                        if (pipes.get(i).getPipeArea().contains(e.getPipeArea().centerX() - partWidth, e.getPipeArea().centerY())) {
                            if (pipes.get(i).getOuts()[1]) {
                                isGood=true;
                                pipes.get(i).setChecked(true);
                            }
                        }
                    }
                    if(!isGood) return false;
                    isGood=false;

                }
            }
        }
        return true;
    }

    public Stack<Pipe> getPipes() {
        return pipes;
    }
}
