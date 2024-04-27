package com.objecteffects.temperature.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
 * This applet renders a shape, selected by the user, with a paint,stroke, and rendering method,
 * also selected by the user.
*/

public class Transform extends JApplet implements ItemListener, ActionListener {
    private static final long serialVersionUID = 1L;
    JLabel primLabel, lineLabel, paintLabel, transLabel, strokeLabel;
    TransPanel display;
    static JComboBox<?> primitive, line, paint, trans, stroke;
    JButton redraw;
    public static boolean no2D = false;

    @SuppressWarnings("deprecation")
    @Override
    public void init() {
        final GridBagLayout layOut = new GridBagLayout();
        getContentPane().setLayout(layOut);
        final GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        this.primLabel = new JLabel();
        this.primLabel.setText("Primitive");
        Font newFont = getFont().deriveFont(1);
        this.primLabel.setFont(newFont);
        this.primLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layOut.setConstraints(this.primLabel, c);
        getContentPane().add(this.primLabel);

        this.lineLabel = new JLabel();
        this.lineLabel.setText("Lines");
        this.lineLabel.setFont(newFont);
        this.lineLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layOut.setConstraints(this.lineLabel, c);
        getContentPane().add(this.lineLabel);

        this.paintLabel = new JLabel();
        this.paintLabel.setText("Paints");
        this.paintLabel.setFont(newFont);
        this.paintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layOut.setConstraints(this.paintLabel, c);
        getContentPane().add(this.paintLabel);

        c.gridwidth = GridBagConstraints.RELATIVE;
        this.transLabel = new JLabel();
        this.transLabel.setText("Transforms");
        this.transLabel.setFont(newFont);
        this.transLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layOut.setConstraints(this.transLabel, c);
        getContentPane().add(this.transLabel);

        c.gridwidth = GridBagConstraints.REMAINDER;
        this.strokeLabel = new JLabel();
        this.strokeLabel.setText("Rendering");
        this.strokeLabel.setFont(newFont);
        this.strokeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layOut.setConstraints(this.strokeLabel, c);
        getContentPane().add(this.strokeLabel);

        final GridBagConstraints ls = new GridBagConstraints();
        ls.weightx = 1.0;
        ls.fill = GridBagConstraints.BOTH;
        primitive = new JComboBox<>(
                new Object[] { "rectangle", "ellipse", "text" });
        primitive.addItemListener(this);
        newFont = newFont.deriveFont(0, 14.0f);
        primitive.setFont(newFont);
        layOut.setConstraints(primitive, ls);
        getContentPane().add(primitive);

        line = new JComboBox<>(
                new Object[] { "thin", "thick", "dashed" });
        line.addItemListener(this);
        line.setFont(newFont);
        layOut.setConstraints(line, ls);
        getContentPane().add(line);

        paint = new JComboBox<>(
                new Object[] { "solid", "gradient", "polka" });
        paint.addItemListener(this);
        paint.setFont(newFont);
        layOut.setConstraints(paint, ls);
        getContentPane().add(paint);

        ls.gridwidth = GridBagConstraints.RELATIVE;

        trans = new JComboBox<>(
                new Object[] { "Identity", "rotate", "scale", "shear" });
        trans.addItemListener(this);
        trans.setFont(newFont);
        layOut.setConstraints(trans, ls);
        getContentPane().add(trans);

        ls.gridwidth = GridBagConstraints.REMAINDER;
        stroke = new JComboBox<>(
                new Object[] { "Stroke", "Fill", "Stroke & Fill" });
        stroke.addItemListener(this);
        stroke.setFont(newFont);
        layOut.setConstraints(stroke, ls);
        getContentPane().add(stroke);

        final GridBagConstraints button = new GridBagConstraints();
        button.gridwidth = GridBagConstraints.REMAINDER;
        this.redraw = new JButton("Redraw");
        this.redraw.addActionListener(this);
        this.redraw.setFont(newFont);
        layOut.setConstraints(this.redraw, button);
        getContentPane().add(this.redraw);

        final GridBagConstraints tP = new GridBagConstraints();
        tP.fill = GridBagConstraints.BOTH;
        tP.weightx = 1.0;
        tP.weighty = 1.0;
        tP.gridwidth = GridBagConstraints.REMAINDER;
        this.display = new TransPanel();
        layOut.setConstraints(this.display, tP);
        this.display.setBackground(Color.white);
        getContentPane().add(this.display);

        validate();

    }

    @Override
    public void itemStateChanged(final ItemEvent e) {
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        this.display.setTrans(trans.getSelectedIndex());
        this.display.renderShape();
    }

    public static void main(final String[] argv) {
        if (argv.length > 0 && argv[0].equals("-no2d")) {
            Transform.no2D = true;
        }

        final JFrame frame = new JFrame("Transform");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }
        });

        final JApplet applet = new Transform();
        frame.getContentPane().add(BorderLayout.CENTER, applet);

        applet.init();

        frame.setSize(550, 400);
        frame.setVisible(true);
    }

}

class TransPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    AffineTransform at = new AffineTransform();
    int w, h;
    Shape shapes[] = new Shape[3];
    BufferedImage bi;
    boolean firstTime = true;

    public TransPanel() {
        setBackground(Color.white);
        this.shapes[0] = new Rectangle(0, 0, 100, 100);
        this.shapes[1] = new Ellipse2D.Double(0.0, 0.0, 100.0, 100.0);
        final TextLayout textTl = new TextLayout("Text",
                new Font("Helvetica", 1, 96),
                new FontRenderContext(null, false, false));
        final AffineTransform textAt = new AffineTransform();
        textAt.translate(0, (float) textTl.getBounds().getHeight());
        this.shapes[2] = textTl.getOutline(textAt);
    }

    public void setTrans(final int transIndex) {
        // Sets the AffineTransform.
        switch (transIndex) {
        case 0:
            this.at.setToIdentity();
            this.at.translate(this.w / 2, this.h / 2);
            break;
        case 1:
            this.at.rotate(Math.toRadians(45));
            break;
        case 2:
            this.at.scale(0.5, 0.5);
            break;
        case 3:
            this.at.shear(0.5, 0.0);
            break;
        }
    }

    public void renderShape() {
        repaint();
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        if (!Transform.no2D) {
            final Graphics2D g2 = (Graphics2D) g;
            final Dimension d = getSize();
            this.w = d.width;
            this.h = d.height;

            // Prints out the intructions.
            String instruct = "Pick a primitive, line style, paint, transform,";
            TextLayout thisTl = new TextLayout(instruct,
                    new Font("Helvetica", 0, 10), g2.getFontRenderContext());
            float width = (float) thisTl.getBounds().getWidth();
            final float height = (float) thisTl.getBounds().getHeight();
            thisTl.draw(g2, this.w / 2 - width / 2, 15);

            instruct = "and rendering method and click the Redraw button.";
            thisTl = new TextLayout(instruct, new Font("Helvetica", 0, 10),
                    g2.getFontRenderContext());
            width = (float) thisTl.getBounds().getWidth();
            thisTl.draw(g2, this.w / 2 - width / 2, height + 17);

            // Initialize the transform.
            if (this.firstTime) {
                this.at.setToIdentity();
                this.at.translate(this.w / 2, this.h / 2);
                this.firstTime = false;
            }

            // Sets the Stroke.
            final Stroke oldStroke = g2.getStroke();

            switch (Transform.line.getSelectedIndex()) {
            case 0:
                g2.setStroke(new BasicStroke(3.0f));
                break;
            case 1:
                g2.setStroke(new BasicStroke(8.0f));
                break;
            case 2:
                final float dash[] = { 10.0f };
                g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
                break;
            }

            // Sets the Paint.
            final Paint oldPaint = g2.getPaint();

            switch (Transform.paint.getSelectedIndex()) {
            case 0:
                g2.setPaint(Color.blue);
                break;
            case 1:
                g2.setPaint(new GradientPaint(0, 0, Color.lightGray,
                        this.w - 250, this.h, Color.blue, false));
                break;
            case 2:
                final BufferedImage buffi = new BufferedImage(15, 15,
                        BufferedImage.TYPE_INT_RGB);
                final Graphics2D buffig = buffi.createGraphics();
                buffig.setColor(Color.blue);
                buffig.fillRect(0, 0, 15, 15);
                buffig.setColor(Color.lightGray);
                buffig.translate(15 / 2 - 5 / 2, 15 / 2 - 5 / 2);
                buffig.fillOval(0, 0, 7, 7);
                final Rectangle r = new Rectangle(0, 0, 25, 25);
                g2.setPaint(new TexturePaint(buffi, r));
                break;
            }

            // Sets the Shape.
            final Shape shape = this.shapes[Transform.primitive
                    .getSelectedIndex()];
            final Rectangle r = shape.getBounds();

            // Sets the selected Shape to the center of the Canvas.
            final AffineTransform saveXform = g2.getTransform();
            final AffineTransform toCenterAt = new AffineTransform();
            toCenterAt.concatenate(this.at);
            toCenterAt.translate(-(r.width / 2), -(r.height / 2));

            g2.transform(toCenterAt);

            // Sets the rendering method.
            switch (Transform.stroke.getSelectedIndex()) {
            case 0:
                g2.draw(shape);
                break;
            case 1:
                g2.fill(shape);
                break;
            case 2:
                final Graphics2D tempg2 = g2;
                g2.fill(shape);
                g2.setColor(Color.darkGray);
                g2.draw(shape);
                g2.setPaint(tempg2.getPaint());
                break;
            }

            g2.setStroke(oldStroke);
            g2.setPaint(oldPaint);
            g2.setTransform(saveXform);

        }
    }
}
