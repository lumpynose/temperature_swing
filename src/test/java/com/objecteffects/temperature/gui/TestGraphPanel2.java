package com.objecteffects.temperature.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class TestGraphPanel2 extends JPanel {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private final static Logger log = LogManager.getLogger(GraphPanel2.class);

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WeatherPainter.PANELWIDTH,
                WeatherPainter.PANELHEIGHT);
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g.create();

        super.paintComponent(g2d);

        /* moves Y origin to bottom */
        g2d.translate(0, getHeight() - 1);

        g2d.setColor(new Color(128, 8, 0));

        g2d.setFont(new Font("Dialog", Font.PLAIN, 24));
        g2d.drawString("This is my custom Panel2?",
                WeatherPainter.PANELWIDTH / 2, -WeatherPainter.PANELHEIGHT / 2);

        setOpaque(false);

        g2d.setStroke(new BasicStroke(3f));

        g2d.draw(new RoundRectangle2D.Float(110, -290, 120, 100, 10, 10));
        g2d.draw(new RoundRectangle2D.Float(150, -250, 110, 10, 5, 5));
    }
}
