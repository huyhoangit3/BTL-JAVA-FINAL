package com.nhom7;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class Program {
    // number of sensor, length and with of considering area.
    private int numberOfSensor, lengthOfArea, widthOfArea;
    // list of sensors in network.
    private List<Sensor> sensors;

    //constructor non-args
    public Program() {
        this.numberOfSensor = 0;
        this.lengthOfArea = 0;
        this.widthOfArea = 0;
        this.sensors = new ArrayList<>();
    }

    // init data for per sensor in network.
    public void loadData() {
        findAllNeighbors();
        findAllShortestPath();
    }

    // This method is used to random sensors.
    public void randSensors() {
        // initialize sink sensor.
        Sensor sinkSensor = new Sensor();
        // set coordinate for sink sensor.
        sinkSensor.setCoordinate(new Point(lengthOfArea / 2, widthOfArea / 2));
        // add sink sensor to sensor list.
        this.sensors.add(sinkSensor);

        // index of sensor.
        int pos = 1;
        Random rand = new Random();
        while (pos < numberOfSensor) {
            // initialize per sensor.
            Sensor sensor = new Sensor();
            // set index and coordinate for per sensor.
            sensor.setIndex(pos);
            sensor.setCoordinate(new Point(rand.nextInt(this.lengthOfArea + 1),
                    rand.nextInt(this.widthOfArea + 1)));
            // make sure not have duplicate sensor in network.
            if (!this.sensors.contains(sensor)) {
                this.sensors.add(sensor);
                pos++;
            }
        }
    }

    /**
     * This method is used to find neighbors of sensor source.
     *
     * @param source - sensor which you want to find neighbors sensor.
     */
    public void findNeighbors(Sensor source) {
        // traverse sensor list.
        for (Sensor sensor : this.sensors) {
            if (!source.equals(sensor) && source.distanceToOther(sensor) <= 2 * Sensor.getRadius()) {
                source.getNearNeighbors().add(sensor);
            }
        }
    }

    // find all neighbors for all sensor in network.
    public void findAllNeighbors() {
        for (Sensor sensor : this.sensors) {
            findNeighbors(sensor);
        }
    }

    // find all shortest path for all sensor that have path to sink sensor.
    public void findAllShortestPath() {
        for (int i = 1; i < this.numberOfSensor; i++) {
            this.sensors.get(i).setShortestPath(findShortestPath(this.sensors.get(0),
                    this.sensors.get(i)));
        }
    }

    /**
     * This method is used to find shortest path from source to destination sensor.
     *
     * @param source - start sensor.
     * @param dest   - destination sensor.
     * @return path from source to destination.
     */
    public List<Sensor> findShortestPath(Sensor source, Sensor dest) {
        Map<Sensor, Sensor> pred = new LinkedHashMap<>();

        if (!BFS(source, dest, pred)) {
            return null;
        }
        // LinkedList to store path
        List<Sensor> path = new ArrayList<>();
        Sensor crawl = dest;
        path.add(crawl);
        while (pred.get(crawl) != null) {
            path.add(pred.get(crawl));
            crawl = pred.get(crawl);
        }
        // reverse path list.
        Collections.reverse(path);
        return path;
    }
    /**
     * This method implement Breadth First Search(BFS) algorithm.
     *
     * @param src         - start sensor.
     * @param destination - destination sensor.
     * @param predecessor - map to store predecessors.
     * @return shortest path from source to destination.
     */
    public boolean BFS(Sensor src, Sensor destination, Map<Sensor, Sensor> predecessor) {
        LinkedList<Sensor> queue = new LinkedList<>();
        for (Sensor sensor : this.sensors) {
            sensor.setVisited(false);
        }
        src.setVisited(true);
        queue.add(src);
        // bfs Algorithm
        while (!queue.isEmpty()) {
            Sensor currentSensor = queue.remove();
            List<Sensor> neighbors = currentSensor.getNearNeighbors();
            for (Sensor neighbor : neighbors) {
                if (!neighbor.isVisited()) {
                    neighbor.setVisited(true);
                    predecessor.put(neighbor, currentSensor);
                    queue.add(neighbor);
                    // stopping condition (when we find our destination)
                    if (neighbor.equals(destination))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * This method is used to read data from file and load it into sensor list.
     *
     * @param filePath - file name will be read.
     */
    public void readFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        Scanner sc = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            sc = new Scanner(fileInputStream);
            int numberOfLine = 0;
            while (sc.hasNextLine()) {
                Sensor sensor = new Sensor();
                // read line by line
                String line = sc.nextLine();
                // remove line break on per line.
                line = line.replace("\n", "");
                // split line by pass a regex pattern.
                String[] result = line.split(",");
                // first line is information about length and width of considering area.
                if (numberOfLine == 0) {
                    this.lengthOfArea = Integer.parseInt(result[0]);
                    this.widthOfArea = Integer.parseInt(result[1]);
                    // rest of file is information about sensors.
                } else {
                    sensor.setCoordinate(new Point(Integer.parseInt(result[0]),
                            Integer.parseInt(result[1])));
                    sensor.setIndex(Integer.parseInt(result[2]));
                    Sensor.setRadius(Integer.parseInt(result[3]));
                    // add sensor to sensor list.
                    this.sensors.add(sensor);
                }
                // increase number of line.
                numberOfLine++;
            }
            this.setNumberOfSensor(this.sensors.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // close resource.
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    /**
     * This method is used to write data of this network to file.
     *
     * @param filePath - file name will be write.
     */
    public void writeToFile(String filePath) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            // information about considering area.
            String areaInfo = this.lengthOfArea + "," + this.widthOfArea + "\n";
            // write information about considering area to file.
            fileWriter.write(areaInfo);
            for (Sensor sensor : this.sensors) {
                // information about per sensor includes x-point, y-point, index, radius
                // seperate by comma.
                String line = (int) sensor.getCoordinate().getX() + "," +
                        (int) sensor.getCoordinate().getY() + "," + sensor.getIndex() +
                        "," + Sensor.getRadius() + "\n";
                fileWriter.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // return String include information about sensor list.
    public String printSensors(Iterable<Sensor> sensorList) {
        StringBuilder result = new StringBuilder();
        for (Sensor sensor : sensorList) {
            result.append("  +++STT: ").append(sensor.getIndex()).append("     (X,Y) = (").
                    append(sensor.getCoordinate().getX()).append(", ").
                    append(sensor.getCoordinate().getY()).append(")\n");
        }
        return result.toString();
    }

    // getter and setter.
    public void setNumberOfSensor(int numberOfSensor) {
        this.numberOfSensor = numberOfSensor;
    }

    public int getLengthOfArea() {
        return lengthOfArea;
    }

    public void setLengthOfArea(int lengthOfArea) {
        this.lengthOfArea = lengthOfArea;
    }

    public int getWidthOfArea() {
        return widthOfArea;
    }

    public void setWidthOfArea(int widthOfArea) {
        this.widthOfArea = widthOfArea;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public int getNumberOfSensor() {
        return numberOfSensor;
    }
}
