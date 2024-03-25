package org.acme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import jakarta.websocket.CloseReason;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;

public class GameWebSocketTest {

    private GameWebSocket gameWebSocket;
    private GameService gameService;
    private Session session;
    private RemoteEndpoint.Async asyncRemote;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        gameService = mock(GameService.class);
        session = mock(Session.class);
        asyncRemote = mock(RemoteEndpoint.Async.class);
        gameWebSocket = new GameWebSocket();
        gameWebSocket.gameService = gameService;
        when(session.getAsyncRemote()).thenReturn(asyncRemote);
    }

    @Test
    void testOnOpenNewGameSuccess() throws IOException {
        String gameId = "testGame";
        when(gameService.getGame(gameId)).thenReturn(null);
        when(gameService.createGame(gameId)).thenReturn(new Game(gameId));

        RemoteEndpoint.Async asyncRemote = mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(asyncRemote);

        gameWebSocket.onOpen(gameId, session);

        // Verify that a message was sent to the player to indicate their color
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(asyncRemote).sendText(messageCaptor.capture());
        String message = messageCaptor.getValue();
        assertEquals("green", message);

        // Verify that the session was added to the list of sessions
        assertTrue(gameWebSocket.getSessions().contains(session));

        // Verify that the game service methods were called
        verify(gameService).setGameWebSocket(gameWebSocket);
        verify(gameService).addPlayer(gameId, session.getId());
        verify(gameService, never()).removePlayer(gameId, session.getId());
    }



    @Test
    public void testOnOpenExistingGameSuccess() throws IOException {
        String gameId = "testGame";
        Game game = new Game(gameId);
        when(gameService.getGame(gameId)).thenReturn(game);

        gameWebSocket.onOpen(gameId, session);

        verify(asyncRemote, never()).sendText(any());
        verify(session, never()).close(any(CloseReason.class));
        verify(gameService).setGameWebSocket(gameWebSocket);
        verify(gameService).addPlayer(gameId, session.getId());
    }

    @Test
    public void testOnOpenGameFullCloseSession() throws IOException {
        String gameId = "testGame";
        Game game = new Game(gameId);
        game.addPlayer("player1");
        game.addPlayer("player2");
        when(gameService.getGame(gameId)).thenReturn(game);

        gameWebSocket.onOpen(gameId, session);

        verify(asyncRemote, never()).sendText(any());
        verify(session).close(any(CloseReason.class));
        verify(gameService, never()).setGameWebSocket(gameWebSocket);
        verify(gameService, never()).addPlayer(gameId, session.getId());
    }
}
