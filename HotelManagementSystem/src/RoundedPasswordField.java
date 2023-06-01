import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPasswordField extends JPasswordField {
    private static final int ARC_WIDTH = 15;
    private static final int ARC_HEIGHT = 15;

    public RoundedPasswordField() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g3 = (Graphics2D) g.create();
        g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        Shape shape = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, ARC_WIDTH, ARC_HEIGHT);

        g3.setColor(getBackground());
        g3.fill(shape);
        g3.setColor(getForeground());
        g3.draw(shape);

        g3.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Remove border painting
    }
}
