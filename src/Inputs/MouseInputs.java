package Inputs;

import GameStates.Gamestate;
import Main.GamePanel;

import java.awt.event.*;

public class MouseInputs implements MouseListener, MouseMotionListener
{
    private GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel)
    {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        switch (Gamestate.state)
        {
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseClicked(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        switch (Gamestate.state)
        {
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mousePressed(e);
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        switch (Gamestate.state)
        {
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mouseReleased(e);
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        switch (Gamestate.state)
        {
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseDragged(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mouseDragged(e);
            default:
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        switch (Gamestate.state)
        {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOptions().mouseMoved(e);
            default:
                break;
        }
    }
}
