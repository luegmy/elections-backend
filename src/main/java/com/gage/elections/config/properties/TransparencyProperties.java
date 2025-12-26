package com.gage.elections.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "electoral.scoring.transparency")
public class TransparencyProperties {
    private double declarationBase;
    private List<Double> inconsistencyBonuses; // Mapea el array [10.0, 5.0, 0.0]
    private double economicInfoItem;
    private double attendanceWeight;
}
