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
                gamePanel.getGame().getPlayer().setUp(true);
                break;
            case KeyEvent.VK_LEFT:
                gamePanel.getGame().getPlayer().setLeft(true);
                break;
            case KeyEvent.VK_DOWN:
                gamePanel.getGame().getPlayer().setDown(true);
                break;
            case KeyEvent.VK_RIGHT:
                gamePanel.getGame().getPlayer().setRight(true);
                break;
            case KeyEvent.VK_A:
                gamePanel.getGame().getPlayer().setAttacking(true);
                break;
            case KeyEvent.VK_SPACE:
                gamePanel.getGame().getPlayer().setJump(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                gamePanel.getGame().getPlayer().setUp(false);
                break;
            case KeyEvent.VK_LEFT:
                gamePanel.getGame().getPlayer().setLeft(false);
                break;
            case KeyEvent.VK_DOWN:
                gamePanel.getGame().getPlayer().setDown(false);
                break;
            case KeyEvent.VK_RIGHT:
                gamePanel.getGame().getPlayer().setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                gamePanel.getGame().getPlayer().setJump(false);
                break;
        }
    }
}
