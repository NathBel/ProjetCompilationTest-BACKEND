package org.acme;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerConstructor() {
        Car car = new Car(1.0, 2.0);
        Player player = new Player("player1", car);
        assertEquals("player1", player.getId());
        assertEquals(car, player.getCar());
    }

    @Test
    void testPlayerSettersAndGetters() {
        Car car = new Car(1.0, 2.0);
        Player player = new Player("player1", car);
        Car newCar = new Car(3.0, 4.0);
        player.setCar(newCar);
        assertEquals(newCar, player.getCar());
    }
}