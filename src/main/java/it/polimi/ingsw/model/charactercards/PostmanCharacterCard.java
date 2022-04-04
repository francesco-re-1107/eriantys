package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;

public class PostmanCharacterCard extends CharacterCard {

    private static final int ADDITIONAL_MOTHER_NATURE_MOVES = 2;

    public PostmanCharacterCard() {
        super(1);
    }

    public int getAdditionalMotherNatureMoves(){
        return ADDITIONAL_MOTHER_NATURE_MOVES;
    }
}
