package org.acme;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CarTest {

    @Test
    void testCarConstructor() {
        Car car = new Car(1.0, 2.0);
        assertEquals(1.0, car.getX());
        assertEquals(2.0, car.getY());
    }

    @Test
    void testCarSettersAndGetters() {
        Car car = new Car();
        car.setX(1.0);
        car.setY(2.0);
        assertEquals(1.0, car.getX());
        assertEquals(2.0, car.getY());
    }
}


