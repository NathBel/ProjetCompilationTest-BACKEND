package org.acme;

import java.util.HashMap;
import java.util.Map;

public class Game {

    private static final double CAR_WIDTH = 8.0;
    private static final double CAR_HEIGHT = 6.0;
    private final String id;
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

    private boolean intervalsOverlap(double a1, double a2, double b1, double b2) {
        return !(a1 >= b2 || a2 <= b1);
    }


    private boolean checkCollision(Player player1, Player player2, String playerId, String otherPlayerId) {
        // Récupérer les positions et les dimensions des voitures
        double x1 = player1.getCar().getX();
        double y1 = player1.getCar().getY();
        String orientation1 = recentMovements.getOrDefault(playerId, "");
        double width1 = CAR_WIDTH;
        double height1 = CAR_HEIGHT;

        double x2 = player2.getCar().getX();
        double y2 = player2.getCar().getY();
        String orientation2 = recentMovements.getOrDefault(otherPlayerId, "");
        double width2 = CAR_WIDTH;
        double height2 = CAR_HEIGHT;

        if (orientation1.equals("ArrowUp") || orientation1.equals("ArrowDown")) {
            width1 = CAR_HEIGHT;
            height1 = CAR_WIDTH;
        }

        if (orientation2.equals("ArrowUp") || orientation2.equals("ArrowDown")) {
            width2 = CAR_HEIGHT;
            height2 = CAR_WIDTH;
        }

        // Calculer les boîtes englobantes pour chaque voiture
        double left1 = x1 - width1 / 2;
        double right1 = x1 + width1 / 2;
        double top1 = y1 + height1 / 2;
        double bottom1 = y1 - height1 / 2;

        double left2 = x2 - width2 / 2;
        double right2 = x2 + width2 / 2;
        double top2 = y2 + height2 / 2;
        double bottom2 = y2 - height2 / 2;

        // System.out.println("Player 1: left1 : " + left1 + ", right 1:" + right1 + ", top1 :" + top1 + ", bottom1 :" + bottom1);

        // System.out.println("Player 2: left2" + left2 + ", right2" + right2 + ", top2" + top2 + ", bottom1" + bottom2);


        // Vérifier s'il y a une collision entre les deux voitures
        return intervalsOverlap(left1, right1, left2, right2) && intervalsOverlap(bottom1, top1, bottom2, top2);
    }

