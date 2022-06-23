package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.common.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.common.exceptions.GameJoiningError;
import it.polimi.ingsw.common.exceptions.NicknameNotRegisteredError;
import it.polimi.ingsw.common.exceptions.NicknameNotValidException;
import it.polimi.ingsw.server.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    Controller c;
    VirtualView vw;

    @BeforeEach
    void setUp() {
        c = new Controller();
        vw = new VirtualView(c, null);
    }

    @Test
    void registerNickname() {
        //nickname not valid
        assertThrows(
                NicknameNotValidException.class,
                () -> c.registerNickname("")
        );

        assertDoesNotThrow(
                () -> c.registerNickname("p1")
        );

        //duplicated nickname
        assertThrows(
                DuplicatedNicknameException.class,
                () -> c.registerNickname("p1")
        );
        vw.setNickname("p1");

        //check isRegistered
        assertTrue(c.isRegistered("p1"));

        //create a game
        c.createGame("p1", 2, false);

        //simulate disconnection
        vw.onDisconnect();

        //register again after disconnection
        var gc = new AtomicReference<>(null);
        assertDoesNotThrow(
                () -> gc.set(c.registerNickname("p1"))
        );
        assertNotNull(gc);
    }

    @Test
    void joinGame() {
        assertDoesNotThrow(
                () -> {
                    c.registerNickname("p1");
                    c.registerNickname("p2");
                    c.registerNickname("p3");
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
                () -> c.registerNickname("p1")
        );

        c.createGame("p1", 2, false);

        assertEquals(1, c.listGames().size());
    }

    @Test
    void saveGames() {
        assertDoesNotThrow(() -> c.saveGames());
    }

}