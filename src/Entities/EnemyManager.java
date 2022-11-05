package Entities;

import GameStates.Playing;
import Utilz.LoadSave;
import static Utilz.Constants.UI.EnemyConstants.*;

import java.awt.*;
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
        addEnemies();
    }

    private void addEnemies()
    {
        crabbies = LoadSave.GetCrabs();
    }

    public void update()
    {
        for (Crabby i : crabbies)
            i.update();
    }

    public void draw(Graphics g, int xLvlOffset)
    {
        drawCrabs(g, xLvlOffset);
    }

    public void drawCrabs(Graphics g, int xLvlOffset)
    {
        for (Crabby i : crabbies)
            g.drawImage(crabbyArr.get(i.getEnemyState()).get(i.getAnimationIdx()), (int) i.getHitbox().x - xLvlOffset, (int) i.getHitbox().y, CRABBY_WIDTH, CRABBY_HEIGHT, null);
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
}
