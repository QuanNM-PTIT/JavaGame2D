package Main;

import Inputs.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel
{
    private MouseInputs mouseInputs;
    private BufferedImage img;
    private float deltaX = 100f, deltaY = 100f;
    private int r = 0, g = 0, b = 0;

    public GamePanel()
    {
        mouseInputs = new MouseInputs();
        importImage();
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void importImage()
    {
        InputStream inp = getClass().getResourceAsStream("/player_sprites.png");
        try
        {
            img = ImageIO.read(inp);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void setPanelSize()
    {
        Dimension size = new Dimension(1280, 800);
        setMaximumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void changeDeltaX(int val)
    {
        this.deltaX += val;
    }

    public void changeDeltaY(int val)
    {
        this.deltaY += val;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img.getSubimage(0, 0, 64, 40), 0, 0, 128, 80, null);
    }
}
