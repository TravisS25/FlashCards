package gamerzdisease.com.flashcards.deck;

/**
 * Created by Travis on 3/16/2015.
 */
public class StudyMode {
    public final static int LINEAR_MODE = 0;
    public final static int RANDOM_MODE = 1;
    private static int sPosition;

    private StudyMode(){}

    public static void increasePosition(){ sPosition++; }
    public static int getPosition(){ return sPosition; }
    public static void resetPosition(){ sPosition = 0; }


}
