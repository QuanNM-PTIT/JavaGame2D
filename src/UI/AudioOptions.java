package UI;

import GameStates.Gamestate;
import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static Utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static Utilz.Constants.UI.URMButtons.URM_SIZE;
import static Utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static Utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class AudioOptions
{
    private SoundButton musicButton, sfxButton;
    private VolumeButton volumeButton;

    public AudioOptions()
    {
        createSoundButtons();
        createVolumeButton();
    }

    private void createSoundButtons()
    {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void createVolumeButton()
    {
        int volumeX = (int) (309 * Game.SCALE);
        int volumeY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(volumeX, volumeY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    public void update()
    {
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g)
    {
        musicButton.draw(g);
        sfxButton.draw(g);
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e)
    {
        if (volumeButton.isMousePressed())
            volumeButton.changeX(e.getX());
    }

    public void mousePressed(MouseEvent e)
    {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMousePressed(true);
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
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e)
    {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButton p)
    {
        return p.getBounds().contains(e.getX(), e.getY());
    }
}
