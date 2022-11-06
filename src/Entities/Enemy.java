package Entities;

import Main.Game;

import java.awt.geom.Rectangle2D;

import static Utilz.Constants.UI.EnemyConstants.*;
import static Utilz.HelpMethods.*;
import static Utilz.Constants.Directions.*;

public abstract class Enemy extends Entity
{
    protected int animationIdx, enemyState, enemyType;
    protected int animationTick, animationSpeed = 25;
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected boolean firstUpdate = true;
    protected boolean inAir = false;
    protected float walkSpeed = 0.25f * Game.SCALE;
    protected int walkDir = LEFT;
    protected int yTile;
    protected float attackDistance = Game.TILES_SIZE;

    public Enemy(float x, float y, int width, int height, int enemyType)
    {
        super(x, y, width, height);
        this.enemyType = enemyType;
        intitHitbox(x, y, width, height);
    }

    protected void firstUpdateCheck(int[][] lvlData)
    {
        if (firstUpdate)
        {
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
            firstUpdate = false;
        }
    }

    protected void updateInAir(int[][] lvlData)
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
            yTile = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData)
    {
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
    }

    protected void updateAnimationTick()
    {
        ++animationTick;
        if (animationTick >= animationSpeed)
        {
            animationTick = 0;
            ++animationIdx;
            if (animationIdx >= GetSpriteAmount(enemyType, enemyState))
            {
                animationIdx = 0;
                if (enemyState == ATTACK)
                    enemyState = IDLE;
            }
        }
    }

    protected void turnTowardsPlayer(Player player)
    {
        if (player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player)
    {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        return playerTileY == yTile && isPlayerInRange(player) && IsSightClear(lvlData, hitbox, player.hitbox, yTile);
    }

    private boolean isPlayerInRange(Player player)
    {
        int absVal = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absVal <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player)
    {
        int absVal = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absVal <= attackDistance;
    }

    protected void newState(int enemyState)
    {
        this.enemyState = enemyState;
        animationTick = 0;
        animationIdx = 0;
    }

    protected void changeWalkDir()
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
