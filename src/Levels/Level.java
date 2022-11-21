package Levels;

import Entities.Crabby;
import Main.Game;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Spike;
import Utilz.HelpMethods;

import static Utilz.HelpMethods.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level
{
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabbies;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> gameContainers;
    private ArrayList<Spike> spikes;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffset;
    private Point playerSpawn;

    public Level(BufferedImage img)
    {
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        calcLevelOffsets();
        calcLevelSpawn();
    }

    private void createSpikes()
    {
        spikes = HelpMethods.GetSpikes(img);
    }

    private void createPotions()
    {
        potions = HelpMethods.GetPotions(img);
    }

    private void createContainers()
    {
        gameContainers = HelpMethods.GetGameContainers(img);
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

    public ArrayList<Potion> getPotions()
    {
        return potions;
    }

    public ArrayList<GameContainer> getGameContainers()
    {
        return gameContainers;
    }

    public ArrayList<Spike> getSpikes()
    {
        return spikes;
    }
}
