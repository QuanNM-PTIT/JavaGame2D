package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Utilz.Constants.Directions.*;
import static Utilz.Constants.UI.EnemyConstants.*;

public class Crabby extends Enemy
{
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Crabby(float x, float y)
    {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        intitHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
        attackBoxOffsetX = (int) (30 * Game.SCALE);
        updateAttackBox();
    }

    private void updateAttackBox()
    {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    public void update(int[][] lvlData, Player player)
    {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateBehavior(int[][] lvlData, Player player)
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
                case ATTACK:
                    if (animationIdx == 0)
                        attackChecked = false;
                    if (animationIdx == 3 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }

    public void drawAttackBox(Graphics g, int xLvlOffset)
    {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public int flipX()
    {
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    public int flipW()
    {
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}
