package Main;

import Inputs.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static Utilz.Constants.PlayerConstants.*;
import static Utilz.Constants.Directions.*;

public class GamePanel extends JPanel
{
    private MouseInputs mouseInputs;
    private BufferedImage img;
    private float deltaX = 0f, deltaY = 0f;
    private int r = 0, g = 0, b = 0;
    private ArrayList<ArrayList<BufferedImage>> animation;
    private int aniTick, aniIdx, aniSpeed = 15;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false;

    public GamePanel()
    {
        mouseInputs = new MouseInputs();
        importImage();
        setPanelSize();
        loadAnimation();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void loadAnimation()
    {
        animation = new ArrayList<ArrayList<BufferedImage>>();
        for (int i = 0; i < 9; ++i)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<BufferedImage>();
            for (int j = 0; j < 6; ++j)
                tmp.add(img.getSubimage(j * 64, i * 40, 64, 40));
            animation.add(tmp);
        }
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

    public void setDirection(int direction)
    {
        this.playerDir = direction;
        moving = true;
    }

    public void setMoving(boolean moving)
    {
        this.moving = moving;
    }

    private void updateAnimation()
    {
        ++aniTick;
        if (aniTick >= aniSpeed)
        {
            aniTick = 0;
            ++aniIdx;
            if (aniIdx >= GetSpriteAmount(playerAction))
                aniIdx = 0;
        }
    }

    public void setAnimation()
    {
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
    }

    public void updatePos()
    {
        if (moving)
        {
            switch (playerDir)
            {
                case UP:
                    deltaY -= 1;
                    break;
                case DOWN:
                    deltaY += 1;
                    break;
                case LEFT:
                    deltaX -= 1;
                    break;
                case RIGHT:
                    deltaX += 1;
                    break;
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        updateAnimation();
        setAnimation();
        updatePos();
        g.drawImage((animation.get(playerAction)).get(aniIdx), (int) deltaX, (int) deltaY, 128, 80, null);
    }
}
