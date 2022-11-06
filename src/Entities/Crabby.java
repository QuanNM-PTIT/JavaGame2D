package Entities;

import Main.Game;

import static Utilz.Constants.Directions.LEFT;
import static Utilz.Constants.UI.EnemyConstants.*;
import static Utilz.HelpMethods.*;

public class Crabby extends Enemy
{

    public Crabby(float x, float y)
    {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        intitHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
    }

    public void update(int[][] lvlData)
    {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData)
    {
        firstUpdateCheck(lvlData);
        if (inAir)
            updateInAir(lvlData);
        else
        {
            switch (enemyState)
            {
                case IDLE:
                    enemyState = RUNNING;
                    break;
                case RUNNING:
                    move(lvlData);
                    break;
            }
        }
    }
}
