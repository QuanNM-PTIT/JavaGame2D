package GameStates;

import Main.Game;
import UI.MenuButton;
import Utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Menu extends State implements Statemethods
{

    private ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
    private BufferedImage backgroundImage, menuBackgroundImage;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game)
    {
        super(game);
        loadButton();
        loadBackground();
        menuBackgroundImage = LoadSave.GetPlayerAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadBackground()
    {
        backgroundImage = LoadSave.GetPlayerAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImage.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImage.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadButton()
    {
        buttons.add(new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, Gamestate.PLAYING));
        buttons.add(new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, Gamestate.OPTIONS));
        buttons.add(new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, Gamestate.QUIT));
    }

    @Override
    public void update()
    {
        for (MenuButton mb : buttons)
            mb.update();
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(menuBackgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons)
            mb.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        for (MenuButton mb : buttons)
            if (isIn(e, mb))
                mb.setMousePressed(true);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        for (MenuButton mb : buttons)
            if (isIn(e, mb))
            {
                if (mb.isMousePressed())
                    mb.applyGameState();
                if (mb.getState() == Gamestate.PLAYING)
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIdx());
                break;
            }
        resetButton();
    }

    private void resetButton()
    {
        for (MenuButton mb : buttons)
            mb.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        for (MenuButton mb : buttons)
            mb.setMouseOver(false);
        for (MenuButton mb : buttons)
            if (isIn(e, mb))
            {
                mb.setMouseOver(true);
                break;
            }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            Gamestate.state = Gamestate.PLAYING;
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
