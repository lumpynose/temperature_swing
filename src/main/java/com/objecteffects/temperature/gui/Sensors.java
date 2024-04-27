package com.objecteffects.temperature.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.sensors.SensorData;

final public class Sensors implements ISensors {
    private final static Logger log = LogManager.getLogger(Sensors.class);

    private final static String NAME = "name";
    private final static String TEMPERATURE = "temperature";
    private final static String HUMIDITY = "humidity";
    private final static String TIME = "time";
    private final static String LUMINANCE = "luminance";
    private final static String PRESSURE = "pressure";
    private final static String VOC = "voc";
    private final static String NFORMAT = " %s ";
    private final static String TFORMAT = "%3.0f\u00B0 %s";
    private final static String HFORMAT = "%3.0f%%";
    private final static String LUMFORMAT = "%d lux";
    private final static String PRESSFORMAT = "%3.0f mb";
    private final static String VOCFORMAT = "%d voc";

    private final static int VSPACE = 4;

    private final static Color nameColor = new Color(128, 128, 0);
    private final static Color borderColor = nameColor.darker();
    private final static Color valuesColor = new Color(143, 188, 143);

    private final static Font nameFont = new Font("Arial Bold", Font.BOLD, 24);
    private final static Font temperatureFont = new Font("Arial Bold",
            Font.BOLD, 20);
    private final static Font humidityFont = new Font("Arial", Font.PLAIN, 18);
    private final static Font luminanceFont = new Font("Arial", Font.PLAIN, 16);
    private final static Font pressureFont = new Font("Arial", Font.PLAIN, 16);
    private final static Font vocFont = new Font("Arial", Font.PLAIN, 16);
    private final static Font timeFont = new Font("Arial", Font.PLAIN, 12);

    private final static Map<String, Map<String, JComponent>> panelsMap = new HashMap<>();

    private final static JFrame frame = new JFrame("temperatures");
    private final static JPanel mainPanel = new JPanel();

    @Override
    public void setup() {
        final LayoutManager boxLayout = new BoxLayout(mainPanel,
                BoxLayout.Y_AXIS);
        mainPanel.setLayout(boxLayout);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.setName("frame");

        mainPanel.setBackground(valuesColor);
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setName("mainPanel");
    }

