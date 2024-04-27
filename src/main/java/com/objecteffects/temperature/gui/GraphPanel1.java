package com.objecteffects.temperature.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.objecteffects.temperature.math.MathUtils;

@SuppressWarnings("serial")
class GraphPanel1 extends JPanel {
    private final static Logger log = LogManager.getLogger(GraphPanel1.class);

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WeatherPainter.PANELWIDTH,
                WeatherPainter.PANELHEIGHT);
    }

    @Override
    @SuppressWarnings("boxing")
    public void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g.create();

        super.paintComponent(g2d);

        /* moves Y origin to bottom */
        g2d.translate(0, getHeight() - 1);

        setOpaque(false);

        g2d.setStroke(new BasicStroke(3f));

        g2d.setColor(new Color(0, 128 - 32, 0));

        int y = 0;

        for (int i = 0; i < 100; i = i + 10) {
            final int newY = MathUtils.mapRange(i, 0, 100, 0, -479);
            log.debug("newY: {}", newY);

            g2d.fillRoundRect(0 + y, newY, 10, 10, 5, 5);

            y = y + 10;
        }

        g2d.fillRoundRect(100, -130, 80, 100, 8, 8);

        g2d.setFont(new Font("Dialog", Font.PLAIN, 24));
        g2d.drawString("TestGraphPanel1?", 200, -100);

        g2d.dispose();
    }
}
