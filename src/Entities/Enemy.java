package Entities;

import Main.Game;

import static Utilz.Constants.UI.EnemyConstants.*;
import static Utilz.HelpMethods.*;
import static Utilz.Constants.Directions.*;

public abstract class Enemy extends Entity
{
    private int animationIdx, enemyState, enemyType;
    private int animationTick, animationSpeed = 25;
    private float fallSpeed;
    private float gravity = 0.04f * Game.SCALE;
    private boolean firstUpdate = true;
    private boolean inAir = false;
    private float walkSpeed = 0.25f * Game.SCALE;
    private int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType)
    {
        super(x, y, width, height);
        this.enemyType = enemyType;
        intitHitbox(x, y, width, height);
    }

    private void updateAnimationTick()
    {
        ++animationTick;
        if (animationTick >= animationSpeed)
        {
            animationTick = 0;
            ++animationIdx;
            if (animationIdx >= GetSpriteAmount(enemyType, enemyState))
                animationIdx = 0;
        }
    }

    public void update(int[][] lvlData)
    {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData)
    {
        if (firstUpdate)
        {
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
            firstUpdate = false;
        }
        if (inAir)
        {
            if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData))
            {
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            }
            else
            {
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        }
        else
        {
            switch (enemyState)
            {
                case IDLE:
                    enemyState = RUNNING;
                    break;
                case RUNNING:
                    float xSpeed = 0;
                    if (walkDir == LEFT)
                        xSpeed = -walkSpeed;
                    else
                        xSpeed = walkSpeed;
                    if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
                        if (IsFloor(hitbox, xSpeed, lvlData))
                        {
                            hitbox.x += xSpeed;
                            return;
                        }
                    changeWalkDir();
                    break;
            }
        }
    }

    private void changeWalkDir()
    {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public int getAnimationIdx()
    {
        return animationIdx;
    }

    public int getEnemyState()
    {
        return enemyState;
    }
}
