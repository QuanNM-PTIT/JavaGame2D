package Utilz;

import Entities.Crabby;
import Main.Game;
import Objects.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilz.Constants.EnemyConstants.*;
import static Utilz.Constants.ObjectConstants.*;

public class HelpMethods
{
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData)
    {
        return (!IsSolid(x, y, lvlData) && !IsSolid(x + width, y + height, lvlData) && !IsSolid(x + width, y, lvlData) && !IsSolid(x, y + height, lvlData));
    }

    public static boolean IsSolid(float x, float y, int[][] lvlData)
    {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;
        float xIdx = x / Game.TILES_SIZE;
        float yIdx = y / Game.TILES_SIZE;
        return IsTileSolid((int) xIdx, (int) yIdx, lvlData);
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed)
    {
        int curTile = (int) (hitBox.x / Game.TILES_SIZE);
        if (xSpeed > 0)
        {
            int tileXPos = curTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitBox.width);
            return tileXPos + xOffset - 1;
        }
        else
            return curTile * Game.TILES_SIZE;
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed)
    {
        int curTile = (int) (hitBox.y / Game.TILES_SIZE);
        if (airSpeed > 0)
        {
            int tileYPos = curTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitBox.height);
            return tileYPos + yOffset - 1;
        }
        else
            return curTile * Game.TILES_SIZE;
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData)
    {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData) && !IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
            return false;
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData)
    {
        if (xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData)
    {
        if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData)
    {
        int val = lvlData[yTile][xTile];
        if (val >= 48 || val < 0 || val != 11)
            return true;
        return false;
    }

    public static boolean IsCannonCanSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile)
    {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);
        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData)
    {
        for (int i = 0; i < xEnd - xStart; ++i)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }

    public static boolean IsAllWalkable(int xStart, int xEnd, int y, int[][] lvlData)
    {
        if (IsAllTilesClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; ++i)
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile)
    {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);
        if (firstXTile > secondXTile)
            return IsAllWalkable(secondXTile, firstXTile, yTile, lvlData);
        return IsAllWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsProjectileHittingLevel(Projectile projectile, int[][] lvlData)
    {
        return IsSolid(projectile.getHitbox().x + projectile.getHitbox().width / 2, projectile.getHitbox().y + projectile.getHitbox().height / 2, lvlData);
    }

    public static int[][] GetLevelData(BufferedImage img)
    {
        int[][] lvData = new int[img.getHeight()][img.getWidth()];
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getRed();
                if (val >= 48)
                    val = 0;
                lvData[i][j] = val;
            }
        }
        return lvData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img)
    {
        ArrayList<Crabby> res = new ArrayList<Crabby>();
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getGreen();
                if (val == CRABBY) // Color: 0b009a
                    res.add(new Crabby(j * Game.TILES_SIZE, i * Game.TILES_SIZE));
            }
        }
        return res;
    }

    public static Point GetPlayerSpawn(BufferedImage img)
    {
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getGreen();
                if (val == 100)
                    return new Point(j * Game.TILES_SIZE, i * Game.TILES_SIZE);
            }
        }
        return new Point(Game.TILES_SIZE, Game.TILES_SIZE);
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img)
    {
        ArrayList<Potion> res = new ArrayList<Potion>();
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getBlue();
                if (val == RED_POTION || val == BLUE_POTION)
                    res.add(new Potion(j * Game.TILES_SIZE, i * Game.TILES_SIZE, val));
            }
        }
        return res;
    }

    public static ArrayList<GameContainer> GetGameContainers(BufferedImage img)
    {
        ArrayList<GameContainer> res = new ArrayList<GameContainer>();
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getBlue();
                if (val == BOX || val == BARREL)
                    res.add(new GameContainer(j * Game.TILES_SIZE, i * Game.TILES_SIZE, val));
            }
        }
        return res;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage img)
    {
        ArrayList<Spike> res = new ArrayList<Spike>();
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getBlue();
                if (val == SPIKE)
                    res.add(new Spike(j * Game.TILES_SIZE, i * Game.TILES_SIZE, SPIKE));
            }
        }
        return res;
    }

    public static ArrayList<Cannon> GetCannons(BufferedImage img)
    {
        ArrayList<Cannon> res = new ArrayList<Cannon>();
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getBlue();
                if (val == CANNON_LEFT || val == CANNON_RIGHT)
                    res.add(new Cannon(j * Game.TILES_SIZE, i * Game.TILES_SIZE, val));
            }
        }
        return res;
    }

    public static ArrayList<SpaceGate> GetSpaceGate(BufferedImage img)
    {
        ArrayList<SpaceGate> res = new ArrayList<SpaceGate>();
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getBlue();
                if (val == 100)
                    res.add(new SpaceGate(j * Game.TILES_SIZE, i * Game.TILES_SIZE, val));
            }
        }
        return res;
    }
}
