package model;

public class DataPoint {
    private final String date;
    private final double value;
    private final double cost;

    public DataPoint(String date, double value, double cost) {
        this.date = date;
        this.value = value;
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    public double getCost() {
        return cost;
    }
}
