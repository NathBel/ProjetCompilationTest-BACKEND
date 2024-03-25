package org.acme;

import io.quarkus.runtime.QuarkusApplication;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.Session;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.CloseReason;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/game/{id}")
public class GameWebSocket implements QuarkusApplication {

    private String gameId;
    private Set<Session> sessions = new HashSet<>();

    GameService gameService = GameService.getInstance();


    @OnOpen
    public void onOpen(@PathParam("id") String gameId, Session session) {

        Game game = gameService.getGame(gameId);
        if (game == null) {
            game = gameService.createGame(gameId);
            gameService.setGameWebSocket(this);
        }

        if (gameService.getPlayerCount(gameId) < 2) {
            this.gameId = gameId;
            sessions.add(session);
            gameService.addPlayer(gameId, session.getId());

            // Envoyer le nombre de joueurs connectés à tous les joueurs connectés à la partie
            int playerCount = gameService.getPlayerCount(gameId);

            if (playerCount == 1) {
                sendMessageToSession(session, "green");
            }
            else if (playerCount == 2) {
                sendMessageToSession(session, "red");
            }

            //Cast playerCount to String
            String message = Integer.toString(playerCount);
            //délai de 1 seconde
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessageToAll(message);
        } else {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Game is full"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @OnMessage
    public void onMessage(String message) {
        // process incoming message and update game state
        gameService.processMessage(gameId, message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        gameService.removePlayer(gameId, session.getId());
    }

    @OnError
    public void onError(Throwable error) {
        // handle error
    }

    public void sendMessageToSession(Session session, String message) {
        if (session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        } else {
            sessions.remove(session);
        }
    }

    public void sendMessageToAll(String message) {
        sessions.forEach(session -> {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            } else {
                System.out.println("Session is closed, cannot send message: " + session.getId() + "\n");
                sessions.remove(session);
            }
        });
    }


    @Override
    public int run(String... args) throws Exception {
        return 0;
    }
}

