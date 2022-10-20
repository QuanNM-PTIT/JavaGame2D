package Inputs;

import Main.GamePanel;

import java.awt.event.*;

public class KeyboardInputs implements KeyListener
{
    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                gamePanel.changeDeltaY(-5);
                break;
            case KeyEvent.VK_LEFT:
                gamePanel.changeDeltaX(-5);
                break;
            case KeyEvent.VK_DOWN:
                gamePanel.changeDeltaY(5);
                break;
            case KeyEvent.VK_RIGHT:
                gamePanel.changeDeltaX(5);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
