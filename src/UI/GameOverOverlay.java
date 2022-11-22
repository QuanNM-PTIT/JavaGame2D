package UI;

import GameStates.Gamestate;
import GameStates.Playing;
import Main.Game;
import Utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Utilz.Constants.UI.URMButtons.URM_SIZE;

public class GameOverOverlay
{
    private Playing playing;
    private int imgX, imgY, imgW, imgH;
    private BufferedImage img;
    private UrmButton menu, play;

    public GameOverOverlay(Playing playing)
    {
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons()
    {
        int menuX = (int) (330 * Game.SCALE);
        int playX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void createImg()
    {
        img = LoadSave.GetPlayerAtlas(LoadSave.DEATH_SCREEN);
        imgW = (int) (img.getWidth() * Game.SCALE);
        imgH = (int) (img.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (100 * Game.SCALE);
    }

    public void draw(Graphics g)
    {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        menu.draw(g);
        play.draw(g);
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }

    private boolean isIn(UrmButton b, MouseEvent e)
    {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e)
    {
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(play, e))
            play.setMouseOver(true);
        else if (isIn(menu, e))
            menu.setMouseOver(true);
    }

    public void update()
    {
        menu.update();
        play.update();
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
        else if (isIn(play, e))
        {
            if (play.isMousePressed())
                playing.resetAll();
        }

        menu.resetBools();
        play.resetBools();
    }

    public void mousePressed(MouseEvent e)
    {
        if (isIn(play, e))
            play.setMousePressed(true);
        else if (isIn(menu, e))
            menu.setMousePressed(true);
    }
}
