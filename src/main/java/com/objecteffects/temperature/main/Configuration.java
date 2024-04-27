package com.objecteffects.temperature.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.sensors.TUnit;

public class Configuration {
    @SuppressWarnings("unused")
    private final static Logger log = LogManager
            .getLogger(Configuration.class);

    private final static String configFile =
            "c:/home/rusty/mqtt.properties";

    private final static Parameters params = new Parameters();

    private final static FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(
                    PropertiesConfiguration.class)
                    .configure(params.properties()
                            .setFileName(configFile));

    private static FileBasedConfiguration config;

    public static void loadConfiguration() throws ConfigurationException {
        config = builder.getConfiguration();
    }

    public static List<String> getTopics() {
        return Arrays.asList(config.getStringArray("topics"));
    }

    public static String getBrokerAddress() {
        return config.getString("brokerAddress");
    }

    public static Map<String, String> getSensors() {
        final Map<String, String> splitKeyValues = new HashMap<>();

        for (final String entry : config.getStringArray("sensors")) {
            final String[] kv = entry.split(":");
            splitKeyValues.put(kv[0], kv[1]);
        }

        return splitKeyValues;
    }

    public static void loadProperties() {
        try {
            Configuration.loadConfiguration();
        }
        catch (final ConfigurationException ex) {
            log.error("loadConfiguration", ex);

            throw new RuntimeException(ex);
        }
    }

    public static TUnit getTUnit() {
        final String tunitProp = config.getString("tunit");

        if (tunitProp == null) {
            return TUnit.Fahrenheit;
        }

        final TUnit tunit = TUnit.get(tunitProp);

        if (tunit == null) {
            return TUnit.Fahrenheit;
        }

        return tunit;
    }
}
