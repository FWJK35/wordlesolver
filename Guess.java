/*
    Calvin Rodrigue
    5/10/22

    Guess class contains main methods and type
    for logic of wordle. Includes the two
    necessary constructors, as well as required
    accessor methods. Also contains methods to check
    validity of that guess on a word
*/

public class Guess {
    private char[] guess;
    private int[] result;

    public Guess(char[] guess, String answer) { //makes set of green, yellow, and gray letters based on answer
        this.guess = guess;
        this.result = new int[5];
        for (int c = 0; c < 5; c++) {
            if (answer.charAt(c) == 
            guess[c]) { //if letter is in right spot
                result[c] = 2;
            }
        }

        for (int c = 0; c < 5; c++) { //for each character
            if (answer.indexOf(guess[c]) >= 0) { //letter is in word
                if (result[c] != 2) {
                    int countInAnswer = 0;
                    int countInGuess = 0;
                    int indexInGuess = 0;
                    for (int i = 0; i < 5; i++) {
                        countInAnswer += (answer.charAt(i) == guess[c] && result[i] != 2 ? 1 : 0);
                        countInGuess += (guess[i] == guess[c] && result[i] != 2 ? 1 : 0);
                        indexInGuess = (i == c ? countInGuess : indexInGuess);
                    }
                    result[c] = (indexInGuess <= countInAnswer ? 1 : 0);
                }
            }
        }
        //System.out.println(Arrays.toString(result));
    }

    public char[] getGuess() {
        return this.guess;
    }
    public int[] getResult() {
        return this.result;
    }

    public boolean isValid(String test) { //checks if a word is valid with a given guess and info
        for (int c = 0; c < 5; c++) { //for each letter

            int countInTest = 0;
            int countInGuess = 0;
            for (int i = 0; i < 5; i++) { //count number of that char in guess and check
                countInTest += (test.charAt(i) == guess[c] ? 1 : 0);
                countInGuess += (guess[i] == guess[c] ? 1 : 0);
            }

            if (this.result[c] == 2 && guess[c] != test.charAt(c)) { //is not in that spot
                //System.out.println("2");
                return false;
            }
            if (this.result[c] == 0 && countInGuess <= countInTest && test.indexOf(guess[c]) >= 0) { //wrong letter is in word
                //System.out.println("0");
                return false;
            }
            if (this.result[c] == 1 && (test.indexOf(guess[c]) < 0 || guess[c] == test.charAt(c))) { //wrong number of yellow
                //System.out.println("1");
                return false;
            }
        }
        return true;
    }

    public String toString() { //print guess colored
        String out = "";
        for (int c = 0; c < 5; c++) {
            if (result[c] == 0) {
                out += "\u001B[37m";
            }
            if (result[c] == 1) {
                out += "\u001B[33m";
            }
            if (result[c] == 2) {
                out += "\u001B[32m";
            }
            out += guess[c];
        }
        out += "\u001B[0m";
        return out; //]]]] fix bracket coloring
    }
}
