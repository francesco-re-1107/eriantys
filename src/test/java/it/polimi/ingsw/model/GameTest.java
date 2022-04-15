package it.polimi.ingsw.model;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.charactercards.PostmanCharacterCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @BeforeEach
    void setUp() {
        Utils.LOGGER.setLevel(Level.WARNING);
    }

    @Test
    void testStaticGameTwoPlayers() {
        Game g = new Game(2, true);
        List<Player> players;

        //add player 1
        assertDoesNotThrow(
                () -> g.addPlayer("p1")
        );

        //add player 2 with same nickname of player 1
        assertThrows(
                DuplicatedNicknameException.class,
                () -> g.addPlayer("p1")
        );

        //start the game without enough players
        assertThrows(
                InvalidOperationException.class,
                () -> g.startGame()
        );

        //add player 2
        assertDoesNotThrow(
                () -> g.addPlayer("p2")
        );

        //add another player
        assertThrows(
                InvalidOperationException.class,
                () -> g.addPlayer("_p3")
        );

        players = g.getPlayers();
        assertEquals(2, players.size());

        //game started
        assertEquals(Game.State.CREATED, g.getGameState());
        g.startGame();
        assertEquals(Game.State.STARTED, g.getGameState());

        //start the game again
        assertThrows(
                InvalidOperationException.class,
                () -> g.startGame()
        );

        //check round
        assertEquals(Round.Stage.PLAN, g.getCurrentRound().getStage());
        assertEquals(players.get(0), g.getCurrentRound().getCurrentPlayer());

        //play assistant card player 1 (it's not the current player)
        assertThrows(
                InvalidOperationException.class,
                () -> g.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(1))
        );

        //putStudents in PLAN stage
        assertThrows(
                InvalidOperationException.class,
                () -> g.placeStudents(players.get(0), new StudentsContainer(), new HashMap<>())
        );

        //moveMotherNature in PLAN stage
        assertThrows(
                InvalidOperationException.class,
                () -> g.moveMotherNature(players.get(0), 1)
        );

        //selectCloud in PLAN stage
        assertThrows(
                InvalidOperationException.class,
                () -> g.selectCloud(players.get(0), new StudentsContainer())
        );

        //play assistant card player 0
        g.playAssistantCard(players.get(0), AssistantCard.getDefaultDeck().get(1));
        assertEquals(9, players.get(0).getAssistantCardsLeftCount());

        //play same assistant card for player 1
        assertThrows(
                InvalidOperationException.class,
                () -> g.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(1))
        );

        //play correct assistant card for player 1
        g.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(0));

        assertEquals(Round.Stage.ATTACK, g.getCurrentRound().getStage());

        //player 1 has higher turn priority
        assertEquals(players.get(1), g.getCurrentRound().getCurrentPlayer());

        //trying placing not enough students
        assertThrows(
                InvalidOperationException.class,
                () -> g.placeStudents(players.get(1), new StudentsContainer(), new HashMap<>())
        );

        //place students
        RandomizedStudentsContainer picker =
                new RandomizedStudentsContainer(players.get(1).getEntrance());

        StudentsContainer inSchool = picker.pickManyRandom(1);

        Map<Island, StudentsContainer> inIsland = new HashMap<>();
        inIsland.put(g.getIslands().get(3), picker.pickManyRandom(1));
        inIsland.put(g.getIslands().get(5), picker.pickManyRandom(1));

        g.placeStudents(players.get(1), inSchool, inIsland);

        //check size of entrance, school and island
        assertEquals(4, players.get(1).getEntrance().getSize());
        assertEquals(1, players.get(1).getSchool().getSize());
        assertEquals(2, g.getIslands().get(3).getStudents().getSize());
        assertEquals(2, g.getIslands().get(5).getStudents().getSize());

        //play postman character card
        if (g.getCharacterCards().containsKey("PostmanCharacterCard")) {
            int prevCoins = players.get(1).getCoins();

            g.playCharacterCard(players.get(1), new PostmanCharacterCard());
            assertEquals(prevCoins - 1, players.get(1).getCoins());
            assertEquals(2, g.getCurrentRound().getAdditionalMotherNatureMoves());
        }

        //move mother nature
        assertThrows(
                InvalidOperationException.class,
                () -> g.moveMotherNature(players.get(1), 10)
        );
        int prevPosition = g.getMotherNaturePosition();
        g.moveMotherNature(players.get(1), 1);
        assertEquals(prevPosition + 1, g.getMotherNaturePosition());

        //select cloud for player 1
        //non-existing cloud
        assertThrows(
                InvalidOperationException.class,
                () -> g.selectCloud(
                        players.get(1),
                        new StudentsContainer()
                                .addStudents(Student.BLUE, 3)
                                .addStudents(Student.RED, 2)
                )
        );
        //correct cloud
        g.selectCloud(players.get(1), g.getCurrentRound().getClouds().get(0));
        assertEquals(7, players.get(1).getEntrance().getSize());

        //next turn
        assertEquals(players.get(0), g.getCurrentRound().getCurrentPlayer());
        assertEquals(Round.Stage.ATTACK, g.getCurrentRound().getStage());

        //place students
        picker = new RandomizedStudentsContainer(players.get(0).getEntrance());
        inSchool = picker.pickManyRandom(3);
        inIsland = new HashMap<>();
        g.placeStudents(players.get(0), inSchool, inIsland);

        //move mother nature
        g.moveMotherNature(players.get(0), 1);

        //select cloud
        g.selectCloud(players.get(0), g.getCurrentRound().getClouds().get(0));

        assertEquals(Round.Stage.PLAN, g.getCurrentRound().getStage());
        //plays first the one who played the lowest card before
        assertEquals(players.get(1), g.getCurrentRound().getCurrentPlayer());

        //play two times the same card
        assertThrows(
                InvalidOperationException.class,
                () -> g.playAssistantCard(players.get(1), AssistantCard.getDefaultDeck().get(0))
        );
    }


    /**
     * This method creates a completely random game and brings it all the way to the end
     * in order to test different endings and different character cards.
     * This will be repeated 500 times to be sure that all parts of the code will be tested at least once.
     *
     * This test DOESN'T check whether the methods called do what they claim to do.
     */
    @RepeatedTest(500)
    void testDynamicGame() {
        Random r = new Random();

        Game g = new Game(r.nextInt(2,4), r.nextBoolean());

        int pCounter = 0;
        try {
            do {
                g.addPlayer("p" + pCounter);
                pCounter++;
            }while(g.getCurrentNumberOfPlayers() < g.getNumberOfPlayers());
        }catch (DuplicatedNicknameException e){}

        g.startGame();

        while(g.getGameState() != Game.State.FINISHED){

            //stage PLAN
            while(g.getCurrentRound().getStage() == Round.Stage.PLAN){
                Player currentPlayer = g.getCurrentRound().getCurrentPlayer();

                boolean playedCard = false;

                while(!playedCard) {
                    AssistantCard card;
                    do {
                        card = AssistantCard.getDefaultDeck().get(r.nextInt(10));
                    }
                    while (!currentPlayer.canPlayAssistantCard(card));

                    try{
                        g.playAssistantCard(currentPlayer, card);
                        playedCard = true;
                    }catch (InvalidOperationException e){}
                }
            }

            //stage ATTACK
            while(g.getCurrentRound().getStage() == Round.Stage.ATTACK) {
                Player currentPlayer = g.getCurrentRound().getCurrentPlayer();

                //2 random students in school and 1 on a random island
                RandomizedStudentsContainer picker =
                        new RandomizedStudentsContainer(currentPlayer.getEntrance());

                StudentsContainer inSchool = picker.pickManyRandom(2);

                Map<Island, StudentsContainer> inIsland = new HashMap<>();
                int studentsToPutOnIsland = g.getNumberOfPlayers() == 2 ? 1 : 2;
                inIsland.put(g.getIslands().get(r.nextInt(g.getIslands().size())), picker.pickManyRandom(studentsToPutOnIsland));

                g.placeStudents(currentPlayer, inSchool, inIsland);

                //play character card randomly
                //TODO: implement

                //move mother nature randomly
                int allowedMoves = g.getCurrentRound().getCardPlayedBy(currentPlayer).get().motherNatureMaxMoves() +
                            g.getCurrentRound().getAdditionalMotherNatureMoves();
                g.moveMotherNature(currentPlayer, 1 + r.nextInt(allowedMoves));

                //select cloud randomly
                List<StudentsContainer> clouds = g.getCurrentRound().getClouds();
                g.selectCloud(currentPlayer, clouds.get(r.nextInt(clouds.size())));
            }
        }

        assertNotNull(g.getWinner().get());
    }
}