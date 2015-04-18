package com.shankstravis.myflashcards.deck;

/**
 * Created by Travis on 3/16/2015.
 */
public class StudyMode {
    public final static int IN_ORDER_MODE = 0;
    public final static int RANDOM_MODE = 1;
    private static int mPosition;

    private StudyMode(){}

    public static void increasePosition(){ mPosition++; }
    public static void setPosition(int position){ mPosition = position; }
    public static int getPosition(){ return mPosition; }
    public static void resetPosition(){ mPosition = 0; }


}
