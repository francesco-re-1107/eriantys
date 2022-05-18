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

    @Test
    void cardsSubclasses (){
        /*CentaurCharacterCard c1 = new CentaurCharacterCard();
        assertNotNull(c1.getInfluenceCalculator(null));

        FarmerCharacterCard c2 = new FarmerCharacterCard();
        assertNotNull(c2.getInfluenceCalculator(null));

        GrandmaCharacterCard c3 = new GrandmaCharacterCard(new Island());
        assertNotNull(c3.getIsland());

        HeraldCharacterCard c4 = new HeraldCharacterCard(new Island());
        assertNotNull(c4.getIsland());

        KnightCharacterCard c5 = new KnightCharacterCard();
        assertNotNull(c5.getInfluenceCalculator(null));

        MinstrelCharacterCard c6 = new MinstrelCharacterCard(new StudentsContainer(), new StudentsContainer());
        assertNotNull(c6.getStudentsToAdd());
        assertNotNull(c6.getStudentsToRemove());

        MushroomManCharacterCard c7 = new MushroomManCharacterCard(Student.BLUE);
        assertNotNull(c7.getInfluenceCalculator(null));

        PostmanCharacterCard c8 = new PostmanCharacterCard();
        c8.getAdditionalMotherNatureMoves();*/
    }
}