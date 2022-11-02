import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TestPane extends JPanel {
    int x = 0,y = 0,radius = 20,xDelta = 5, yDelta =4;

    public TestPane() {
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x += xDelta;
                y += yDelta;
                if (x + (radius * 2) > getWidth()) {
                    x = getWidth() - (radius * 2);
                    xDelta *= -1;
                } else if (x < 0) {
                    x = 0;
                    xDelta *= -1;

                }
                if (y + (radius * 2) > getWidth()) {
                    y = getWidth() - (radius * 2);
                    yDelta *= -1;
                } else if (y < 0) {
                    y = 0;
                    yDelta *= -1;
                }
                repaint();
            }
        });
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x, y - radius, radius * 2, radius * 2);
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.setRenderingHints(rh);
    }
}