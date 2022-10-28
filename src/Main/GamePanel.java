package Main;

import Inputs.*;

import javax.swing.*;
import java.awt.*;
import static Main.Game.GAME_WIDTH;
import static Main.Game.GAME_HEIGHT;

public class GamePanel extends JPanel
{
    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game)
    {
        this.game = game;
        mouseInputs = new MouseInputs(this);
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize()
    {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setMaximumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void updateGame()
    {

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame()
    {
        return this.game;
    }
}
