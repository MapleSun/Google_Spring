package com.google.engedu.ghost;

import android.util.Log;
import java.util.HashMap;
import java.util.Random;



public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;
    private Random random;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        String Letter = new String();
        TrieNode CurrentNode = this;
        for (int index=0; index<s.length(); index++) {
            String ParentWord = Letter;
            Letter += s.toCharArray()[index] + "";
            //Log.d("Current Letter is ", Letter);

            if(!CurrentNode.children.containsKey(Letter)){
                //Log.d("Dont have", Letter+" Create new Node");
                TrieNode newChildNode = new TrieNode();
                //TrieNode newGrandChildNode = new TrieNode();
                //newChildNode.children.put(Letter,newGrandChildNode);
                CurrentNode.children.put(Letter,newChildNode);

                if(index == s.length()-1){
                    //Log.d("String finished", "new String is "+Letter);
                    CurrentNode.isWord = true;
                    return;
                }
                CurrentNode=CurrentNode.children.get(Letter);
            }else{
                CurrentNode=CurrentNode.children.get(Letter);
               // Log.d("Exist", "dont need to add it "+Letter);
            }


        }
    }



    public boolean isWord(String s) {
        String Letter = new String();
        TrieNode CurrentNode = this;
        for (int index=0; index < s.length(); index++) {
                Letter += s.toCharArray()[index] + "";
            if(!CurrentNode.children.containsKey(Letter)){
                Log.d("No such word", Letter);
                return false;
            }else{
                if(index != s.length()-1){
                    CurrentNode=CurrentNode.children.get(Letter);
                    //Log.d("Stilling Search", Letter);
                }else {
                    return CurrentNode.isWord;
                }
            }
        }
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        //Log.d("Start get word", "start get word");

        if(null == s || s.length() == 0) {
            random = new Random();
            String FirstLetter = (char)(random.nextInt(26) + 'a')+"";
            return FirstLetter;
        }else {
            TrieNode CurrentNode = this;
            String Letter = new String();
            for(int i=0; i<s.length();i++) {
                Letter += s.toCharArray()[i] + "";
                if(!CurrentNode.children.containsKey(Letter)){
                    return null;
                }
                CurrentNode = CurrentNode.children.get(Letter);
            }

            Object[] keys = CurrentNode.children.keySet().toArray();
            Random rand = new Random();
            String randomword = (String) keys[rand.nextInt(keys.length)];
            return  randomword;
        }
    }



    public String getGoodWordStartingWith(String s) {
        if(null == s || s.length() == 0) {
            random = new Random();
            String FirstLetter = (char)(random.nextInt(26) + 'a')+"";
            return FirstLetter;
        }else {
            TrieNode CurrentNode = this;
            String Letter = new String();
            for(int i=0; i<s.length();i++) {
                Letter += s.toCharArray()[i] + "";
                if(!CurrentNode.children.containsKey(Letter)){
                    return null;
                }
                CurrentNode = CurrentNode.children.get(Letter);
            }

            Object[] keys = CurrentNode.children.keySet().toArray();
            int i = 0;
            String goodWord = new String();
            do {
                goodWord = (String) keys[i];
                //Log.d("goodWord", goodWord);
                i++;
            } while(CurrentNode.children.get(goodWord).isWord && i<keys.length);
            return goodWord;

        }
    }
}
