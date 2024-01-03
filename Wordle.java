/*
    Calvin Rodrigue
    5/10/22

    Wordle class contains main code to run and execute a
    repeating animation of generating and solving a 
    5-letter word from a data set of allowed words.
    The data set of allowed words contains more words 
    than the set of possible answers because the answers are
    only common words.
*/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Wordle {
    public static int counter = 0;
    public static void main(String[] args) {
        //create new window
        WordleWindow w = new WordleWindow();

        while (true) {
            ArrayList<String> answers = new ArrayList<String>();
            ArrayList<String> guesses = new ArrayList<String>();

            try { //get list of allowed guess
                File file = new File("wordle-nyt-allowed-guesses.txt");
                Scanner scan = new Scanner(file);
                while (scan.hasNext()) {
                    guesses.add(scan.nextLine());
                }
                scan.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try { //get list of allowed answers
                File file = new File("wordle-nyt-answers-alphabetical.txt");
                Scanner scan = new Scanner(file);
                while (scan.hasNext()) {
                    String thisLine = scan.nextLine();
                    answers.add(thisLine);
                    guesses.add(thisLine);
                }
                scan.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            String theAnswer = answers.get((int) (Math.random() * answers.size()));
            System.out.println(theAnswer);

            String currentGuess = Parser.getTop100().get((int) (Math.random() * 100));

            //Old code to solve the wordle with user input

            // System.out.println("Your first guess should be: " + currentGuess);
            // System.out.println("Type 0 for a gray letter, 1 for a yellow letter, and 2 for a green letter.");

            // Scanner in = new Scanner(System.in); //get info about guess result
            // String result = in.nextLine();
            // int[] guessInfo = new int[5];
            // for (int r = 0; r < 5; r++) {
            //     guessInfo[r] = Integer.parseInt(result.substring(r, r + 1));
            // }


            Guess guessResult = new Guess(currentGuess.toCharArray(), theAnswer);
            w.addGuess(guessResult);
            
            while (w.isAnimating()) { //wait for animation to finish
                System.out.print("");
            }

            System.out.println(guessResult);
            for (int e = answers.size() - 1; e >= 0; e--) { //eliminate impossible answers
                if (!guessResult.isValid(answers.get(e))) {
                    answers.remove(e);
                }
            }

            while (true) { //game loop
                
                //get best nextguess
                double calculated = 0;
                String bestWord = "";
                double bestElims = 0;
                for (String guess : guesses) { //for each possible guess
                    int elims = 0;
                    
                    for (String answer : answers) { //for each possible answer
                        Guess g = new Guess(guess.toCharArray(), answer);
                        for (String word : answers) { //see how many it eliminates
                            if (!g.isValid(word)) { //if eliminates
                                elims++;
                            }
                        }
                    }
                    calculated++;
                    w.setCalcPercent(calculated / guesses.size());
                    if (elims >= bestElims) { //get most effective predicted word
                        bestElims = elims;
                        if (elims == bestElims) {
                            if (Math.random() > 0.3) {
                                bestWord = guess;
                            }
                        }
                        else {
                            bestWord = guess;
                        }
                    }
                }
                w.setCalcPercent(1.0);
                System.out.println("Your next guess should be: " + bestWord);

                currentGuess = bestWord; //guess that word

                //Old code to solve the wordle with user input

                // result = in.nextLine();
                // guessInfo = new int[5]; //make new guess with the next word
                // for (int r = 0; r < 5; r++) {
                //     guessInfo[r] = Integer.parseInt(result.substring(r, r + 1));
                // }

                if (answers.size() > 1) { //displays guess on window
                    guessResult = new Guess(currentGuess.toCharArray(), theAnswer);
                    w.addGuess(guessResult);
                    while (w.isAnimating()) { //wait for animation to finish
                        System.out.print("");
                    }
                }

                System.out.println(guessResult);
                for (int e = answers.size() - 1; e >= 0; e--) { //eliminate answers with given info
                    if (!guessResult.isValid(answers.get(e))) {
                        answers.remove(e);
                    }
                }

                if (answers.size() == 1) { //return last answer left in list
                    currentGuess = answers.get(0);
                    System.out.println("The word is " + currentGuess);
                    w.addGuess(new Guess(currentGuess.toCharArray(), theAnswer));
                    break;
                }
                if (answers.size() == 0) { //if word is for some reason not found
                    System.out.println("The word is " + "not found");
                    break;
                }
            }

            long delay = System.currentTimeMillis() + 7000; //wait for animation to finish and pause for a bit
            while (System.currentTimeMillis() < delay) {}
            w.clearGuesses();
        }
    }
}