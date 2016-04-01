package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private HashSet<String> wordSet = null;
    private ArrayList<String> wordList = null;
    private HashMap<String, ArrayList<String>> lettersToWord = null;
    private HashMap<Integer, ArrayList<String>> sizeToWords = null;
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        wordSet = new HashSet<String>();
        wordList = new ArrayList<String>();
        lettersToWord = new HashMap<String,ArrayList<String>>();
        sizeToWords = new HashMap<Integer, ArrayList<String>>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            wordList.add(word);
            String sortedword = sortWord(word);
            if (!lettersToWord.containsKey(sortedword)) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                lettersToWord.put(sortedword, list);
            } else {
                lettersToWord.get(sortedword).add(word);
            }

            int length = word.length();
            if(!sizeToWords.containsKey(length)){
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                sizeToWords.put(length, list);
            }
            else{
                sizeToWords.get(length).add(word);
            }
        }

    }

    public static String sortWord(String args) {
        String original = args;
        char[] chars = original.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return sorted;
    }

    public boolean isGoodWord(String word, String base) {
        if(!wordSet.contains(word)) {
            return false;
        }
        else if(word.contains(base)){
            return false;
        }
        return true;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (char c:alphabet) {
            String gameWord = word + c;
            String keyWord = sortWord(gameWord);
            if (lettersToWord.containsKey(keyWord)) {
                for (String answer:lettersToWord.get(keyWord))
                      {
                          if(isGoodWord(answer,word)){
                              result.add(answer);
                          }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {

        String starterWord = new String();
        int startedIndex = random.nextInt(wordList.size());

        int wordspoolsize = sizeToWords.get(wordLength).size();
        ArrayList<String> wordspool = sizeToWords.get(wordLength);
        while(true){
            String starterword = wordspool.get(startedIndex % wordspoolsize);

            if(getAnagramsWithOneMoreLetter(starterword).size() >= MIN_NUM_ANAGRAMS){

                for (String answer:getAnagramsWithOneMoreLetter(starterword)
                     ) {
                    Log.d("Answer",answer);
                }

                if(wordLength < MAX_WORD_LENGTH){
                    wordLength++;
                }
                return starterword;

            }
            startedIndex++;
        }

    }
}
