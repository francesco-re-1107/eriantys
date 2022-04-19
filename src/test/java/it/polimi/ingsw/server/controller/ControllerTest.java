package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.common.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.common.exceptions.GameJoiningError;
import it.polimi.ingsw.common.exceptions.NicknameNotRegisteredError;
import it.polimi.ingsw.common.exceptions.NicknameNotValidException;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    Controller c;
    VirtualView vw;

    @BeforeEach
    void setUp() {
        c = new Controller();
        vw = new VirtualView(null, null);
    }

    @Test
    void registerNickname() {
        //nickname not valid
        assertThrows(
                NicknameNotValidException.class,
                () -> c.registerNickname("", vw)
        );

        assertDoesNotThrow(
                () -> c.registerNickname("p1", vw)
        );

        //nickname not valid
        assertThrows(
                DuplicatedNicknameException.class,
                () -> c.registerNickname("p1", vw)
        );

        //emulate call to the onDisconnect callback
        vw.onDisconnect();

        //client not in a game reconnection
        assertDoesNotThrow(
                () -> assertNull(c.registerNickname("p1", vw))
        );

        c.createGame("p1", 2, false);

        //client in a game reconnection
        assertDoesNotThrow(
                () -> assertNotNull(c.registerNickname("p1", vw))
        );

    }

    @Test
    void joinGame() {
        assertDoesNotThrow(
                () -> {
                    c.registerNickname("p1", vw);
                    c.registerNickname("p2", vw);
                    c.registerNickname("p3", vw);
                }
        );

        //not existing game
        assertThrows(
                GameJoiningError.class,
                () -> c.joinGame("p1", UUID.randomUUID())
        );

        c.createGame("p1", 2, false);

        //join the newly created game
        UUID uuid = c.listGames().get(0).uuid();
        c.joinGame("p2", uuid);

        //the game starts...

        //try joining the already started game
        assertThrows(
                GameJoiningError.class,
                () -> c.joinGame("p3", uuid)
        );
    }

    @Test
    void createAndListGame() {
        assertEquals(0, c.listGames().size());

        //create game without registering
        assertThrows(
                NicknameNotRegisteredError.class,
                () -> c.createGame("p1", 2, false)
        );

        assertDoesNotThrow(
                () -> c.registerNickname("p1", vw)
        );

        c.createGame("p1", 2, false);

        assertEquals(1, c.listGames().size());
    }

    @Test
    void onGameUpdate() {
        //...
    }
}