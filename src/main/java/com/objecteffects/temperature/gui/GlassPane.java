package com.objecteffects.temperature.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.math.MathUtils;

class GlassPane extends JComponent {
    private final static Logger log = LogManager.getLogger(GlassPane.class);
    private static final long serialVersionUID = 1L;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WeatherPainter.PANELWIDTH,
                WeatherPainter.PANELHEIGHT);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        log.debug("glassPane");

        final Graphics2D g2d = (Graphics2D) g;
//        final Graphics2D g2d = (Graphics2D) g.create();
//        final Graphics2D g2d = (Graphics2D) g.create(0, 0,
//                WeatherPainter.PANELWIDTH, WeatherPainter.PANELHEIGHT);

        super.paintComponent(g);

        g2d.translate(0, getHeight() - 1);

        setOpaque(true);
        setBackground(Color.RED);

        g2d.setStroke(new BasicStroke(3f));

        g2d.setColor(new Color(0, 128 - 32, 0));

        int y = 0;

        for (int i = 0; i < 100; i = i + 4) {
            final int newY = yLoc(i);
            log.debug("newY: {}", Integer.valueOf(newY));

            g2d.fillOval(0 + y, newY, 4, 4);

            y = y + 10;
        }

        g2d.fillOval(100, yLoc(80), 80, 80);

        g2d.setColor(Color.BLUE);

        g2d.setFont(new Font("Dialog", Font.PLAIN, 24));
        g2d.drawString("Glass pane text?", 60, yLoc(90));

        setVisible(true);
    }

    private int yLoc(final int y) {
        return MathUtils.mapRange(y, 0, 100, 0,
                -WeatherPainter.PANELHEIGHT - 1);
    }
}
