package UI;

import GameStates.Gamestate;
import GameStates.Playing;
import Main.Game;
import Utilz.LoadSave;

import static Utilz.Constants.UI.PauseButtons.*;
import static Utilz.Constants.UI.URMButtons.*;
import static Utilz.Constants.UI.VolumeButtons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverlay
{
    private Playing playing;
    private AudioOptions audioOptions;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuButton, replayButton, unpauseButton;

    public PauseOverlay(Playing playing)
    {
        this.playing = playing;
        audioOptions = playing.getGame().getAudioOptions();
        loadBackground();
        createUrmButtons();
    }

    private void loadBackground()
    {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    private void createUrmButtons()
    {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int buttonY = (int) (325 * Game.SCALE);
        unpauseButton = new UrmButton(unpauseX, buttonY, URM_SIZE, URM_SIZE, 0);
        replayButton = new UrmButton(replayX, buttonY, URM_SIZE, URM_SIZE, 1);
        menuButton = new UrmButton(menuX, buttonY, URM_SIZE, URM_SIZE, 2);
    }

    public void update()
    {
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        audioOptions.update();
    }

    public void draw(Graphics g)
    {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);
        audioOptions.draw(g);
    }

    public void mouseClicked(MouseEvent e)
    {

    }

    public void mouseDragged(MouseEvent e)
    {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e)
    {
        if (isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if (isIn(e, replayButton))
            replayButton.setMousePressed(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        if (isIn(e, menuButton))
        {
            if (menuButton.isMousePressed())
            {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
            }
        }
        else if (isIn(e, replayButton))
        {
            if (replayButton.isMousePressed())
            {
                playing.resetAll();
                playing.unpauseGame();
            }
        }
        else if (isIn(e, unpauseButton))
        {
            if (unpauseButton.isMousePressed())
                playing.unpauseGame();
        }
        else
            audioOptions.mouseReleased(e);

        menuButton.resetBools();
        unpauseButton.resetBools();
        replayButton.resetBools();
    }

    public void mouseMoved(MouseEvent e)
    {
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);

        if (isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMouseOver(true);
        else if (isIn(e, replayButton))
            replayButton.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    private boolean isIn(MouseEvent e, PauseButton p)
    {
        return p.getBounds().contains(e.getX(), e.getY());
    }
}
