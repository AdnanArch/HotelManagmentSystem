package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private static final int ARC_WIDTH = 15;
    private static final int ARC_HEIGHT = 15;
    private Color backgroundColor;
    private Color foregroundColor;
    private Font buttonFont;

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        buttonFont = new Font("Sans-serif", Font.PLAIN, 18);
        setButtonColors(new Color(52, 152, 219), Color.WHITE);
        setButtonFont(buttonFont);


        setBackground(backgroundColor);
        setForeground(foregroundColor);
        setFont(buttonFont);
        setFocusable(false);
    }

    public void setButtonColors(Color backgroundColor, Color foregroundColor) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        repaint();
    }

    public void setButtonFont(Font font) {
        this.buttonFont = font;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        Shape shape = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, ARC_WIDTH, ARC_HEIGHT);

        g2.setColor(backgroundColor);
        g2.fill(shape);
        g2.setColor(foregroundColor);
        g2.draw(shape);

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Paint the border
        g.setColor(foregroundColor);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
    }

}
