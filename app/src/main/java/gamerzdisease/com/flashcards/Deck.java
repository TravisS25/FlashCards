package gamerzdisease.com.flashcards;

/**
 * Created by Travis on 1/31/2015.
 */
import java.io.Serializable;
import java.util.Dictionary;

public class Deck implements Serializable {

    private String name;
    private Dictionary<String, String> card;

    public Deck(){}

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Dictionary<String, String> getCard(){
        return this.card;
    }

    public void setCard(String key, String value){
        this.card.put(key, value);
    }
}
