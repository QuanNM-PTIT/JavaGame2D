package Levels;

import GameStates.Gamestate;
import Main.Game;
import Utilz.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager
{
    private Game game;
    private ArrayList<BufferedImage> levelSprite;
    private ArrayList<Level> levels;
    private int levelIdx = 0;

    public LevelManager(Game game)
    {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel()
    {
        ++levelIdx;
        if (levelIdx >= levels.size())
        {
            levelIdx = 0;
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(levelIdx);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLevelOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevels()
    {
        ArrayList<BufferedImage> allLevels = LoadSave.GetAllLevels();
        for (BufferedImage i : allLevels)
            levels.add(new Level(i));
    }

    private void importOutsideSprites()
    {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);;
        levelSprite = new ArrayList<BufferedImage>();
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 12; ++j)
                levelSprite.add(img.getSubimage(j * 32, i * 32, 32, 32));

    }

    public void draw(Graphics g, int xLvlOffset)
    {
        for (int i = 0; i < Game.TILES_IN_HEIGHT; ++i)
            for (int j = 0; j < levels.get(levelIdx).getLevelData()[0].length; ++j)
            {
                int idx = levels.get(levelIdx).getSpriteIdx(i, j);
                g.drawImage(levelSprite.get(idx), j * Game.TILES_SIZE - xLvlOffset, Game.TILES_SIZE * i, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }

    public void update()
    {

    }

    public Level getCurLevel()
    {
        return levels.get(levelIdx);
    }

    public int getAmountOfLevels()
    {
        return levels.size();
    }

    public int getLevelIdx()
    {
        return levelIdx;
    }
}
