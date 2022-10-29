package UI;

import Utilz.LoadSave;
import static Utilz.Constants.UI.URMButtons.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UrmButton extends PauseButton
{
    private ArrayList<BufferedImage> imgs;
    private int rowIdx, idx;
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIdx)
    {
        super(x, y, width, height);
        this.rowIdx = rowIdx;
        loadImgs();
    }

    private void loadImgs()
    {
        BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.URM_BUTTONS);
        imgs = new ArrayList<BufferedImage>();
        for (int i = 0; i < 3; ++i)
            imgs.add(img.getSubimage(i * URM_SIZE_DEFAULT, rowIdx * URM_SIZE_DEFAULT, URM_SIZE_DEFAULT, URM_SIZE_DEFAULT));
    }

    public void update()
    {
        idx = 0;
        if (mouseOver)
            idx = 1;
        if (mousePressed)
            idx = 2;
    }

    public void draw(Graphics g)
    {
        g.drawImage(imgs.get(idx), x, y, URM_SIZE, URM_SIZE, null);
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
}
