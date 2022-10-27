package Levels;

public class Level
{
    private int[][] lvlData;

    public Level(int[][] lvlData)
    {
        this.lvlData = lvlData;
    }

    public int getSpriteIdx(int x, int y)
    {
        return lvlData[x][y];
    }

    public int[][] getLevelData()
    {
        return lvlData;
    }
}
