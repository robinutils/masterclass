import javax.swing.*;
import java.awt.*;

public class CircleLabelUI extends javax.swing.plaf.basic.BasicLabelUI {
    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension size = c.getSize();
        g2.setColor(c.getBackground());
        g2.fillOval(0, 0, size.width, size.height);
        super.paint(g, c);
    }
}
