package UI;

import Main.Game;
import Utilz.LoadSave;
import static Utilz.Constants.UI.PauseButtons.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseOverlay
{
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private SoundButton musicButton, sfxButton;

    public PauseOverlay()
    {
        loadBackground();
        createSoundButtons();
    }

    private void createSoundButtons()
    {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground()
    {
        backgroundImg = LoadSave.GetPlayerAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    public void update()
    {
        musicButton.update();
        sfxButton.update();
    }

    public void draw(Graphics g)
    {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        musicButton.draw(g);
        sfxButton.draw(g);
    }

    public void mouseDragged(MouseEvent e)
    {

    }

    public void mouseClicked(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e)
    {
        if (isIn(e, musicButton))
        {
            if (musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted());
        }
        else if (isIn(e, sfxButton))
        {
            if (sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted());
        }
        musicButton.resetBools();
        sfxButton.resetBools();
    }

    public void mouseMoved(MouseEvent e)
    {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButton p)
    {
        return p.getBounds().contains(e.getX(), e.getY());
    }
}
