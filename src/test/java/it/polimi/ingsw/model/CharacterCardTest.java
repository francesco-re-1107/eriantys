package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void cardsSubclasses (){
        CentaurCharacterCard c1 = new CentaurCharacterCard();
        assertNotNull(c1.getInfluenceCalculator());

        FarmerCharacterCard c2 = new FarmerCharacterCard(null);
        assertNotNull(c2.getInfluenceCalculator());

        GrandmaCharacterCard c3 = new GrandmaCharacterCard(new Island());
        assertNotNull(c3.getIsland());

        HeraldCharacterCard c4 = new HeraldCharacterCard(new Island());
        assertNotNull(c4.getIsland());

        KnightCharacterCard c5 = new KnightCharacterCard(new Player("p1", Tower.BLACK, 8, new StudentsContainer()));
        assertNotNull(c5.getInfluenceCalculator());

        MinstrelCharacterCard c6 = new MinstrelCharacterCard(new StudentsContainer(), new StudentsContainer());
        assertNotNull(c6.getStudentsToAdd());
        assertNotNull(c6.getStudentsToRemove());

        MushroomManCharacterCard c7 = new MushroomManCharacterCard(Student.BLUE);
        assertNotNull(c7.getInfluenceCalculator());

        PostmanCharacterCard c8 = new PostmanCharacterCard();
        c8.getAdditionalMotherNatureMoves();
    }
}