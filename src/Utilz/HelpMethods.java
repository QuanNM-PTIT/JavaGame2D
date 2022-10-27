package Utilz;

import Main.Game;

public class HelpMethods
{
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData)
    {
        return !IsSolid(x, y, lvlData) && !IsSolid(x + width, y + height, lvlData) && !IsSolid(x + width, y, lvlData) && !IsSolid(x, y + height, lvlData);
    }

    public static boolean IsSolid(float x, float y, int[][] lvlData)
    {
        if (x < 0 || x >= Game.GAME_WIDTH)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;
        int xIdx = (int) (x / Game.TILES_SIZE);
        int yIdx = (int) (y / Game.TILES_SIZE);
        int val = lvlData[yIdx][xIdx];
        if (val >= 48 || val < 0 || val != 11)
            return true;
        return false;
    }
}
