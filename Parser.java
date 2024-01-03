/*
    Calvin Rodrigue
    5/10/22

    Parser class goes through previously
    generated list of effectiveness of all
    words as a starting word, and returns a
    list of the best 100 starting words.
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;
import java.io.File;
import java.io.IOException;

public class Parser {
    public static void main(String[] args) {
        System.out.println(getTop100());

    }

    public static ArrayList<String> getTop100() {
        ArrayList<String> result = new ArrayList<String>();
        HashMap<String, Integer> results = new HashMap<String, Integer>();

        try { //scan file containing guess effectiveness
            File file = new File("guessresults.txt");
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String thisLine = scan.nextLine();
                int score = Integer.parseInt(thisLine.substring(6));
                results.put(thisLine.substring(0, 5), score);
            }
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> top = new HashMap<String, Integer>();
        for (Entry<String, Integer> e: results.entrySet()) {
            if (top.size() < 100) { //add first 100 words
                top.put(e.getKey(), e.getValue());
            }
            else {
                int minScore = results.get("roate");
                String minScoreWord = "";
                for (String t: top.keySet()) { //find worst word currently in top
                    if (top.get(t) > minScore) {
                        minScore = top.get(t);
                        minScoreWord = t;
                    }
                }
                if (e.getValue() < minScore) { //if this word is better replace the worst
                    top.replace(minScoreWord, 0);
                    top.put(e.getKey(), e.getValue());
                }
            }
        }
        for (String s : top.keySet()) { //return list of best starters
            if (top.get(s) != 0) result.add(s);
        }
        return result;
    }
}
