package Main;

import Inputs.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel
{
    private MouseInputs mouseInputs;
    private float deltaX = 100f, deltaY = 100f;
    private float xDir = 0.05f, yDir = 0.05f;
    private int r = 0, g = 0, b = 0;
    private Random rnd;
    private Color color;

    public GamePanel()
    {
        rnd = new Random();
        color = new Color(r, g, b);
        mouseInputs = new MouseInputs();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void changeDeltaX(int val)
    {
        this.deltaX += val;
    }

    public void changeDeltaY(int val)
    {
        this.deltaY += val;
    }

    private Color getRndColor()
    {
        r = rnd.nextInt(255);
        g = rnd.nextInt(255);
        b = rnd.nextInt(255);
        return new Color(r, g, b);
    }

    private void updateRectangle()
    {
        deltaX += xDir;
        if (deltaX >= 500 || deltaX <= 0)
        {
            xDir *= -1;
            color = getRndColor();
        }
        deltaY += yDir;
        if (deltaY >= 500 || deltaY <= 0)
        {
            yDir *= -1;
            color = getRndColor();
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        updateRectangle();
        g.setColor(color);
        g.fillRect((int) deltaX, (int) deltaY, 100, 100);
        repaint();
    }
}