    private boolean handleBallCollision(double ballX, double ballY, Player player1, Player player2, String playerId, String otherPlayerId) {
        // Récupérer les positions et les dimensions des voitures
        double x1 = player1.getCar().getX();
        double y1 = player1.getCar().getY();
        String orientation1 = recentMovements.getOrDefault(playerId, "");
        double width1 = CAR_WIDTH;
        double height1 = CAR_HEIGHT;

        double x2 = player2.getCar().getX();
        double y2 = player2.getCar().getY();
        String orientation2 = recentMovements.getOrDefault(otherPlayerId, "");
        double width2 = CAR_WIDTH;
        double height2 = CAR_HEIGHT;

        if (orientation1.equals("ArrowUp") || orientation1.equals("ArrowDown")) {
            width1 = CAR_HEIGHT;
            height1 = CAR_WIDTH;
        }

        if (orientation2.equals("ArrowUp") || orientation2.equals("ArrowDown")) {
            width2 = CAR_HEIGHT;
            height2 = CAR_WIDTH;
        }

        // Calculer les boîtes englobantes pour chaque voiture
        double left1 = x1 - width1 / 2;
        double right1 = x1 + width1 / 2;
        double top1 = y1 + height1 / 2;
        double bottom1 = y1 - height1 / 2;

        double left2 = x2 - width2 / 2;
        double right2 = x2 + width2 / 2;
        double top2 = y2 + height2 / 2;
        double bottom2 = y2 - height2 / 2;

        // Check if there is collision between the ball and the players
        boolean intervalsOverlap1 = intervalsOverlap(left1, right1, ballX - 1, ballX + 1) && intervalsOverlap(bottom1, top1, ballY - 1, ballY + 1);
        boolean intervalsOverlap2 = intervalsOverlap(left2, right2, ballX - 1, ballX + 1) && intervalsOverlap(bottom2, top2, ballY - 1, ballY + 1);
        System.out.println("Collision between ball and player1: " + intervalsOverlap1);
        System.out.println("Collision between ball and player2: " + intervalsOverlap2);

        // Move the ball in the opposite direction
        if (intervalsOverlap1 || intervalsOverlap2) {
            double ballSpeed = 20;
            double ballNewX = ball.getX();
            double ballNewY = ball.getY();

            if (intervalsOverlap1) {
                // Move the ball in the opposite direction of player 1
                if(orientation1.equals("ArrowUp")){
                    ballNewY = ball.getY() + ballSpeed;
                } else if(orientation1.equals("ArrowDown")){
                    ballNewY = ball.getY() - ballSpeed;
                } else if(orientation1.equals("ArrowLeft")){
                    ballNewX = ball.getX() - ballSpeed;
                } else if(orientation1.equals("ArrowRight")){
                    ballNewX = ball.getX() + ballSpeed;
                }
            }

            if (intervalsOverlap2) {
                // Move the ball in the opposite direction of player 2
                if(orientation2.equals("ArrowUp")){
                    ballNewY = ball.getY() + ballSpeed;
                } else if(orientation2.equals("ArrowDown")){
                    ballNewY = ball.getY() - ballSpeed;
                } else if(orientation2.equals("ArrowLeft")){
                    ballNewX = ball.getX() - ballSpeed;
                } else if(orientation2.equals("ArrowRight")){
                    ballNewX = ball.getX() + ballSpeed;
                }
            }

            ball.setX(ballNewX);
            ball.setY(ballNewY);

            // Send the new ball coordinates to the front
            sendMessageToAllPlayers("ball:" + ballNewX + "," + ballNewY);
        }

        return intervalsOverlap1 || intervalsOverlap2;
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

        // Calculer les dimensions de la boîte englobante pour chaque voiture en fonction de son orientation
        double width1 = CAR_WIDTH;
        double height1 = CAR_HEIGHT;
        double width2 = CAR_WIDTH;
        double height2 = CAR_HEIGHT;

        String orientation1 = recentMovements.getOrDefault(playerId, "");

        String orientation2 = recentMovements.getOrDefault(otherPlayerId, "");

        if (orientation1.equals("ArrowUp") || orientation1.equals("ArrowDown")) {
            width1 = CAR_HEIGHT;
            height1 = CAR_WIDTH;
        }

        if (orientation2.equals("ArrowUp") || orientation2.equals("ArrowDown")) {
            width2 = CAR_HEIGHT;
            height2 = CAR_WIDTH;
        }

        // Vérifier si le déplacement est valide (dans les limites du terrain et pas de collision entre les joueurs)
        if (isValidPosition(newX, newY)) {

            // Print ball coordinates:
            System.out.println("Ball coordinates: " + ball.getX() + ", " + ball.getY());

            double ballX = ball.getX();
            double ballY = ball.getY();

            if(handleBallCollision(ballX, ballY, player, otherPlayer, playerId , otherPlayerId)){
                System.out.println("Collision with ball");
                return;
            }

            if (checkCollision(player, otherPlayer, playerId , otherPlayerId)) {
                // Il y a collision, ajuster les positions des deux joueurs en conséquence

                // System.out.println("Collision between players");

                // Faire reculer les joueurs en fonction de leur vitesse
                handleCollision(player, otherPlayer, playerId, otherPlayerId);

                // Réinitialiser la vitesse du joueur courant
                speeds.put(playerId, INITIAL_SPEED);
                speeds.put(otherPlayerId, INITIAL_SPEED);

            } else {

                // System.out.println("No collision between players");

                player.getCar().setX(newX);
                player.getCar().setY(newY);

                // Envoyer les mises à jour aux joueurs
                sendMessageToAllPlayers(playerId + ":" + newX + "," + newY);

                //Envoyer la largeur des voitures pour les deux joueurs
                sendMessageToAllPlayers("player1,width:" + width1 + "," + height1);
                sendMessageToAllPlayers("player2,width:" + width2 + "," + height2);

            }

        }
    }

