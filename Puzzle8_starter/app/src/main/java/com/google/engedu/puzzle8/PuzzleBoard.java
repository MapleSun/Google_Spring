package com.google.engedu.puzzle8;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;


public class PuzzleBoard {
    private PuzzleBoard previousBoard;
    private int Steps = 0;
    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        tiles = new ArrayList<PuzzleTile>();
        //Bitmap[][] subBitmaps = new Bitmap[3][3];
        Bitmap subBitmap ;
        Bitmap bitmapFullSize = Bitmap.createScaledBitmap(bitmap,parentWidth,parentWidth,false);
        PuzzleTile newTile;
        int tilesize = parentWidth / NUM_TILES;
        int xStart = 0;
        int yStart = 0;
        for(int i =0; i<NUM_TILES; i++){
            for(int j=0; j<NUM_TILES; j++) {
                xStart = tilesize*j;
                yStart = tilesize*i;

                subBitmap = Bitmap.createBitmap(bitmapFullSize, xStart,yStart,tilesize,tilesize);
                //subBitmap = Bitmap.createBitmap(bitmapFullSize, xStart,yStart,parentWidth / NUM_TILES,parentWidth / NUM_TILES);

                if(i*j == (NUM_TILES-1)*(NUM_TILES-1)){
                    newTile = null;
                }else {
                    newTile = new PuzzleTile(subBitmap, j+i*NUM_TILES);
                    //newTile = new PuzzleTile(subBitmap, i + j);
                }
                tiles.add(newTile);
            }
        }
        //neighbours();
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        this.Steps = otherBoard.Steps + 1;
        previousBoard = otherBoard;
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
        this.Steps = 0;
        this.previousBoard = null;
    }

    public int getSteps(){return Steps;}


    public ArrayList<PuzzleBoard> previousBoard(){
        ArrayList<PuzzleBoard> previousBoards = new ArrayList<>();
        PuzzleBoard currBoard = this;
        Log.d("Debug","currBoard "+currBoard.priority());
        previousBoards.add(currBoard);
        while(currBoard.previousBoard!=null){
            Log.d("Debug","CurrBoard"+currBoard.priority());
            previousBoards.add(currBoard.previousBoard);
             currBoard = currBoard.previousBoard;
        }
        return previousBoards;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public ArrayList<PuzzleBoard> neighbours() {
        int emptytileNum = tiles.indexOf(null);
        int xCord = emptytileNum / NUM_TILES;
        int yCord = emptytileNum % NUM_TILES;

        ArrayList<PuzzleBoard> BoardList = new ArrayList<>();
        for(int i = 0; i<4; i++){
            //Log.d("Debug","i is "+ Integer.toString(i)+" j is "+ Integer.toString(j));
            int neighbourXcord = xCord + NEIGHBOUR_COORDS[i][0];
            int neighbourYcord = yCord + NEIGHBOUR_COORDS[i][1];
            if ((neighbourXcord>=0 && neighbourXcord<NUM_TILES)&&(neighbourYcord>=0 && neighbourYcord<NUM_TILES)){
                //Log.d("neighbour", "Xcord"+Integer.toString(neighbourXcord)+" Ycord" + Integer.toString(neighboutYcord));
                PuzzleBoard newBoard = new PuzzleBoard(this);
                int goodNeighbourNum = XYtoIndex(neighbourXcord, neighbourYcord);
                newBoard.swapTiles(emptytileNum, goodNeighbourNum);
                BoardList.add(newBoard);
            }

        }

        return BoardList;
    }


    public int priority() {
        int Manhattan = 0;

        for(int i=0; i<NUM_TILES*NUM_TILES; i++){
            if(this.tiles.get(i) != null) {
                int xCurr = i/NUM_TILES;
                int yCurr = i%NUM_TILES;
                int xHome = this.tiles.get(i).getNumber() / NUM_TILES;
                int yHome = this.tiles.get(i).getNumber() % NUM_TILES;
                Manhattan += Math.abs(xCurr-xHome) + Math.abs(yCurr-yHome);

                //Log.d("HOME", "Xcurr "+ Integer.toString(xCurr) + " Ycurr " + Integer.toString(yCurr));
                //Log.d("HOME", "Xhome "+ Integer.toString(xHome) + " Xhome " + Integer.toString(yHome));
                //Log.d("tile in board "+i, Integer.toString(this.tiles.get(i).getNumber()) + " Manhattan "+Manhattan );


            }
        }
        //Log.d("Manhattan","Steps"+Integer.toString(Steps) + "   " + Integer.toString(Manhattan));

        return Manhattan + Steps;
    }

}
