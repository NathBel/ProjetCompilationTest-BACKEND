package org.acme;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Game {

    private String id;
    private Map<String, Player> players = new HashMap<>();
    private Ball ball;

    // Définir des variables pour la vitesse initiale et le plafond de vitesse
    private static final double INITIAL_SPEED = 0.5;
    private static final double MAX_SPEED = 2.0; // Par exemple, vous pouvez ajuster ce nombre selon vos besoins

    // Ajouter des variables pour suivre les mouvements récents et la vitesse actuelle de chaque joueur
    private Map<String, String> recentMovements = new HashMap<>();
    private Map<String, Double> speeds = new HashMap<>();

    public Game(String id) {
        this.id = id;
        this.ball = new Ball();
    }

    public String getId() {
        return id;
    }

    public void addPlayer(String playerId) {
        Player player = new Player(playerId, new Car(0, 0));
        players.put(player.getId(), player);
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Ball getBall() {
        return ball;
    }

    public void sendMessageToAllPlayers(String message) {
        GameService gameService = GameService.getInstance();
        gameService.getGameWebSocket().sendMessageToAll(message);
    }


    public void processMessageGame(String message) {

        Player player1 = players.values().stream().findFirst().get();
        Player player2 = players.values().stream().skip(1).findFirst().get();

        switch (message) {
            case "start":
                ball.setX(50);
                ball.setY(50);

                //Set player cars to initial position one to 5,50, and other to 95,50

                player1.getCar().setX(5);
                player1.getCar().setY(50);

                player2.getCar().setX(95);
                player2.getCar().setY(50);

                //Send message to all players
                sendMessageToAllPlayers("game:started");
                sendMessageToAllPlayers("ball:" + ball.getX() + "," + ball.getY());
                sendMessageToAllPlayers("player1:" + player1.getCar().getX() + "," + player1.getCar().getY());
                sendMessageToAllPlayers("player1:rotateRight");
                sendMessageToAllPlayers("player2:" + player2.getCar().getX() + "," + player2.getCar().getY());
                sendMessageToAllPlayers("player2:rotateLeft");
                break;
        }
    }

    private boolean checkCollision(Player player1, Player player2) {
        // Récupérer les positions et les dimensions des voitures
        double x1 = player1.getCar().getX();
        double y1 = player1.getCar().getY();
        double width1 = 10.0;
        double height1 = 5.0;

        double x2 = player2.getCar().getX();
        double y2 = player2.getCar().getY();
        double width2 = 10.0;
        double height2 = 5.0;

        // Calculer les boîtes englobantes pour chaque voiture
        double left1 = x1 - width1 / 2;
        double right1 = x1 + width1 / 2;
        double top1 = y1 + height1 / 2;
        double bottom1 = y1 - height1 / 2;

        double left2 = x2 - width2 / 2;
        double right2 = x2 + width2 / 2;
        double top2 = y2 + height2 / 2;
        double bottom2 = y2 - height2 / 2;

        // Vérifier s'il y a une collision entre les deux voitures
        return !(left1 > right2 || right1 < left2 || top1 < bottom2 || bottom1 > top2 );
    }

    public void processMessageMove(String message) {
        String direction = "";

        if (message.contains("-")) {
            //decompose message to get before and after ":"
            String[] parts = message.split("-");
            message = parts[0];
            direction = parts[1];
        }

        Player player1 = players.values().stream().findFirst().get();
        Player player2 = players.values().stream().skip(1).findFirst().get();

        switch (message) {
            case "player1":
                handlePlayerMovement(player1, player2, direction, "player1", "player2");
                break;
            case "player2":
                handlePlayerMovement(player2, player1, direction, "player2", "player1");
                break;
        }
    }

    private void handlePlayerMovement(Player player, Player otherPlayer, String direction, String playerId, String otherPlayerId) {
        // Mettre à jour la vitesse actuelle du joueur
        Double currentSpeed = speeds.getOrDefault(playerId, INITIAL_SPEED);

        // Augmenter la vitesse si la direction est la même que précédemment
        if (direction.equals(recentMovements.getOrDefault(playerId, ""))) {
            if (currentSpeed < MAX_SPEED) {
                currentSpeed += 0.25;
            }
        } else {
            // Réinitialiser la vitesse si la direction a changé
            currentSpeed = INITIAL_SPEED;
        }

        // Limiter la vitesse au plafond
        currentSpeed = Math.min(currentSpeed, MAX_SPEED);

        // Mettre à jour les mouvements récents et la vitesse actuelle du joueur
        recentMovements.put(playerId, direction);
        speeds.put(playerId, currentSpeed);

        // Mettre à jour la position du joueur en fonction de la vitesse
        double newX = player.getCar().getX();
        double newY = player.getCar().getY();

        switch (direction) {
            case "ArrowUp":
                sendMessageToAllPlayers(playerId + ":rotateUp");
                newY += currentSpeed;
                break;
            case "ArrowDown":
                sendMessageToAllPlayers(playerId + ":rotateDown");
                newY -= currentSpeed;
                break;
            case "ArrowLeft":
                sendMessageToAllPlayers(playerId + ":rotateLeft");
                newX -= currentSpeed;
                break;
            case "ArrowRight":
                sendMessageToAllPlayers(playerId + ":rotateRight");
                newX += currentSpeed;
                break;
        }

        // Vérifier si le déplacement est valide (dans les limites du terrain et pas de collision entre les joueurs)
        if (isValidPosition(newX, newY)) {
            if (checkCollision(player, otherPlayer)) {
                // Il y a collision, ajuster les positions des deux joueurs en conséquence

                System.out.println("Collision between players");

                // Faire reculer les joueurs en fonction de leur vitesse

                // Réinitialiser la vitesse du joueur courant
                speeds.put(playerId, INITIAL_SPEED);
                speeds.put(otherPlayerId, INITIAL_SPEED);

            } else {

                System.out.println("No collision between players");

                player.getCar().setX(newX);
                player.getCar().setY(newY);

                // Envoyer les mises à jour aux joueurs
                sendMessageToAllPlayers(playerId + ":" + newX + "," + newY);
            }

        }
    }

    // Vérifier si la position est valide (dans les limites du terrain)
    private boolean isValidPosition(double x, double y) {
        return x >= 0 && x <= 100 && y >= 0 && y <= 100;
    }


}

