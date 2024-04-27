package com.objecteffects.temperature.main;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConfigurationTests {
    private final static Logger log = LogManager
            .getLogger(ConfigurationTests.class);

    @BeforeAll
    public static void loadConfiguration() throws ConfigurationException {
        Configuration.loadConfiguration();
    }

    @Test
    public void getConfigs() {
        log.debug("topics: {}", Configuration.getTopics());
        log.debug("brokerAddress: {}", Configuration.getBrokerAddress());
        log.debug("sensors: {}", Configuration.getSensors());
        log.debug("TUnit: {}", Configuration.getTUnit());
    }
}
