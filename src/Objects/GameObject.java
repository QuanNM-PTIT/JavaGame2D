package Objects;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Utilz.Constants.ANIMATION_SPEED;
import static Utilz.Constants.ObjectConstants.*;

public class GameObject
{
    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIdx;
    protected int xDrawOffset, yDrawOffset;

    public GameObject(int x, int y, int objType)
    {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void intitHitbox(int width, int height)
    {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset)
    {
        g.setColor(Color.MAGENTA);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void updateAnimationTick()
    {
        ++aniTick;
        if (aniTick >= ANIMATION_SPEED)
        {
            aniTick = 0;
            ++aniIdx;
            if (aniIdx >= GetSpriteAmount(objType))
            {
                aniIdx = 0;
                if (objType == BARREL || objType == BOX)
                {
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    public void reset()
    {
        aniIdx = aniTick = 0;
        active = true;
        if (objType == BARREL || objType == BOX)
            doAnimation = false;
        else
            doAnimation = true;
    }

    public int getObjType()
    {
        return objType;
    }

    public Rectangle2D.Float getHitbox()
    {
        return hitbox;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isActive()
    {
        return active;
    }

    public int getxDrawOffset()
    {
        return xDrawOffset;
    }

    public int getyDrawOffset()
    {
        return yDrawOffset;
    }

    public int getAniIdx()
    {
        return aniIdx;
    }
}
