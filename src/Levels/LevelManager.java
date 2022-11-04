package Levels;

import Main.Game;
import Utilz.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager
{
    private Game game;
    private ArrayList<BufferedImage> levelSprite;
    private Level levelOne;

    public LevelManager(Game game)
    {
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites()
    {
        BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.LEVEL_ATLAS);;
        levelSprite = new ArrayList<BufferedImage>();
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 12; ++j)
                levelSprite.add(img.getSubimage(j * 32, i * 32, 32, 32));

    }

    public void draw(Graphics g, int xLvlOffset)
    {
        for (int i = 0; i < Game.TILES_IN_HEIGHT; ++i)
            for (int j = 0; j < levelOne.getLevelData()[0].length; ++j)
            {
                int idx = levelOne.getSpriteIdx(i, j);
                g.drawImage(levelSprite.get(idx), j * Game.TILES_SIZE - xLvlOffset, Game.TILES_SIZE * i, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }

    public void update()
    {

    }

    public Level getCurLevel()
    {
        return levelOne;
    }
}
