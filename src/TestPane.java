import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TestPane extends JPanel {
    int x = 0,y = 100,radius = 20,xDelta = 10;

    public TestPane() {
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x += xDelta;
                if (x + (radius * 2) > getWidth()) {
                    x = getWidth() - (radius * 2);
                    xDelta *= -1;
                } else if (x < 0) {
                    x = 0;
                    xDelta *= -1;
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
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillOval(x, y - radius, radius * 2, radius * 2);
    }
}