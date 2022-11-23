package UI;

import Utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton
{
    private ArrayList<ArrayList<BufferedImage>> soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIdx, colIdx;

    public SoundButton(int x, int y, int width, int height)
    {
        super(x, y, width, height);
        loadSoundImgs();
    }

    private void loadSoundImgs()
    {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImgs = new ArrayList<ArrayList<BufferedImage>>();
        for (int i = 0; i < 2; ++i)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<BufferedImage>();
            for (int j = 0; j < 3; ++j)
                tmp.add(img.getSubimage(j * SOUND_SIZE_DEFAULT, i * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT));
            soundImgs.add(tmp);
        }
    }

    public void update()
    {
        if (muted)
            rowIdx = 1;
        else
            rowIdx = 0;
        colIdx = 0;
        if (mouseOver)
            colIdx = 1;
        if (mousePressed)
            colIdx = 2;
    }

    public void resetBools()
    {
        mouseOver = mousePressed = false;
    }

    public void draw(Graphics g)
    {
        g.drawImage(soundImgs.get(rowIdx).get(colIdx), x, y, width, height, null);
    }

    public boolean isMouseOver()
    {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver)
    {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed()
    {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed)
    {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted()
    {
        return muted;
    }

    public void setMuted(boolean muted)
    {
        this.muted = muted;
    }
}
