package org.acme;

public class Voiture {

    public float carbure;

    public Voiture(float carbure) {
        this.carbure = carbure;
    }

    public void setCarbure(float newCarbure) {
        this.carbure = newCarbure;
    }

    public void drive(String direction) {

        if (this.carbure < 0.333f) {
            System.out.println("No more fuel");
            return;
        } else {
            this.carbure -= 1.0f/3.0f;

        }




        switch(direction) {
            case "left":
                System.out.println("Turning left");
                break;
            case "right":
                System.out.println("Turning right");
                break;
            case "forward":
                System.out.println("Going forward");
                break;
            case "backward":
                System.out.println("Going backward");
                break;
            default:
                System.out.println("Invalid direction");
        }
    }
}
