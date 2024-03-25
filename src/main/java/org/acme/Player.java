package org.acme;

public class Player {

    private String id;
    private Car car;

    public Player(String id, Car car) {
        this.id = id;
        this.car = car;
    }

    public String getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
