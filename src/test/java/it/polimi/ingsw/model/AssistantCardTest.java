package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssistantCardTest {

    @Test
    void getters() {
        AssistantCard assistantCard = new AssistantCard(2, 4, 3);
        assertEquals(2, assistantCard.getId());
        assertEquals(4, assistantCard.getMotherNatureMaxMoves());
        assertEquals(3, assistantCard.getTurnPriority());
    }

    @Test
    void getDefaultDeck() {
        List<AssistantCard> list = AssistantCard.getDefaultDeck();

        // list length after the creation must be 10
        assertEquals(10, list.size());
    }


}