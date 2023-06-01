import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedTextField extends JTextField {
    private static final int ARC_WIDTH = 15;
    private static final int ARC_HEIGHT = 15;

    public RoundedTextField() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        Shape shape = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, ARC_WIDTH, ARC_HEIGHT);

        g2.setColor(getBackground());
        g2.fill(shape);
        g2.setColor(getForeground());
        g2.draw(shape);

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Remove border painting
    }
}
