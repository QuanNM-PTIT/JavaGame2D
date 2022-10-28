package UI;

import GameStates.Gamestate;
import Main.Game;
import Utilz.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import static Utilz.Constants.UI.Buttons.*;

public class MenuButton
{
    private int xPos, yPos, rowIdx, idx;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    private ArrayList<BufferedImage> imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIdx, Gamestate state)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIdx = rowIdx;
        this.state = state;
        loadImages();
        initBounds();
    }

    private void initBounds()
    {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImages()
    {
        imgs = new ArrayList<BufferedImage>();
        BufferedImage tmp = LoadSave.GetPlayerAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < 3; ++i)
            imgs.add(tmp.getSubimage(i * B_WIDTH_DEFAULT, rowIdx * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT));
    }

    public void draw(Graphics g)
    {
        g.drawImage(imgs.get(idx), xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update()
    {
        idx = 0;
        if (mouseOver)
            idx = 1;
        if (mousePressed)
            idx = 2;
    }

    public Rectangle getBounds()
    {
        return bounds;
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

    public void applyGameState()
    {
        Gamestate.state = state;
    }

    public void resetBools()
    {
        mouseOver = mousePressed = false;
    }
}
