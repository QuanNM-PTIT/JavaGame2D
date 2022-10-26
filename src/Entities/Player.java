package Entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static Utilz.Constants.PlayerConstants.*;

public class Player extends Entity
{
    private ArrayList<ArrayList<BufferedImage>> animation;
    private int aniTick, aniIdx, aniSpeed = 15;
    private int playerAction = IDLE;
    private boolean moving = false;
    private boolean left, up, right, down;
    private float playerSpeed = 0.5f;

    public Player(float x, float y)
    {
        super(x, y);
        loadAnimation();
    }

    public void update()
    {
        updatePos();
        updateAnimation();
        setAnimation();
    }

    public void render(Graphics g)
    {
        g.drawImage((animation.get(playerAction)).get(aniIdx), (int) x, (int) y, 128, 80, null);
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
        moving = false;
        if (left && !right)
        {
            x -= playerSpeed;
            moving = true;
        }
        else if (!left && right)
        {
            x += playerSpeed;
            moving = true;
        }
        if (up && !down)
        {
            y -= playerSpeed;
            moving = true;
        }
        else if (!up && down)
        {
            y += playerSpeed;
            moving = true;
        }
    }

    private void loadAnimation()
    {
        InputStream inp = getClass().getResourceAsStream("/player_sprites.png");
        try
        {
            BufferedImage img = ImageIO.read(inp);
            animation = new ArrayList<ArrayList<BufferedImage>>();
            for (int i = 0; i < 9; ++i)
            {
                ArrayList<BufferedImage> tmp = new ArrayList<BufferedImage>();
                for (int j = 0; j < 6; ++j)
                    tmp.add(img.getSubimage(j * 64, i * 40, 64, 40));
                animation.add(tmp);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                inp.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public boolean isLeft()
    {
        return left;
    }

    public void setLeft(boolean left)
    {
        this.left = left;
    }

    public boolean isUp()
    {
        return up;
    }

    public void setUp(boolean up)
    {
        this.up = up;
    }

    public boolean isRight()
    {
        return right;
    }

    public void setRight(boolean right)
    {
        this.right = right;
    }

    public boolean isDown()
    {
        return down;
    }

    public void setDown(boolean down)
    {
        this.down = down;
    }
}
