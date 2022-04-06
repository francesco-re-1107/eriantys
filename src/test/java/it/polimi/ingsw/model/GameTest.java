package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.PostmanCharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game g1;

    //TODO: test game always with the same custom random seed otherwise test for example 100 games every time

    @Test
    void testGame1() {
        g1 = new Game(2, true);
        List<Player> players;

        //add player 1
        assertDoesNotThrow(
                () -> g1.addPlayer("p1")
        );

        //add player 2 with same nickname of player 1
        assertThrows(
                DuplicatedNicknameException.class,
                () -> g1.addPlayer("p1")
        );

        //start the game without enough players
        assertThrows(
                InvalidOperationException.class,
                () -> g1.startGame()
        );

        //add player 2
        assertDoesNotThrow(
                () -> g1.addPlayer("p2")
        );

        //add another player
        assertThrows(
                InvalidOperationException.class,
                () -> g1.addPlayer("_p3")
        );

        players = g1.getPlayers();
        assertEquals(2, players.size());

        //game started
        assertEquals(Game.State.CREATED, g1.getGameState());
        g1.startGame();
        assertEquals(Game.State.STARTED, g1.getGameState());

        //start the game again
        assertThrows(
                InvalidOperationException.class,
                () -> g1.startGame()
        );

        //check round
        assertEquals(Round.Stage.PLAN, g1.getCurrentRound().getStage());
        assertEquals(players.get(0), g1.getCurrentRound().getCurrentPlayer());

        //play assistant card player 0
        g1.playAssistantCard(players.get(0), AssistantCard.getDefaultDeck().get(1));
        assertEquals(9, players.get(0).getAssistantCardsLeftCount());

        //play same assistant card for player 1
        assertThrows(
                InvalidOperationException.class,
                () -> g1.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(1))
        );

        //play correct assistant card for player 1
        g1.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(0));

        assertEquals(Round.Stage.ATTACK, g1.getCurrentRound().getStage());

        //player 1 has higher turn priority
        assertEquals(players.get(1), g1.getCurrentRound().getCurrentPlayer());

        //put students
        RandomizedStudentsContainer picker =
                new RandomizedStudentsContainer(players.get(1).getEntrance());

        StudentsContainer inSchool = picker.pickManyRandom(1);

        Map<Island, StudentsContainer> inIsland = new HashMap<>();
        inIsland.put(g1.getIslands().get(3), picker.pickManyRandom(1));
        inIsland.put(g1.getIslands().get(5), picker.pickManyRandom(1));

        g1.putStudents(players.get(1), inSchool, inIsland);

        //check size of entrance, school and island
        assertEquals(4, players.get(1).getEntrance().getSize());
        assertEquals(1, players.get(1).getSchool().getSize());
        assertEquals(2, g1.getIslands().get(3).getStudents().getSize());
        assertEquals(2, g1.getIslands().get(5).getStudents().getSize());

        //play postman character card
        if (g1.getCharacterCards().containsKey("PostmanCharacterCard")) {
            g1.playCharacterCard(players.get(1), new PostmanCharacterCard());
            assertEquals(0, players.get(1).getCoins());
            assertEquals(2, g1.getCurrentRound().getAdditionalMotherNatureMoves());
        }

        //move mother nature
        assertThrows(
                InvalidOperationException.class,
                () -> g1.moveMotherNature(players.get(1), 10)
        );
        int prevPosition = g1.getMotherNaturePosition();
        g1.moveMotherNature(players.get(1), 1);
        assertEquals(prevPosition + 1, g1.getMotherNaturePosition());

        //select cloud for player 1
        //non-existing cloud
        assertThrows(
                InvalidOperationException.class,
                () -> g1.selectCloud(
                        players.get(1),
                        new StudentsContainer()
                                .addStudents(Student.BLUE, 3)
                                .addStudents(Student.RED, 2)
                )
        );
        //correct cloud
        g1.selectCloud(players.get(1), g1.getCurrentRound().getClouds().get(0));
        assertEquals(7, players.get(1).getEntrance().getSize());

        //next turn
        assertEquals(players.get(0), g1.getCurrentRound().getCurrentPlayer());
        assertEquals(Round.Stage.ATTACK, g1.getCurrentRound().getStage());

        //put students
        picker = new RandomizedStudentsContainer(players.get(0).getEntrance());
        inSchool = picker.pickManyRandom(3);
        inIsland = new HashMap<>();
        g1.putStudents(players.get(0), inSchool, inIsland);

        //move mother nature
        g1.moveMotherNature(players.get(0), 1);

        //select cloud
        g1.selectCloud(players.get(0), g1.getCurrentRound().getClouds().get(0));

        assertEquals(Round.Stage.PLAN, g1.getCurrentRound().getStage());
        //plays first the one who played the lowest card before
        assertEquals(players.get(1), g1.getCurrentRound().getCurrentPlayer());

        //play assistant card player 1
        g1.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(5));

        //play assistant card player 0
        g1.playAssistantCard(players.get(0), AssistantCard.getDefaultDeck().get(4));

        //put students player 0
        picker = new RandomizedStudentsContainer(players.get(0).getEntrance());
        inSchool = picker.pickManyRandom(3);
        inIsland = new HashMap<>();
        g1.putStudents(players.get(0), inSchool, inIsland);

        //move mother nature player 0
        g1.moveMotherNature(players.get(0), 3);

        //select cloud player 0
        g1.selectCloud(players.get(0), g1.getCurrentRound().getClouds().get(0));

        //put students player 1
        picker = new RandomizedStudentsContainer(players.get(1).getEntrance());
        inSchool = picker.pickManyRandom(3);
        inIsland = new HashMap<>();
        g1.putStudents(players.get(1), inSchool, inIsland);

        //move mother nature player 1
        g1.moveMotherNature(players.get(1), 2);

        //select cloud player 1
        g1.selectCloud(players.get(1), g1.getCurrentRound().getClouds().get(0));

    }
}