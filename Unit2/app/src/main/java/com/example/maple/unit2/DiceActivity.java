package com.example.maple.unit2;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Field;
import java.util.Random;


public class DiceActivity extends AppCompatActivity {

    // the user's overall score state
    // the user's turn score
    // the computer's overall score
    // the computer's turn score
    // randomly select a dice value
    // update the display to reflect the rolled value

    private int USER_OVERALL_SCORE=0;
    private int COMPUTER_OVERALL_SCORE=0;
    private int TURN_SCORE=0;
    private Random RANDOM_DICE_VALUE;
    private boolean PlayerTurn = true;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button RollButton = (Button) findViewById(R.id.RollButton);
        Button ResetButton = (Button) findViewById(R.id.ResetButton);
        Button HoldButton = (Button) findViewById(R.id.HoldButton);
        ImageView DiceFace = (ImageView) findViewById(R.id.imageView);
        TextView ScoreBar = (TextView) findViewById(R.id.textView);

        RollButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Player", "My Turn");
                RollDice();
                Log.d("Player Score", Integer.toString(TURN_SCORE));
                if(TURN_SCORE ==0) {
                    ControlButton(false);
                    final TextView ScoreBar = (TextView) findViewById(R.id.textView);
                    ScoreBar.setText(score());
                    PlayerTurn = false;

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    ComputerTurn();
                                }
                            }, 1500);


                }


            }
        });


        ResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ResetGame();
                Log.d("Reset", "=============================");
            }
        });


        HoldButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HoldScore();
                PlayerTurn = false;
                ComputerTurn();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    public void RollDice(){
        TextView ScoreBar = (TextView) findViewById(R.id.textView);
        ImageView DiceFace = (ImageView) findViewById(R.id.imageView);
        RANDOM_DICE_VALUE = new Random();
        int ran_dice = RANDOM_DICE_VALUE.nextInt(5);
        int[] dice_pic_array = new int[]{R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
        int resID = dice_pic_array[ran_dice];
        DiceFace.setImageResource(resID);
        if(ran_dice != 0) {
            TURN_SCORE += (ran_dice + 1);
            ScoreBar.setText(score());
        }else{
            TURN_SCORE = 0;
            ScoreBar.setText(score());
        }
    }

    public String score()
    {
        return "Your score: " + USER_OVERALL_SCORE
                +" Computer score: "+ COMPUTER_OVERALL_SCORE
                + " your turn score: "+ TURN_SCORE;
    }

    public void ResetGame(){
        final TextView ScoreBar = (TextView) findViewById(R.id.textView);
        USER_OVERALL_SCORE=0;
        TURN_SCORE=0;
        COMPUTER_OVERALL_SCORE=0;
        ScoreBar.setText(score());
    }

    public void HoldScore(){
        final TextView ScoreBar = (TextView) findViewById(R.id.textView);
        //When HoldButton is clicked, updating the user's overall score, reset the user round score and update the label.
        USER_OVERALL_SCORE+=TURN_SCORE;
        TURN_SCORE = 0;
        ScoreBar.setText(score());

    }

    public void ComputerTurn(){
        final TextView ScoreBar = (TextView) findViewById(R.id.textView);
        ControlButton(false);


        ComputerRoll();

    }


    public void ComputerRoll(){
        long startTime = 0;
        final TextView ScoreBar = (TextView) findViewById(R.id.textView);
        //runs without a timer by reposting this handler at the end of the runnable
        final Handler timerHandler = new Handler();
        final Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {

                if (TURN_SCORE < 20 && !PlayerTurn) {
                    Log.d("Computer Trun","Computer Turn");
                    RollDice();
                    Log.d("Computer Score", Integer.toString(TURN_SCORE));
                    if(TURN_SCORE == 0){
                        PlayerTurn = true;
                    }
                    timerHandler.postDelayed(this, 500);
                }else{
                    ControlButton(true);
                    PlayerTurn = true;
                    COMPUTER_OVERALL_SCORE += TURN_SCORE;
                    TURN_SCORE = 0;
                    ScoreBar.setText(score());
                }

            }
        };
        timerRunnable.run();
    }


    public void ControlButton(boolean is_able){

        Button RollButton = (Button) findViewById(R.id.RollButton);
        Button HoldButton = (Button) findViewById(R.id.HoldButton);
        RollButton.setEnabled(is_able);
        HoldButton.setEnabled(is_able);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Dice Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.maple.unit2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Dice Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.maple.unit2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


