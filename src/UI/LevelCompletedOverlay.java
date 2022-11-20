package UI;

import GameStates.Gamestate;
import GameStates.Playing;
import Main.Game;
import Utilz.LoadSave;

import static Utilz.Constants.UI.URMButtons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LevelCompletedOverlay
{
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int backgroundX, backgroundY, backgroundW, backgroundH;

    public LevelCompletedOverlay(Playing playing)
    {
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons()
    {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg()
    {
        img = LoadSave.GetPlayerAtlas(LoadSave.COMPLETED_IMG);
        backgroundW = (int) (img.getWidth() * Game.SCALE);
        backgroundH = (int) (img.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundW / 2;
        backgroundY = (int) (75 * Game.SCALE);
    }

    public void draw(Graphics g)
    {
        g.drawImage(img, backgroundX, backgroundY, backgroundW, backgroundH, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update()
    {
        next.update();
        menu.update();
    }

    private boolean isIn(UrmButton b, MouseEvent e)
    {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e)
    {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(next, e))
            next.setMouseOver(true);
        else if (isIn(menu, e))
            menu.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e)
    {
        if (isIn(menu, e))
        {
            if (menu.isMousePressed())
            {
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        }
        else if (isIn(next, e))
        {
            if (next.isMousePressed())
            {
                playing.loadNextLevel();
            }
        }

        menu.resetBools();
        next.resetBools();
    }

    public void mousePressed(MouseEvent e)
    {
        if (isIn(next, e))
            next.setMousePressed(true);
        else if (isIn(menu, e))
            menu.setMousePressed(true);
    }
}
