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

    public void update(int[][] lvlData, Player player)
    {
        updateMove(lvlData, player);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData, Player player)
    {
        firstUpdateCheck(lvlData);
        if (inAir)
            updateInAir(lvlData);
        else
        {
            switch (enemyState)
            {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player))
                        turnTowardsPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
                    break;
            }
        }
    }
}
