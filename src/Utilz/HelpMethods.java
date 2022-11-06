package Utilz;

import Main.Game;

import java.awt.geom.Rectangle2D;

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
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData)
    {
        int val = lvlData[yTile][xTile];
        if (val >= 48 || val < 0 || val != 11)
            return true;
        return false;
    }

    public static boolean IsAllWalkable(int xStart, int xEnd, int y, int[][] lvlData)
    {
        for (int i = 0; i < xEnd - xStart; ++i)
        {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if (y + 1 < lvlData.length)
            {
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
            }
        }
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
}
