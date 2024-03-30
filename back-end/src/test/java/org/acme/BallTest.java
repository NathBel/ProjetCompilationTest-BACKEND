package org.acme;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    @Test
    void testBallConstructor() {
        Ball ball = new Ball(1.0, 2.0);
        assertEquals(1.0, ball.getX());
        assertEquals(2.0, ball.getY());
    }

    @Test
    void testBallDefaultConstructor() {
        Ball ball = new Ball();
        assertEquals(0.0, ball.getX());
        assertEquals(0.0, ball.getY());
    }

    @Test
    void testBallSettersAndGetters() {
        Ball ball = new Ball();
        ball.setX(1.0);
        ball.setY(2.0);
        assertEquals(1.0, ball.getX());
        assertEquals(2.0, ball.getY());
    }
}