package org.acme;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;

class MyAppTest {

    @Test
    void title() {
        MyApp myApp = new MyApp();
        assertEquals("##Hello", myApp.title("Hello", 2));
        assertEquals("Hello", myApp.title("Hello", 0));
        assertEquals("#", myApp.title(null, 0));
        assertEquals("#", myApp.title(null, 2));
        assertEquals("#", myApp.title(null, -2));
        assertEquals("Hello", myApp.title("Hello", null));
    }

    @Test
    void run() throws Exception {
        MyApp myApp = new MyApp();
        assertEquals(0, myApp.run());
    }

    @Test
    void carDrive(){
        Voiture car = new Voiture(1);
        car.drive("left");
        assertEquals(0.667, car.carbure, 0.001);

        car.drive("right");
        assertEquals(0.334, car.carbure, 0.001);

        car.drive("forward");
        assertEquals(0, car.carbure, 0.001);

        car.drive("backward");
        assertEquals(0, car.carbure, 0.001);

        car.drive("invalid");
        assertEquals(0, car.carbure, 0.001);
    }
}