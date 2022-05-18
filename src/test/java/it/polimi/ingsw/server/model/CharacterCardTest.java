package it.polimi.ingsw.server.model;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.server.model.charactercards.CentaurCharacterCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacterCardTest {

    @Test
    void generateRandomDeck() {
        List<String> d = CharacterCard.generateRandomDeck(6);
        assertEquals(6, d.size());

        assertThrows(
                InvalidOperationException.class,
                () -> CharacterCard.generateRandomDeck(16)
        );
    }

    @Test
    void getCost() {
        assertEquals(3, new CentaurCharacterCard().getCost());
        assertEquals(4, new CentaurCharacterCard().getCost(2));
    }

    @Test
    void getName() {
        assertEquals("CentaurCharacterCard", new CentaurCharacterCard().getName());
    }
}