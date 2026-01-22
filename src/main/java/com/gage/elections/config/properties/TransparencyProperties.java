package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.transparency")
public class TransparencyProperties {
    double declarationBase;
    Map<Integer, Double> inconsistencyBonuses = new HashMap<>();
    double economicInfoItem;
    double attendanceWeight;
    double defaultAttendancePoint;
}
