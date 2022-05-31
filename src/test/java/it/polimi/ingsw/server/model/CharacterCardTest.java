package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import it.polimi.ingsw.server.model.charactercards.CentaurCharacterCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.polimi.ingsw.server.model.Character.CENTAUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacterCardTest {

    @Test
    void generateRandomDeck() {
        List<Character> d = Character.generateRandomDeck(6);
        assertEquals(6, d.size());

        assertThrows(
                InvalidOperationError.class,
                () -> Character.generateRandomDeck(16)
        );
    }

    @Test
    void getCost() {
        assertEquals(3, CENTAUR.getCost());
        assertEquals(4, CENTAUR.getCost(3));
    }

    @Test
    void getCharacter() {
        assertEquals(CENTAUR, new CentaurCharacterCard().getCharacter());
    }
}