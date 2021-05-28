package com.nhom7;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to simulate real sensor.
 */
public class Sensor {
    // index of sensor. start with 0.
    private int index;
    // default radius of sensor.
    private static int radius = 10;
    // position (Oxy) of sensor in considering area.
    private Point coordinate;
    // list of neighbors sensor (connected sensor).
    private List<Integer> nearNeighbors;
    // list of sensors make shortest path to sink sensor.
    private List<Integer> shortestPath;

    // constructor non-args
    public Sensor() {
        this.index = 0;
        this.coordinate = new Point();
        this.nearNeighbors = new ArrayList<>();
        this.shortestPath = new ArrayList<>();
    }

    /**
     * This method is used to calculate distance between two sensor.
     *
     * @param other - sensor you want to calculate distance to it.
     * @return distance between two sensor
     * @see <a href="https://www.wikihow.com/Find-the-Distance-Between-Two-Points">Google</a>
     */
    public double distanceToOther(Sensor other) {
        return Math.sqrt(Math.pow(this.coordinate.getX() -
                other.coordinate.getX(), 2) +
                Math.pow(this.coordinate.getY() -
                        other.coordinate.getY(), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(coordinate, sensor.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, coordinate);
    }

    // getter and setter method
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static int getRadius() {
        return radius;
    }

    public static void setRadius(int radius) {
        Sensor.radius = radius;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    public List<Integer> getNearNeighbors() {
        return nearNeighbors;
    }

    public void setNearNeighbors(List<Integer> nearNeighbors) {
        this.nearNeighbors = nearNeighbors;
    }

    public List<Integer> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Integer> shortestPath) {
        this.shortestPath = shortestPath;
    }
}
