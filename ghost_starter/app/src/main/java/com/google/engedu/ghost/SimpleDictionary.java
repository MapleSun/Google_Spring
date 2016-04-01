package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }

    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
//        If prefix is empty return a randomly selected word from the words ArrayList
//        Otherwise, perform a binary search over the words ArrayList until you find a word that starts with the given prefix and return it.
//        If no such word exists, return null
        if(null == prefix || prefix.length() == 0){
            Log.d("Start game", "Computer Starts game");
            random = new Random();
            String StartWord = words.get(random.nextInt(words.size() - 1));
            //Log.d("start", StartWord);
            //Log.d("random",Integer.toString(random.nextInt(words.size() - 1)));
            Log.d("Computer start word", StartWord);
            return StartWord;
        }else{
            return BinarySearch(prefix);
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }

    public String BinarySearch(String prefix){
        int BEGIN = 0;
        int END = words.size() - 1;
        while(BEGIN < END) {
            int MID = (BEGIN + END) / 2;
            if (words.get(MID).startsWith(prefix)) {
                return words.get(MID);
            }
            if (words.get(MID).compareTo(prefix) < 0) {
                BEGIN = MID + 1;

            } else if (words.get(MID).compareTo(prefix) > 0) {
                END = MID - 1;
            }
        }
        return null;


    }
}
