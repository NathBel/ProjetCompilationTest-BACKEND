package org.acme;

import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class GameService {

    private static GameService instance;
    private Map<String, Game> games = new HashMap<>();

    private GameWebSocket gameWebSocket;


    // Constructeur privé pour empêcher la création d'instances en dehors de la classe
    private GameService() {

    }

    public static synchronized GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void setGameWebSocket(GameWebSocket gameWebSocket) {
        this.gameWebSocket = gameWebSocket;
    }

    public GameWebSocket getGameWebSocket() {
        return gameWebSocket;
    }


    public Game createGame(String gameId) {
        Game game = new Game(gameId);
        games.put(gameId, game);
        return game;

    }

    public Game getGame(String gameId) {

        return games.get(gameId);
    }

    public void addPlayer(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null) {
            game.addPlayer(playerId);
        }
    }

    public int getPlayerCount(String gameId) {
        Game game = getGame(gameId);
        if (game != null) {
            return game.getPlayers().size();
        }
        return 0;
    }

    public void removePlayer(String gameId, String playerId) {
        Game game = getGame(gameId);
        if (game != null) {
            game.removePlayer(playerId);
        }
    }

    public void processMessage(String gameId, String message) {
        Game game = getGame(gameId);
        if (game != null) {
            //Check what's before the ":" of the message
            String[] parts = message.split(":");
            String target = parts[0];
            String action = parts[1];

            switch (target) {
                case "game":
                    game.processMessageGame(action);

                case "move":
                    game.processMessageMove(action);
                case "ball":

                default:
                    break;
            }

        }
    }

}
