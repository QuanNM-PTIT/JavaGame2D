package Main;

import Inputs.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel
{
    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game)
    {
        this.game = game;
        mouseInputs = new MouseInputs();
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize()
    {
        Dimension size = new Dimension(1280, 800);
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
