package it.polimi.ingsw.model;

import it.polimi.ingsw.model.charactercards.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class CharacterCard {

    private final int cost;

    private final static  Random random = new Random();

    private int used = 0;

    public CharacterCard(int cost) {
        this.cost = cost;
    }

    public void incrementUsedCounter(){
        this.used++;
    }

    public int getCost() {
        return cost + (used == 0 ? 0 : 1);
    }

    public static ArrayList<CharacterCard> generateRandomCards(int howMany) {
        ArrayList<CharacterCard> deck = new ArrayList<>();

        for(int i = 0; i < howMany; i++){
            //generate random number and pick a card
            switch (random.nextInt(8)){
                case 0:
                    deck.add(new MonkCharacterCard());
                    break;
                case 1:
                    deck.add(new FarmerCharacterCard());
                    break;
                case 2:
                    deck.add(new HeraldCharacterCard());
                    break;
                case 3:
                    deck.add(new PostmanCharacterCard());
                    break;
                case 4:
                    deck.add(new GrandmaCharacterCard());
                    break;
                case 5:
                    deck.add(new CentaurCharacterCard());
                    break;
                case 6:
                    deck.add(new JesterCharacterCard());
                    break;
                case 7:
                    deck.add(new KnightCharacterCard());
                    break;
            }
        }

        return deck;
    }
}
