package com.google.engedu.ghost;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static com.google.engedu.ghost.SimpleDictionary.*;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView ghostText;
    private TextView gameStatus;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);


        try {
            InputStream Wordlist = getAssets().open("words.txt");
            //dictionary = new SimpleDictionary(Wordlist);
            dictionary = new FastDictionary(Wordlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ghostText = (TextView) findViewById(R.id.ghostText);
        gameStatus = (TextView) findViewById(R.id.gameStatus);

        if(dictionary.isWord("food")){
            Log.d("In dictionary", "food");
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            // add the letter to the word fragment. Also check whether the current word fragment is a complete word and,
            // if it is, update the game status label to indicate
            // so (this is not the right behavior for the game but will allow you to verify that your code is working for now).
            String aChar = String.valueOf((char) event.getUnicodeChar());
            //Log.d("aChar", aChar);
            ghostText.append(aChar.toLowerCase());
            String CurrentWord = getCurrentWord();
            //Log.d("CurrentWord", CurrentWord);

//            if (CheckWord(CurrentWord)) {
//                gameStatus.setText("The word is in dictonary");
//                return true;
//            }
            computerTurn();
            return true;

        } else {
            //Log.d("Some", "Not letter!");
            return super.onKeyUp(keyCode, event);
        }
    }



    public boolean CheckWord(String CurrentWord){
        return dictionary.isWord(CurrentWord);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        String CurrentWord = getCurrentWord();
        if(CheckWord(CurrentWord) && CurrentWord.length() >=4) {
                label.setText("Computer Wins: Challenge");
        }else{
            if(null == dictionary.getAnyWordStartingWith(CurrentWord)){
                // challenge

                 label.setText("Computer Wins: No word has that predix");
                return;
            }else{
                //String possibleWord = dictionary.getAnyWordStartingWith(CurrentWord);
                String possibleWord = dictionary.getGoodWordStartingWith(CurrentWord);

                Log.d("possibleWord", possibleWord);
                String nextLetter = possibleWord.substring(CurrentWord.length(), CurrentWord.length()+1);
                Log.d("possibleWord", possibleWord);
                Log.d("nextLetter",nextLetter);
                ghostText.append(nextLetter);
                Log.d("User turn", "User turn");

                // Do computer turn stuff then make it the user's turn again
                userTurn = true;
                label.setText(USER_TURN);

            }


        }

    }

    private String getCurrentWord(){
        TextView ghostText = (TextView) findViewById(R.id.ghostText);
        String CurrentWord = ghostText.getText().toString();
        return CurrentWord;
    }

    public void Challenge(View view){
//        Get the current word fragment
//        If it has at least 4 characters and is a valid word, declare victory for the user
//        otherwise if a word can be formed with the fragment as prefix, declare victory for the computer and display a possible word
//        If a word cannot be formed with the fragment, declare victory for the user
        String CurrentWord = getCurrentWord();
        if(CheckWord(CurrentWord) && CurrentWord.length() >=4) {
            gameStatus.setText("Player Wins: Challenge");
        }else{
            gameStatus.setText("Wrong call, LOSE!");
        }
    }
    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Ghost Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.google.engedu.ghost/http/host/path")
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
                "Ghost Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.google.engedu.ghost/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
