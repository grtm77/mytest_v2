package com.example.system.demo.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "related")
public class RelatedProperties {

    private String sensorPath;
    private String gatewayPath;
    private String crossingPath;
    private String resultPath;

    private double gatewayRadius;
    private double crossingRadius;
    private double gatewayAngle;
    private String gatewayHeight;

}
