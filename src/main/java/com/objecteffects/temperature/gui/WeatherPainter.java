package com.objecteffects.temperature.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class WeatherPainter {
    private final static Logger log = LogManager
            .getLogger(WeatherPainter.class);

    final static int PANELWIDTH = 640;
    final static int PANELHEIGHT = 480;

    private final JFrame frame = new JFrame("temperatures");

    private static WeatherPainter wp = null;

    public void createWindow() {
        log.debug("weatherPainter");

        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        addMenuBar();

        final JPanel contentPane = new JPanel();

        contentPane
                .setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 8));
        contentPane.setDoubleBuffered(true);
        contentPane.setPreferredSize(
                new Dimension(PANELWIDTH + 20, PANELHEIGHT + 20));

        this.frame.setContentPane(contentPane);

//        contentPane.setVisible(true);

        addGlassPane();

        addLayeredPane(contentPane);

        this.frame.pack();
        this.frame.setVisible(true);
    }

    private void addLayeredPane(final JPanel contentPane) {
        final JLayeredPane layeredPane = new JLayeredPane();

        layeredPane.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));

        contentPane.add(layeredPane);

        addGraphPanels(layeredPane);

//        layeredPane.setVisible(true);
    }

    private void addGlassPane() {
        final GlassPane glassPane = new GlassPane();

        glassPane.setBounds(0, 0, PANELWIDTH, PANELHEIGHT);

        this.frame.setGlassPane(glassPane);

        // the missing piece; must be called after
        // setting the frame's glass pane.
        glassPane.setVisible(true);
    }

    void addMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(154, 165, 127));
        menuBar.setPreferredSize(new Dimension(PANELWIDTH, 20));

        this.frame.setJMenuBar(menuBar);

//        menuBar.setVisible(true);
    }

    void addGraphPanels(final JLayeredPane layeredPane) {
        final GraphPanel1 testGraphPanel1 = new GraphPanel1();
        testGraphPanel1.setBounds(0, 0, PANELWIDTH, PANELHEIGHT);

        layeredPane.add(testGraphPanel1, Integer.valueOf(8));
//        graphPanel1.setVisible(true);

        final GraphPanel2 testGraphPanel2 = new GraphPanel2();
        testGraphPanel2.setBounds(0, 0, PANELWIDTH, PANELHEIGHT);

        layeredPane.add(testGraphPanel2, Integer.valueOf(12));
//        graphPanel2.setVisible(true);
    }

    void dispose() {
        this.frame.dispose();
    }

    boolean isShowing() {
        return this.frame.isShowing();
    }

    static void temperatureLabelPressed() {
        if (wp != null) {
            wp.dispose();
            wp = null;

            return;
        }

        wp = new WeatherPainter();

        wp.createWindow();
    }
}
