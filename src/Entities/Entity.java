package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

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

    protected void intitHitbox(int width, int height)
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
}
