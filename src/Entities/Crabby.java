package Entities;

import GameStates.Playing;

import static Utilz.Constants.EnemyConstants.*;
import static Utilz.HelpMethods.IsFloor;

public class Crabby extends Enemy
{
    public Crabby(float x, float y)
    {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox(82, 19, 30);
    }

    public void update(int[][] lvlData, Playing playing)
    {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateBehavior(int[][] lvlData, Playing playing)
    {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
        {
            inAirChecks(lvlData, playing);
        }
        else
        {
            switch (state)
            {
                case IDLE:
                    if (IsFloor(hitbox, lvlData))
                        newState(RUNNING);
                    else
                        inAir = true;
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, playing.getPlayer()))
                    {
                        turnTowardsPlayer(playing.getPlayer());
                        if (isPlayerCloseForAttack(playing.getPlayer()))
                            newState(ATTACK);
                    }
                    move(lvlData);

                    break;
                case ATTACK:
                    if (aniIdx == 0)
                        attackChecked = false;
                    if (aniIdx == 3 && !attackChecked)
                        checkPlayerHit(attackBox, playing.getPlayer());
                    break;
                case HIT:
                    if (aniIdx <= GetSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDir, lvlData, 2f);
                    updatePushBackDrawOffset();
                    break;
            }
        }
    }
}
