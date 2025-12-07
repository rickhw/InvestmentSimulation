package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataPoint {
    private final String date;
    private final double value;
    private final double cost;
}
