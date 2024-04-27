package com.objecteffects.temperature.sensors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.objecteffects.temperature.main.Configuration;
import com.objecteffects.temperature.main.MainPaho;

public class ProcessSensorData {
    private final static Logger log = LogManager
            .getLogger(ProcessSensorData.class);

    private final static Collection<SensorData> sensors = Collections
            .synchronizedSet(new HashSet<>());
    private final static DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("HH:mm");
    private final static Map<String, String> propSensors =
            Configuration.getSensors();
    private final static TUnit tunit = Configuration.getTUnit();

    public static void processData(final String topic, final String data) {
        final Gson gson = new Gson();

        final String topic_trimmed = StringUtils.substringAfterLast(topic, "/");

        log.debug("topic: {}", topic_trimmed);

        final SensorData target = gson.fromJson(data, SensorData.class);

        // sensor missing from config file?
        if (!propSensors.containsKey(topic_trimmed)) {
            return;
        }

        target.setSensorName(propSensors.get(topic_trimmed));
        target.setTemperatureShow((float) tunit.convert(target));
        target.setTemperatureLetter(tunit.toString());

        final LocalDateTime dateTime = LocalDateTime.now();

        target.setTimestamp(dtf.format(dateTime));

        log.debug("decoded data: {}", target.toString());

        if (sensors.add(target)) {
            log.debug("add target: {}", target.getSensorName());

            MainPaho.getGui().addSensor(target);
        }
        else {
            log.debug("update target: {}", target.getSensorName());

            MainPaho.getGui().updateSensor(target);
        }

        log.debug("length: {}", Integer.valueOf(sensors.size()));
    }
}
