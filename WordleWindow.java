/*
    Calvin Rodrigue
    5/10/22

    WordleWindow class creates new JFrame that
    acts as the window for displaying the main
    program.
*/

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.util.ArrayList;

public class WordleWindow extends JFrame {
    //instance variables
    private ArrayList<Guess> guesses;
    private boolean isAnimated;
    private double calcPercent;
    //constants
    private static final int TOP_LEFT_X = 500;
    private static final int TOP_LEFT_Y = 100;
    private static final int LETTER_SPACING = 10;
    private static final int BOX_SIZE = 100;
    private static final int LETTER_PADDING = 18;
    private static final long TYPE_DELAY = 200;

    public static void main(String[] args) {
        //make new window and add test guess
        WordleWindow window = new WordleWindow();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new DimensionUIResource(1920, 1080));
        window.pack();
        window.setVisible(true);
        window.addGuess(new Guess("roate".toCharArray(), "rated"));
    }

    //constructor
    public WordleWindow() {
        super("Wordle Solver");
        guesses = new ArrayList<Guess>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new DimensionUIResource(1920, 1080));
        pack();
        setVisible(true);
    }

    //update display
    public void paint(Graphics g) {
        
        g.setFont(new Font("OCR A Extended", 10, BOX_SIZE));

        if (calcPercent > 0) {
            g.setColor(Color.BLACK);
            g.drawRect(TOP_LEFT_X, TOP_LEFT_Y + 6 * (BOX_SIZE + LETTER_SPACING), 
                5 * BOX_SIZE + 4 * LETTER_SPACING, LETTER_SPACING);
            g.fillRect(TOP_LEFT_X, TOP_LEFT_Y + 6 * (BOX_SIZE + LETTER_SPACING), 
                (int) ((5 * BOX_SIZE + 4 * LETTER_SPACING) * calcPercent), LETTER_SPACING);
        }
        else {
            //clear calculating bar
            g.setColor(Color.WHITE);
            g.fillRect(TOP_LEFT_X, TOP_LEFT_Y + 6 * (BOX_SIZE + LETTER_SPACING), 
                5 * BOX_SIZE + 4 * LETTER_SPACING + 1, LETTER_SPACING + 1);
            //draw empty grid
            if (guesses.size() == 0) { //if guesses is cleared
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            g.setColor(Color.BLACK);
            for (int i = 0; i < 6; i++) {
                int startY = TOP_LEFT_Y + (BOX_SIZE + LETTER_SPACING) * i;
                for (int c = 0; c < 5; c++) {
                    int startX = TOP_LEFT_X + (BOX_SIZE + LETTER_SPACING) * c;
                    g.drawRect(startX, startY, BOX_SIZE, BOX_SIZE);
                }
            }

            for (int i = 0; i < 6; i++) { //for each row

                int startY = TOP_LEFT_Y + (BOX_SIZE + LETTER_SPACING) * i;

                if (guesses.size() > i) { //if there is guess to print

                    Guess thisguess = guesses.get(i);

                    if (isAnimated && i == guesses.size() - 1) { //supposed to animate
                        g.setColor(Color.BLACK);
                        for (int c = 0; c < 5; c++) { //type out the black letters
                            char thisletter = thisguess.getGuess()[c];
                            int startX = TOP_LEFT_X + (BOX_SIZE + LETTER_SPACING) * c;

                            pause(TYPE_DELAY);

                            g.drawRect(startX, startY, BOX_SIZE, BOX_SIZE);
                            g.drawString(Character.toUpperCase(thisletter) + "", startX + LETTER_PADDING, startY + BOX_SIZE - LETTER_PADDING);
                        }
                        for (int c = 0; c < 5; c++) { //reveal the letters color
                            int thisresult = thisguess.getResult()[c];
                            char thisletter = thisguess.getGuess()[c];
                            int startX = TOP_LEFT_X + (BOX_SIZE + LETTER_SPACING) * c;

                            //get correct color
                            Color resultColor;

                            if (thisresult == 2) {
                                resultColor = new Color(100, 200, 100);
                            }
                            else if (thisresult == 1) {
                                resultColor = new Color(200, 200, 100);
                            }
                            else {
                                resultColor = new Color(200, 200, 200);
                            }
                            
                            int frames = 10;
                            g.setColor(Color.WHITE);
                            for (int f = 0; f <= frames; f++) { //mask black letters
                                int maskHeight = (int) (BOX_SIZE / 2.0 / frames * f);
                                //top
                                g.fillRect(startX, startY, BOX_SIZE, maskHeight);
                                //bottom
                                g.fillRect(startX, startY + BOX_SIZE - maskHeight, BOX_SIZE, maskHeight);

                                pause(TYPE_DELAY / frames);
                            }
                            pause(TYPE_DELAY / 2);
                            for (int f = frames; f >= 0; f--) { //reveal colored letters
                                g.setColor(resultColor);
                                g.fillRect(startX, startY, BOX_SIZE, BOX_SIZE);

                                g.setColor(Color.WHITE);
                                g.drawString(Character.toUpperCase(thisletter) + "", startX + LETTER_PADDING, startY + BOX_SIZE - LETTER_PADDING);
                            
                                int maskHeight = (int) (BOX_SIZE / 2.0 / frames * f);
                                //top
                                g.fillRect(startX, startY, BOX_SIZE, maskHeight);
                                //bottom
                                g.fillRect(startX, startY + BOX_SIZE - maskHeight, BOX_SIZE, maskHeight);

                                pause(TYPE_DELAY / frames);
                            }
                            pause(TYPE_DELAY);
                            g.setColor(resultColor);
                            g.fillRect(startX, startY, BOX_SIZE, BOX_SIZE);

                            g.setColor(Color.WHITE);
                            g.drawString(Character.toUpperCase(thisletter) + "", startX + LETTER_PADDING, startY + BOX_SIZE - LETTER_PADDING);
                            
                        }
                        isAnimated = false;
                    }
                    else {
                        for (int c = 0; c < 5; c++) {
                            int thisresult = thisguess.getResult()[c];
                            char thisletter = thisguess.getGuess()[c];
                            int startX = TOP_LEFT_X + (BOX_SIZE + LETTER_SPACING) * c;
                            //get correct color
                            if (thisresult == 2) {
                                g.setColor(new Color(100, 200, 100));
                            }
                            else if (thisresult == 1) {
                                g.setColor(new Color(200, 200, 100));
                            }
                            else if (thisresult == 0) {
                                g.setColor(new Color(200, 200, 200));
                            }

                            g.fillRect(startX, startY, BOX_SIZE, BOX_SIZE);

                            g.setColor(Color.WHITE);
                            g.drawString(Character.toUpperCase(thisletter) + "", startX + LETTER_PADDING, startY + BOX_SIZE - LETTER_PADDING);
                        }
                        g.setColor(new Color(255, 255, 255));
                    }
                }
                else { //print empty boxes
                    g.setColor(Color.BLACK);
                    for (int c = 0; c < 5; c++) {
                        int startX = TOP_LEFT_X + (BOX_SIZE + LETTER_SPACING) * c;
                        g.drawRect(startX, startY, BOX_SIZE, BOX_SIZE);
                    }
                }
            }
        }
    }

    //adds a guess to the list
    public void addGuess(Guess g) {
        guesses.add(g);
        isAnimated = true;
        calcPercent = 0;
        repaint();
    }

    //resets the list
    public void clearGuesses() {
        guesses = new ArrayList<Guess>();
        repaint();
    }

    //updates percent calculating complete
    public void setCalcPercent(double percent) {
        calcPercent = percent;
        repaint();
    }

    //accessor if animation is playing
    public boolean isAnimating() {
        return isAnimated;
    }

    //pauses the program to allow animation to play
    public void pause(long delay) {
        long endTime = System.currentTimeMillis() + delay;
        while (endTime > System.currentTimeMillis()) {}
    }
}
