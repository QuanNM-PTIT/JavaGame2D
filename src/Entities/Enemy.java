package Entities;

import GameStates.Playing;
import Main.Game;

import java.awt.geom.Rectangle2D;

import static Utilz.Constants.*;
import static Utilz.Constants.UI.EnemyConstants.*;
import static Utilz.HelpMethods.*;
import static Utilz.Constants.Directions.*;

public abstract class Enemy extends Entity
{
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int yTile;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType)
    {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        curHealth = maxHealth;
        walkSpeed = 0.25f * Game.SCALE;
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
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData))
        {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        }
        else
        {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
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

    public void hurt(int val)
    {
        curHealth -= val;
        if (curHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox, Player player)
    {
        if (attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType));
        attackChecked = true;
    }

    protected void updateAnimationTick()
    {
        ++aniTick;
        if (aniTick >= ANIMATION_SPEED)
        {
            aniTick = 0;
            ++aniIdx;
            if (aniIdx >= GetSpriteAmount(enemyType, state))
            {
                aniIdx = 0;
                switch (state)
                {
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
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
        int absValX = (int) Math.abs(player.hitbox.x - hitbox.x);
        int absValY = (int) Math.abs(player.hitbox.y - hitbox.y);
        return absValX <= attackDistance && absValY <= attackDistance;
    }

    protected void newState(int enemyState)
    {
        this.state = enemyState;
        aniTick = 0;
        aniIdx = 0;
    }

    protected void changeWalkDir()
    {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public int getState()
    {
        return state;
    }

    public boolean isActive()
    {
        return active;
    }

    public void resetEnemy()
    {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        curHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }
}
