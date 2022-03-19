package it.polimi.ingsw.model;

import java.util.ArrayList;

public abstract class CharacterCard {

    private int cost;

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

    public ArrayList<CharacterCard> generateRandomCards(int howMany) {
        ArrayList<CharacterCard> deck = new ArrayList<CharacterCard>();

        for(int i = 0; i < howMany; i++){
            //generate random number and pick a card
        }

        return deck;
    }
}
