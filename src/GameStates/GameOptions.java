package GameStates;

import Main.Game;
import UI.AudioOptions;
import UI.PauseButton;
import UI.UrmButton;
import Utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Utilz.Constants.UI.URMButtons.*;

public class GameOptions extends State implements Statemethods
{
    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuButton;

    public GameOptions(Game game)
    {
        super(game);
        loadImgs();
        loadButtons();
        audioOptions = game.getAudioOptions();
    }

    private void loadButtons()
    {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (325 * Game.SCALE);
        menuButton = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadImgs()
    {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BACKGROUND);
        bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);
    }

    @Override
    public void update()
    {
        menuButton.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);
        menuButton.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e)
    {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (isIn(e, menuButton))
        {
            menuButton.setMousePressed(true);
        }
        else
            audioOptions.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (isIn(e, menuButton))
        {
            if (menuButton.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        }
        else
            audioOptions.mouseReleased(e);
        menuButton.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        menuButton.setMouseOver(false);
        if (isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }

    private boolean isIn(MouseEvent e, PauseButton p)
    {
        return p.getBounds().contains(e.getX(), e.getY());
    }
}
