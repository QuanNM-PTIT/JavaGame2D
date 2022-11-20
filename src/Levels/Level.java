package Levels;

import Entities.Crabby;
import Main.Game;
import static Utilz.HelpMethods.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level
{
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabbies;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffset;
    private Point playerSpawn;

    public Level(BufferedImage img)
    {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLevelOffsets();
        calcLevelSpawn();
    }

    private void calcLevelSpawn()
    {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calcLevelOffsets()
    {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffset = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies()
    {
        crabbies = GetCrabs(img);
    }

    private void createLevelData()
    {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIdx(int x, int y)
    {
        return lvlData[x][y];
    }

    public int[][] getLevelData()
    {
        return lvlData;
    }

    public int getLevelOffset()
    {
        return maxLvlOffset;
    }

    public ArrayList<Crabby> getCrabbies()
    {
        return crabbies;
    }

    public Point getPlayerSpawn()
    {
        return playerSpawn;
    }
}
