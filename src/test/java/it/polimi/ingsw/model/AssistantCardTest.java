package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssistantCardTest {

    @Test
    void getters() {
        AssistantCard assistantCard = AssistantCard.getDefaultDeck().get(0);

        //assertEquals(0, assistantCard.getId());
        assertEquals(1, assistantCard.getMotherNatureMaxMoves());
        assertEquals(1, assistantCard.getTurnPriority());
    }

    @Test
    void getDefaultDeck() {
        List<AssistantCard> list = AssistantCard.getDefaultDeck();

        // list length after the creation must be 10
        assertEquals(10, list.size());
    }


}