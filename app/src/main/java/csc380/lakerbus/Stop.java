package csc380.lakerbus;

public class Stop {

    private String stopName;
    private double xcoord, ycoord;

    public Stop(String name, double xcoord, double ycoord) {
        stopName = name;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
    }

    public String getStopName() {
        return stopName;
    }

    public double getXcoord() {
        return xcoord;
    }

    public double getYcoord() {
        return ycoord;
    }

    public int compare(Stop x, Stop y) {
        return x.getStopName().compareTo(y.getStopName());
    }
}
