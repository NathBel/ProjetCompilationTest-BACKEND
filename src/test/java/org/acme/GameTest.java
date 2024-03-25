package org.acme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game("testGame");
    }

    @Test
    void testAddPlayer() {
        game.addPlayer("player1");
        assertEquals(1, game.getPlayers().size());
    }

    @Test
    void testRemovePlayer() {
        game.addPlayer("player1");
        game.removePlayer("player1");
        assertEquals(0, game.getPlayers().size());
    }
}

