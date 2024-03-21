package org.acme;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.net.URI;
import java.net.URISyntaxException;

@QuarkusMain
public class Main {
    static WebSocketConnection webSocketConnection ;
    public static void main(String... args) throws URISyntaxException {
        Quarkus.run(MyApp.class, args);

        URI endpointURI = new URI("ws://mqtt.eclipseproject.io:443/mqtt");
   webSocketConnection = new WebSocketConnection(endpointURI);
        //WebSocketConnection webSocketConnection = new WebSocketConnection(endpointURI);
    }
}