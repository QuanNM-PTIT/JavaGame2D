package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Utilz.Constants.Directions.*;
import static Utilz.HelpMethods.CanMoveHere;

public abstract class Entity
{
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIdx;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int curHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;

    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;

    public Entity(float x, float y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void drawHitbox(Graphics g, int xLvlOffset)
    {
        g.setColor(Color.MAGENTA);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void drawAttackBox(Graphics g, int lvlOffsetX)
    {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - lvlOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    protected void updatePushBackDrawOffset()
    {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP)
        {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit)
                pushBackOffsetDir = DOWN;
        }
        else
        {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0)
                pushDrawOffset = 0;
        }
    }

    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti)
    {
        float xSpeed = 0;
        if (pushBackDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed * speedMulti;
    }

    protected void initHitbox(int width, int height)
    {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox()
    {
        return hitbox;
    }

    public int getAnimationIdx()
    {
        return aniIdx;
    }

    public int getState()
    {
        return state;
    }

    public int getCurHealth()
    {
        return curHealth;
    }

    protected void newState(int state)
    {
        this.state = state;
        aniTick = 0;
        aniIdx = 0;
    }
}