    @Override
    public void addSensor(final SensorData data) {
        final Map<String, JComponent> labelsMap = new HashMap<>();

        panelsMap.put(data.getSensorName(), labelsMap);

        final JPanel sensorPanel = new JPanel();
        final LayoutManager boxLayout = new BoxLayout(sensorPanel,
                BoxLayout.Y_AXIS);
        sensorPanel.setLayout(boxLayout);

        sensorPanel.setName(data.getSensorName());
        sensorPanel.setOpaque(true);
        sensorPanel.setBackground(valuesColor);
        sensorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        log.debug("sensor name: {}", data.getSensorName());

        final JButton nameButton = new JButton();

        nameButton.setBorderPainted(false);
        nameButton.setFocusPainted(false);

        nameButton.setText(String.format(NFORMAT, data.getSensorName()));
        nameButton.setHorizontalAlignment(SwingConstants.CENTER);

        nameButton.setName(NAME);
        nameButton.setOpaque(true);
        nameButton.setBackground(nameColor);
        nameButton.setFont(nameFont);
        nameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameButton.setMaximumSize(
                new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        nameButton.setBorder(BorderFactory.createLineBorder(borderColor));

        sensorPanel.add(nameButton);
        sensorPanel.add(Box.createRigidArea(new Dimension(0, VSPACE)));

        final JPanel valuesPanel = new JPanel();

        final LayoutManager boxLayoutVP = new BoxLayout(valuesPanel,
                BoxLayout.Y_AXIS);
        valuesPanel.setLayout(boxLayoutVP);

        valuesPanel.setName(data.getSensorName());
        valuesPanel.setOpaque(false);
        valuesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sensorPanel.add(valuesPanel);

        nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                nameButtonPressed(valuesPanel);
            }
        });

        final JButton temperatureLabel = new JButton();

        temperatureLabel.setBorderPainted(false);
        temperatureLabel.setFocusPainted(false);

        temperatureLabel.setText(String.format(TFORMAT,
                Double.valueOf(data.getTemperatureShow()),
                data.getTemperatureLetter()));
        temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);

        temperatureLabel.setName(TEMPERATURE);
        temperatureLabel.setOpaque(true);
        temperatureLabel.setBackground(valuesColor);
        temperatureLabel.setFont(temperatureFont);
        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        temperatureLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                WeatherPainter.temperatureLabelPressed();
            }
        });

        valuesPanel.add(temperatureLabel);
        valuesPanel.add(Box.createRigidArea(new Dimension(0, VSPACE)));

        labelsMap.put(TEMPERATURE, temperatureLabel);

        if (Float.isFinite(data.getHumidity())) {
            final JLabel humidityLabel = new JLabel();

            humidityLabel.setText(
                    String.format(HFORMAT, Double.valueOf(data.getHumidity())));
            humidityLabel.setHorizontalAlignment(SwingConstants.CENTER);

            humidityLabel.setName(HUMIDITY);
            humidityLabel.setOpaque(false);
            humidityLabel.setFont(humidityFont);
            humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            valuesPanel.add(humidityLabel);
            valuesPanel.add(Box.createRigidArea(new Dimension(0, VSPACE)));

            labelsMap.put(HUMIDITY, humidityLabel);
        }

        if (data.getLuminance() > Integer.MIN_VALUE) {
            final JLabel luminanceLabel = new JLabel();

            luminanceLabel.setText(String.format(LUMFORMAT,
                    Integer.valueOf(data.getLuminance())));
            luminanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

            luminanceLabel.setName(LUMINANCE);
            luminanceLabel.setOpaque(false);
            luminanceLabel.setFont(luminanceFont);
            luminanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            valuesPanel.add(luminanceLabel);
            valuesPanel.add(Box.createRigidArea(new Dimension(0, VSPACE)));

            labelsMap.put(LUMINANCE, luminanceLabel);
        }

        if (Float.isFinite(data.getPressure())) {
            final JLabel pressureLabel = new JLabel();

            pressureLabel.setText(String.format(PRESSFORMAT,
                    Float.valueOf(data.getPressure())));
            pressureLabel.setHorizontalAlignment(SwingConstants.CENTER);

            pressureLabel.setName(PRESSURE);
            pressureLabel.setOpaque(false);
            pressureLabel.setFont(pressureFont);
            pressureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            valuesPanel.add(pressureLabel);
            valuesPanel.add(Box.createRigidArea(new Dimension(0, VSPACE)));

            labelsMap.put(PRESSURE, pressureLabel);
        }

        if (data.getVoc() > Integer.MIN_VALUE) {
            final JLabel vocLabel = new JLabel();

            vocLabel.setText(
                    String.format(VOCFORMAT, Integer.valueOf(data.getVoc())));
            vocLabel.setHorizontalAlignment(SwingConstants.CENTER);

            vocLabel.setName(VOC);
            vocLabel.setOpaque(false);
            vocLabel.setFont(vocFont);
            vocLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            valuesPanel.add(vocLabel);
            valuesPanel.add(Box.createRigidArea(new Dimension(0, VSPACE)));

            labelsMap.put(VOC, vocLabel);
        }

        final JLabel timeLabel = new JLabel();

        timeLabel.setText(data.getTimestamp());
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        timeLabel.setName(TIME);
        timeLabel.setOpaque(false);
        timeLabel.setFont(timeFont);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        valuesPanel.add(timeLabel);
        valuesPanel.add(Box.createRigidArea(new Dimension(0, VSPACE)));

        labelsMap.put(TIME, timeLabel);

        valuesPanel.updateUI();
        valuesPanel.validate();

        sensorPanel.updateUI();
        sensorPanel.validate();

        // sortPanels adds the sensorPanel to the mainPanel
        sortPanels(sensorPanel);

        frame.pack();

        frame.setVisible(true);

        log.debug("component count: {}",
                Integer.valueOf(mainPanel.getComponentCount()));

        if (log.isDebugEnabled()) {
            for (final Component component : mainPanel.getComponents()) {
                log.debug("component: {}", component.getName());

                if (component instanceof JPanel) {
                    for (final Component componentInner : ((JPanel) component)
                            .getComponents()) {
                        log.debug("componentInner: {}, {}",
                                componentInner.getName(),
                                componentInner.getClass());
                    }
                }
            }
        }
    }

    private void sortPanels(final JPanel sensorPanel) {
        final List<Component> panelList = new ArrayList<>();

        panelList.addAll(Arrays.asList(mainPanel.getComponents()));
        panelList.add(sensorPanel);

        Collections.sort(panelList, new Comparator<Component>() {
            @Override
            public int compare(final Component c1, final Component c2) {
                return ((JPanel) c1).getName()
                        .compareTo(((JPanel) c2).getName());
            }
        });

        mainPanel.removeAll();

        for (final Component panel : panelList) {
            log.debug("panel: {}", panel.getName());
            mainPanel.add(panel);
        }
    }

    @Override
    public void updateSensor(final SensorData data) {
        if (!panelsMap.containsKey(data.getSensorName())) {
            log.warn("missing label: {}", data.getSensorName());

            return;
        }

        log.debug("updating: {}", data.getSensorName());

        final Map<String, JComponent> labelsMap = panelsMap
                .get(data.getSensorName());

        final JButton temperatureLabel = (JButton) labelsMap.get(TEMPERATURE);

        final String temperature = String.format(TFORMAT,
                Double.valueOf(data.getTemperatureShow()),
                data.getTemperatureLetter());
        temperatureLabel.setText(temperature);

        if (Float.isFinite(data.getHumidity())) {
            final JLabel humidityLabel = (JLabel) labelsMap.get(HUMIDITY);

            if (humidityLabel != null) {
                final String humidity = String.format(HFORMAT,
                        Double.valueOf(data.getHumidity()));
                humidityLabel.setText(humidity);
            }
        }

        if (data.getLuminance() > Integer.MIN_VALUE) {
            final JLabel luminanceLabel = (JLabel) labelsMap.get(LUMINANCE);

            if (luminanceLabel != null) {
                final String luminance = String.format(LUMFORMAT,
                        Integer.valueOf(data.getLuminance()));
                luminanceLabel.setText(luminance);
            }
        }

        if (Float.isFinite(data.getPressure())) {
            final JLabel pressureLabel = (JLabel) labelsMap.get(PRESSURE);

            if (pressureLabel != null) {
                final String pressure = String.format(PRESSFORMAT,
                        Double.valueOf(data.getPressure()));
                pressureLabel.setText(pressure);
            }
        }

        if (data.getVoc() > Integer.MIN_VALUE) {
            final JLabel vocLabel = (JLabel) labelsMap.get(VOC);

            if (vocLabel != null) {
                final String voc = String.format(VOCFORMAT,
                        Integer.valueOf(data.getVoc()));
                vocLabel.setText(voc);
            }
        }

        final JLabel timeLabel = (JLabel) labelsMap.get(TIME);
        timeLabel.setText(data.getTimestamp());

        mainPanel.updateUI();
        mainPanel.validate();
    }

    private void nameButtonPressed(final JPanel valuesPanel) {
        valuesPanel.setVisible(!valuesPanel.isVisible());

        frame.pack();
    }
}
