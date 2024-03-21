package org.acme;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class WebSocketConnection {

    private Session session;

    public WebSocketConnection(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connexion WebSocket ouverte");
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connexion WebSocket fermée : " + closeReason);
        this.session = null;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Réception d'un message du serveur : " + message);
        // Traitez le message reçu ici
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println("Erreur WebSocket : " + throwable.getMessage());
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

}
