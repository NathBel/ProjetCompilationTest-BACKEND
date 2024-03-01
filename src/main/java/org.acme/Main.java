package org.acme;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }
}