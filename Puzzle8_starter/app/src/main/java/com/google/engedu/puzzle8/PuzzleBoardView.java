package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap, View parent) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            Log.d("Debug","shuffle");
            int numMoves = NUM_SHUFFLE_STEPS;
            for (int i = 0; i < numMoves; i++) {
                ArrayList<PuzzleBoard> newboards = puzzleBoard.neighbours();
                puzzleBoard = newboards.get(random.nextInt(newboards.size()));
            }

            //puzzleBoard.priority();

            invalidate();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {
        Comparator<PuzzleBoard> puzzleBoardComparator = new PuzzleBoardComparator();
        PriorityQueue<PuzzleBoard> solutionQueue = new PriorityQueue<PuzzleBoard>(100000 ,new PuzzleBoardComparator());
        puzzleBoard.reset();
        solutionQueue.add(puzzleBoard);
        //for(int i=0; i<5;i++){
        while(!solutionQueue.isEmpty()){
            PuzzleBoard currBoard = solutionQueue.poll();

            int LowestPriority = currBoard.priority()-currBoard.getSteps();
            //Log.d("LowestPriority", "" + LowestPriority);
            // If it is not the solution, add all neighbours in PriorityQueue
            if(LowestPriority != 0){
                ArrayList<PuzzleBoard> possibleBoard = currBoard.neighbours();

                for (PuzzleBoard board:possibleBoard) {
                    solutionQueue.add(board);
                    //Log.d("board", "" + board.priority());

                }
            }else{
                // Create an ArrayList of all the PuzzleBoards leading to this solution
                ArrayList<PuzzleBoard> PathToVic = currBoard.previousBoard();
                Collections.reverse(PathToVic);
                animation = PathToVic;
                invalidate();
                return;
            }



        }


    }

    class PuzzleBoardComparator implements Comparator<PuzzleBoard>{

        @Override
        public int compare(PuzzleBoard lhs, PuzzleBoard rhs) {
            int lhsPrio = lhs.priority();
            int rhsPrio = rhs.priority();
           return lhsPrio - rhsPrio;
        }
    }

}
