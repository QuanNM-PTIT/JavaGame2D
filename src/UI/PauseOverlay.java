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
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private SoundButton musicButton, sfxButton;
    private UrmButton menuButton, replayButton, unpauseButton;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing)
    {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    private void loadBackground()
    {
        backgroundImg = LoadSave.GetPlayerAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    private void createSoundButtons()
    {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
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
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g)
    {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        musicButton.draw(g);
        sfxButton.draw(g);
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e)
    {
        if (volumeButton.isMousePressed())
        {
            volumeButton.changeX(e.getX());
        }
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
        else if (isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if (isIn(e, replayButton))
            replayButton.setMousePressed(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMousePressed(true);
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
        else if (isIn(e, menuButton))
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

        musicButton.resetBools();
        sfxButton.resetBools();
        menuButton.resetBools();
        unpauseButton.resetBools();
        replayButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e)
    {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if (isIn(e, unpauseButton))
            unpauseButton.setMouseOver(true);
        else if (isIn(e, replayButton))
            replayButton.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButton p)
    {
        return p.getBounds().contains(e.getX(), e.getY());
    }
}
