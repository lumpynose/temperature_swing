package com.objecteffects.temperature.main;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.gui.ISensors;
import com.objecteffects.temperature.gui.Sensors;
import com.objecteffects.temperature.gui.SensorsNull;
import com.objecteffects.temperature.mqtt.paho.ListenerPaho;

public class MainPaho {
    static {
        final String jar = System.getenv("JAR");

        if (Objects.equals(jar, "jar")) {
            System.setProperty("log4j.configurationFile", "log4j2_file.xml");
        }
        else {
            System.setProperty("log4j.configurationFile", "log4j2_console.xml");
        }
    }

    private final static Logger log = LogManager.getLogger(MainPaho.class);
    private static ISensors gui;

    public static void main(final String[] args) {
        guiStart();

        Configuration.loadProperties();

        ListenerPaho.startMqttListener();
    }

    private static void guiStart() {
        Toolkit.getDefaultToolkit().getScreenSize();

        if (GraphicsEnvironment.isHeadless()) {
            gui = new SensorsNull();
        }
        else {
            gui = new Sensors();
        }

        if (Desktop.isDesktopSupported()) {
            log.debug("desktop supported");
        }
        else {
            log.debug("desktop NOT supported");
        }

        if (SystemTray.isSupported()) {
            log.debug("system tray supported");
        }
        else {
            log.debug("system try NOT supported");
        }

        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    gui.setup();
                }
            });
        }
        catch (final InvocationTargetException e) {
            log.warn(e);

            throw new RuntimeException(e);
        }
        catch (final InterruptedException e) {
            log.warn(e);

            throw new RuntimeException(e);
        }
    }

    public static ISensors getGui() {
        return gui;
    }
}
