package gamerzdisease.com.flashcards.deck;

/**
 * Created by Travis on 3/14/2015.
 */
public class Grade {

    private static int mNumCorrect;

    private Grade(){}

    public static int getNumCorrect(){
        return mNumCorrect;
    }

    public static void setNumCorrect(int correct) { mNumCorrect = correct; }

    public static void increaseNumCorrect(){
        mNumCorrect++;
    }

    public static double calculateGrade(int sizeOfDeck){
        return 100.0 * mNumCorrect / sizeOfDeck;
    }

    public static void resetCorrect() {mNumCorrect = 0;}
}
