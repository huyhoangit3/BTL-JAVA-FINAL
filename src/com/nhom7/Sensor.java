package com.nhom7;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sensor {
    private int index;
    private static int radius = 10;
    private Point coordinate;
    private List<Integer> nearNeighbors;
    private List<Integer> shortestPath;

    public Sensor() {
        this.index = 0;
        this.coordinate = new Point();
        this.nearNeighbors = new ArrayList<>();
        this.shortestPath = new ArrayList<>();
    }

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
