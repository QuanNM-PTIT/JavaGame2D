package UI;

import Utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Utilz.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton
{

    private ArrayList<BufferedImage> imgs;
    private BufferedImage slider;
    private int idx = 0;
    private int buttonX, minX, maxX;
    private float floatVal = 0f;
    private boolean mouseOver, mousePressed;

    public VolumeButton(int x, int y, int width, int height)
    {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        buttonX = x + width / 2;
        bounds.x -= VOLUME_WIDTH / 2;
        this.x = x;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        this.width = width;
        loadImgs();
    }

    private void loadImgs()
    {
        BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new ArrayList<BufferedImage>();
        for (int i = 0; i < 3; ++i)
            imgs.add(img.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT));
        slider = img.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update()
    {
        idx = 0;
        if (mouseOver)
            idx = 1;
        else if (mousePressed)
            idx = 2;
    }

    public void draw(Graphics g)
    {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs.get(idx), buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x)
    {
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;
        updateFloatVal();
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    private void updateFloatVal()
    {
        float range = maxX - minX;
        float val = buttonX - minX;
        floatVal = val / range;
    }

    public void resetBools()
    {
        mouseOver = mousePressed = false;
    }

    public boolean isMouseOver()
    {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver)
    {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed()
    {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed)
    {
        this.mousePressed = mousePressed;
    }

    public float getFloatVal()
    {
        return floatVal;
    }
}
