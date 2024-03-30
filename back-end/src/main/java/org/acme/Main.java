package org.acme;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import java.net.URI;
import java.net.URISyntaxException;

@QuarkusMain
public class Main {
    public static void main(String... args) throws URISyntaxException {

        Quarkus.run(GameWebSocket.class, args);
    }
}