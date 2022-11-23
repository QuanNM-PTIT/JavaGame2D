package Entities;

import GameStates.Playing;
import Levels.Level;
import Utilz.LoadSave;
import static Utilz.Constants.UI.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager
{
    private Playing playing;
    private ArrayList<ArrayList<BufferedImage>> crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<Crabby>();

    public EnemyManager(Playing playing)
    {
        this.playing = playing;
        loadEnemyImages();
    }

    public void loadEnemies(Level level)
    {
        crabbies = level.getCrabbies();
    }

    public void update(int[][] lvlData, Player player)
    {
        boolean isAnyActive = false;
        for (Crabby i : crabbies)
        {
            if (i.isActive())
            {
                i.update(lvlData, player);
                isAnyActive = true;
            }
        }
        if (!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLvlOffset)
    {
        drawCrabs(g, xLvlOffset);
    }

    public void drawCrabs(Graphics g, int xLvlOffset)
    {
        for (Crabby i : crabbies)
            if (i.isActive())
            {
                g.drawImage(crabbyArr.get(i.getState()).get(i.getAnimationIdx()), (int) i.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + i.flipX(), (int) i.getHitbox().y - CRABBY_DRAWOFFSET_Y, CRABBY_WIDTH * i.flipW(), CRABBY_HEIGHT, null);
                //i.drawHitbox(g, xLvlOffset);
                //i.drawAttackBox(g, xLvlOffset);
            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox)
    {
        for (Crabby i : crabbies)
        {
            if (i.isActive())
            {
                if (i.getCurHealth() > 0)
                {
                    if (attackBox.intersects(i.getHitbox()))
                    {
                        i.hurt(10);
                        return;
                    }
                }
            }
        }
    }

    private void loadEnemyImages()
    {
        BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.CRABBY_SPRITE);
        crabbyArr = new ArrayList<ArrayList<BufferedImage>>();
        for (int i = 0; i < 5; ++i)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<BufferedImage>();
            for (int j = 0; j < 9; ++j)
                tmp.add(img.getSubimage(j * CRABBY_WIDTH_DEFAULT, i * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT));
            crabbyArr.add(tmp);
        }
    }

    public void resetAllEnemies()
    {
        for (Crabby i : crabbies)
            i.resetEnemy();
    }
}
