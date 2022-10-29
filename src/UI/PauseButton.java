package UI;

import java.awt.*;

public class PauseButton
{
    protected int x, y, width, height;
    protected Rectangle bounds;

    public PauseButton(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds()
    {
        bounds = new Rectangle(x, y, width, height);
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public void setBounds(Rectangle bounds)
    {
        this.bounds = bounds;
    }
}
