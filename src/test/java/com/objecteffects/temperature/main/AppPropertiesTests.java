package com.objecteffects.temperature.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Splitter;

class AppPropertiesTests {
    private static Properties appProps;

    public static void main(final String[] args) {
        try {
            AppPropertiesTests.loadProperties();
            AppPropertiesTests.getTopics();
        }
        catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void loadProperties()
            throws FileNotFoundException, IOException {
        final String rootPath = Thread.currentThread().getContextClassLoader()
                .getResource("").getPath();
        final String appConfigPath = rootPath + "mqtt.properties";

        appProps = new Properties();

        try (InputStream prop = new FileInputStream(appConfigPath)) {
            appProps.load(prop);
        }
    }

    public String getBrokerAddress() {
        final String brokerAddress = appProps.getProperty("mqtt.brokerAddress");

        return brokerAddress;
    }

    public static Map<String, String> getSensors() {
        final Map<String, String> splitKeyValues = Splitter.on(",")
                .omitEmptyStrings().trimResults().withKeyValueSeparator(":")
                .split(appProps.getProperty("mqtt.sensors"));

        return splitKeyValues;
    }

    public static String[] getTopics() {
        final Iterable<String> iter = Splitter.on(",").omitEmptyStrings()
                .trimResults().split(appProps.getProperty("mqtt.topics"));

        final List<String> array = new ArrayList<>();
        for (final String e : iter) {
            array.add(e);
        }

        return array.toArray(new String[0]);
    }
}
