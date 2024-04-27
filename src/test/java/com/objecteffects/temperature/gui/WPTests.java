package com.objecteffects.temperature.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.jupiter.api.Test;

final class WPTests {
    final static int PANELWIDTH = 640;
    final static int PANELHEIGHT = 480;

    final JFrame frame;

    WPTests() {
        this.frame = new JFrame("temperatures");
    }

    public void createGui() {
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addMenuBar();

        final JPanel contentPane = new JPanel();

        contentPane.setDoubleBuffered(true);

        this.frame.setContentPane(contentPane);

        final JLayeredPane layeredPane = new JLayeredPane();

        layeredPane.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));

        contentPane.add(layeredPane);

        addGraphPanels(layeredPane);

        layeredPane.setVisible(true);

        this.frame.pack();
        this.frame.setVisible(true);
    }

    void addMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(154, 165, 64));
        menuBar.setPreferredSize(new Dimension(PANELWIDTH, 20));

        this.frame.setJMenuBar(menuBar);
    }

    void addGraphPanels(final JLayeredPane layeredPane) {
        final TestGraphPanel1 testGraphPanel1 = new TestGraphPanel1();
        testGraphPanel1.setSize(PANELWIDTH, PANELHEIGHT);
        testGraphPanel1.setLocation(0, 0);

        layeredPane.add(testGraphPanel1, Integer.valueOf(8));

        final TestGraphPanel2 testGraphPanel2 = new TestGraphPanel2();
        testGraphPanel2.setBounds(0, 0, PANELWIDTH, PANELHEIGHT);

        layeredPane.add(testGraphPanel2, Integer.valueOf(12));
    }

    void dispose() {
        this.frame.dispose();
    }

    boolean isShowing() {
        return this.frame.isShowing();
    }

    @Test
    void WPTest() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final WPTests wp = new WPTests();
                wp.createGui();
            }
        });

        try {
            Thread.sleep(1000 * 300);
        }
        catch (@SuppressWarnings("unused") final InterruptedException e) {
            return;
        }
    }
}
