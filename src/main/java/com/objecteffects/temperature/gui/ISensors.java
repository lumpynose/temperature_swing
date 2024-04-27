package com.objecteffects.temperature.gui;

import com.objecteffects.temperature.sensors.SensorData;

public interface ISensors {
    void setup();

    void addSensor(SensorData target);

    void updateSensor(SensorData target);
}
