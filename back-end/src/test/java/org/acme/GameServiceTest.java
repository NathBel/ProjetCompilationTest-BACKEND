package org.acme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = GameService.getInstance();
    }

    @Test
    void testCreateGame() {
        Game game = gameService.createGame("testGame");
        assertNotNull(game);
        assertEquals("testGame", game.getId());
    }

    @Test
    void testAddPlayer() {
        gameService.createGame("testGame");
        gameService.addPlayer("testGame", "player1");
        assertEquals(1, gameService.getPlayerCount("testGame"));
    }
}
