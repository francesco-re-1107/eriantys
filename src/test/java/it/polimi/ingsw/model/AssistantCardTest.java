package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AssistantCardTest extends TestCase {


    @Test

      void  TestGetMethods() {
        AssistantCard assistantCard = new AssistantCard(2,4,3 );
        assertEquals(2, assistantCard.getId());
        assertEquals(4, assistantCard.getMotherNatureMaxMoves());
        assertEquals(3, assistantCard.getTurnPriority());
    }


    @Test
    // test the right creation of a deck with 10 elements
    public void testGetDefaultDeck() {
        List<AssistantCard> list1 = AssistantCard.getDefaultDeck();
        List<AssistantCard> list2 = AssistantCard.getDefaultDeck();



        // list lenght after the creation must be 10//
        assertEquals(10, list2.size());
        assertEquals(10, list1.size());


    }


}