    // Vérifier si la position est valide (dans les limites du terrain)
    private boolean isValidPosition(double x, double y) {
        return x >= 0 && x <= 100 && y >= 0 && y <= 100;
    }

    private void handleCollision(Player player, Player otherPlayer, String playerId, String otherPlayerId) {
        // Récupérer les positions et orientations des voitures
        double x1 = player.getCar().getX();
        double y1 = player.getCar().getY();
        String orientation1 = recentMovements.getOrDefault(playerId, "");

        double x2 = otherPlayer.getCar().getX();
        double y2 = otherPlayer.getCar().getY();
        String orientation2 = recentMovements.getOrDefault(otherPlayerId, "");

        // Calculer les dimensions de la boîte englobante pour chaque voiture en fonction de son orientation
        double width1 = CAR_WIDTH;
        double height1 = CAR_HEIGHT;
        double width2 = CAR_WIDTH;
        double height2 = CAR_HEIGHT;

        if (orientation1.equals("ArrowUp") || orientation1.equals("ArrowDown")) {
            width1 = CAR_HEIGHT;
            height1 = CAR_WIDTH;
        }

        if (orientation2.equals("ArrowUp") || orientation2.equals("ArrowDown")) {
            width2 = CAR_HEIGHT;
            height2 = CAR_WIDTH;
        }

        // Calculer les boîtes englobantes pour chaque voiture
        double left1 = x1 - width1 / 2;
        double right1 = x1 + width1 / 2;
        double top1 = y1 + height1 / 2;
        double bottom1 = y1 - height1 / 2;

        double left2 = x2 - width2 / 2;
        double right2 = x2 + width2 / 2;
        double top2 = y2 + height2 / 2;
        double bottom2 = y2 - height2 / 2;

        // Vérifier la direction de la collision
        boolean collideX = !(left1 > right2 || right1 < left2);
        boolean collideY = !(top1 < bottom2 || bottom1 > top2);


        if (collideX || collideY) {
            // Calculer la distance de chevauchement entre les voitures
            double overlapX = (Math.min(right1, right2) - Math.max(left1, left2)) + 2.0;
            double overlapY = (Math.min(bottom1, bottom2) - Math.max(top1, top2)) + 2.0;

            // System.out.println("OverlapX: " + overlapX);
            // System.out.println("OverlapY: " + overlapY);


            // Faire reculer les voitures en fonction de la direction de la collision
            if (overlapX > overlapY) {
                if (left1 > left2) {
                    // Collision à gauche
                    // System.out.println("Collision à gauche");
                    player.getCar().setX(player.getCar().getX() - overlapX);
                    otherPlayer.getCar().setX(otherPlayer.getCar().getX() + overlapX);
                } else {
                    // Collision à droite
                    // System.out.println("Collision à droite");
                    player.getCar().setX(player.getCar().getX() + overlapX);
                    otherPlayer.getCar().setX(otherPlayer.getCar().getX() - overlapX);
                }
            } else {
                if (top1 > top2) {
                    // Collision en haut
                    // System.out.println("Collision en haut");
                    player.getCar().setY(player.getCar().getY() - overlapY);
                    otherPlayer.getCar().setY(otherPlayer.getCar().getY() + overlapY);
                } else {
                    // Collision en bas
                    // System.out.println("Collision en bas");
                    player.getCar().setY(player.getCar().getY() + overlapY);
                    otherPlayer.getCar().setY(otherPlayer.getCar().getY() - overlapY);
                }
            }


            // Envoyer les nouvelles coordonnées au front
            sendMessageToAllPlayers(playerId + ":" + player.getCar().getX() + "," + player.getCar().getY());
            sendMessageToAllPlayers(otherPlayerId + ":" + otherPlayer.getCar().getX() + "," + otherPlayer.getCar().getY());

        }
    }

}

