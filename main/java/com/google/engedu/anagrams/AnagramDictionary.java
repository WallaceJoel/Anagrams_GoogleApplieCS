

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static ArrayList<String> wordList = new ArrayList<>();
    private static HashMap<Integer,  ArrayList<String>> SizeToWords = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        /*
        This method is very simple it reads the file containing all the words that will be used
        for this game. It reads the file from BufferedReader in, String line represents each line
        in the file, String word, takes the word from the file. String currentword saves that word.
        Then the word is added to wordList, which contains all the words from the file,
         and is added to SizeToWords, which has all the words separated into lists corresponding to
         the size of the word.

         */
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            String currentword = wordList.get(wordList.size()-1);

            if(SizeToWords.containsKey(currentword.length())) {
                SizeToWords.get(currentword.length()).add(currentword);
            }else {
                ArrayList<String> wordlist = new ArrayList<String>();
                SizeToWords.put(currentword.length(), wordlist);
            }

        }

    }
    public static String getSortedLetters(String word) {
        /*
        Simple method, it sorts the String Word, in alphabetic order, so a word like "Jackson"
        becomes "acjknos", This method is needed in order shorten our workload, because instead of
        comparing char by char, you can just sort both words, and the two words should be equal.
         */
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int alpha[] = new int [26];
        Arrays.fill(alpha, 0);

        /*this loop is to make sure the word has no illegal character. Then it counts the number of
        each alphabet letter in gives that information to the integer array. For example "Baby"
        would be alpha[0] = 1, alpha[2]= 2 ,alpha[24] = 1 , and every other index is 0
        */
        for(int i = 0; i < word.length(); i++) {
            if(alphabet.indexOf(word.toLowerCase().charAt(i)) == -1) {
                return "WORD HAS ILLEGAL CHARACTERS";
            }else {
                int letter = (word.toLowerCase().charAt(i)-'z')+25;
                alpha[letter] += 1;
            }
        }
        /*
        This loop is simple as well , all it does it prints the characters in alpha based on the
        int held at their corresponding index. Resulting in the sorted version of the argument given

         */
        String sorted = "";
        for(int i = 0; i < 26; i++) {
            for(int a = 0; a < alpha[i]; a++) {
                char letter = (char) (i-25+'z');

                sorted += letter;
            }
        }

        return sorted;

    }
    public boolean isGoodWord(String word, String base) {
        //Self-explanatory
        if(word.contains(base)) {
            return false;
        }else{
            return true;
        }
    }
    public List<String> getAnagrams(String targetWord) {
        /*
        SizeToWords has all the words separated into list, based on the length of the word. This
        method goes through the list corresponding to the targetWord length, and finds all the ones
        that have the same sort order, via getSortedLetters. Based on those it then returns all the
        words besides the targetWord.
         */
        List<String> result = new ArrayList<String>();
        String ComparetoThis = getSortedLetters(targetWord);
        for(int i = 0; i < SizeToWords.get(targetWord.length()).size(); i++) {
            if(!isGoodWord(SizeToWords.get(targetWord.length()).get(i), targetWord)) {
                continue;
            }
            String check =  getSortedLetters(SizeToWords.get(targetWord.length()).get(i));
            if(check.equals(ComparetoThis)) {
                result.add(SizeToWords.get(targetWord.length()).get(i));
            }
        }
        return result;
    }
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        /*
        Same as the getAnagrams method, except it goes through the list from SizeToWords that
        contains the words that are one letter bigger than the argument.

        It then goes through the list and ignores all the words that returns false for isGoodWord

        For the words that pass it returns
         */
        ArrayList<String> result = new ArrayList<String>();
        String ComparetoThis = getSortedLetters(word);

        for(int i = 0; i < SizeToWords.get(word.length()+1).size(); i++) {
            if(!isGoodWord(SizeToWords.get(word.length()+1).get(i), word) ) {
                continue;
            }

            //This loop is needed to compare possible anagrams with the word
            //checks char by char to make sure.
            ArrayList<String> OneLetter = SizeToWords.get(word.length() +1);
            for(int a = 0; a < OneLetter.get(i).length(); a++) {
                if(OneLetter.get(i).charAt(a) != word.charAt(a)) {
                    String Letter = "" + OneLetter.get(i).charAt(a); //The Letter that isn't in word for anagrams
                    String[] WithoutLetterArray = OneLetter.get(i).split(Letter, 2);

                    //WithoutLetter recreates the current wordList string without the additional letter.
                    String WithoutLetter = getSortedLetters(WithoutLetterArray[0]+WithoutLetterArray[1]);
                    if(WithoutLetter.equals(ComparetoThis)) {
                        result.add(OneLetter.get(i));
                        break;
                    }else {
                        break;
                    }
                }
            }

        }
        return result;
    }
    public String pickGoodStarterWord() {
        // self-explanatory, randomly decides a word that picks which word to play the game with
        Random randomNumber = new Random();
        while(true) {
            int rn = randomNumber.nextInt(wordList.size()-1);
            if(getAnagramsWithOneMoreLetter(wordList.get(rn)).size() >= 5) {
                return wordList.get(rn);
            }
        }
    }


}
