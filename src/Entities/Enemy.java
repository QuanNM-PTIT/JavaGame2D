package Entities;

import GameStates.Playing;
import Main.Game;

import java.awt.geom.Rectangle2D;

import static Utilz.Constants.*;
import static Utilz.Constants.EnemyConstants.*;
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
    protected int attackBoxOffsetX;

    public Enemy(float x, float y, int width, int height, int enemyType)
    {
        super(x, y, width, height);
        this.enemyType = enemyType;

        maxHealth = GetMaxHealth(enemyType);
        curHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    protected void updateAttackBox()
    {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    protected void initAttackBox(int w, int h, int attackBoxOffsetX)
    {
        attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
        this.attackBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
    }

    protected void firstUpdateCheck(int[][] lvlData)
    {
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void inAirChecks(int[][] lvlData, Playing playing)
    {
        if (state != HIT && state != DEAD)
        {
            updateInAir(lvlData);
            playing.getObjectManager().checkSpikesTouched(this);
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

    protected void turnTowardsPlayer(Player player)
    {
        if (player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player)
    {
        int playeryTile = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if (playeryTile == yTile)
            if (isPlayerInRange(player))
            {
                if (IsSightClear(lvlData, hitbox, player.hitbox, yTile))
                    return true;
            }
        return false;
    }

    protected boolean isPlayerInRange(Player player)
    {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player)
    {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        switch (enemyType)
        {
            case CRABBY ->
            {
                return absValue <= attackDistance;
            }
        }
        return false;
    }

    public void hurt(int amount)
    {
        curHealth -= amount;
        if (curHealth <= 0)
            newState(DEAD);
        else
        {
            newState(HIT);
            if (walkDir == LEFT)
                pushBackDir = RIGHT;
            else
                pushBackDir = LEFT;
            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player)
    {
        if (attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType), this);
        attackChecked = true;
    }

    protected void updateAnimationTick()
    {
        aniTick++;
        if (aniTick >= ANIMATION_SPEED)
        {
            aniTick = 0;
            aniIdx++;
            if (aniIdx >= GetSpriteAmount(enemyType, state))
            {
                if (enemyType == CRABBY)
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
    }

    protected void changeWalkDir()
    {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
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

        pushDrawOffset = 0;

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

    public boolean isActive()
    {
        return active;
    }

    public float getPushDrawOffset()
    {
        return pushDrawOffset;
    }
}
