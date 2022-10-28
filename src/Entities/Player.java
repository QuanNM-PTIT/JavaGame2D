package Entities;

import Main.Game;
import Utilz.LoadSave;
import static Utilz.HelpMethods.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilz.Constants.PlayerConstants.*;

public class Player extends Entity
{
    private ArrayList<ArrayList<BufferedImage>> animation;
    private int aniTick, aniIdx, aniSpeed = 15;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 0.75f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    private float airSpeed = 0;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.75f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height)
    {
        super(x, y, width, height);
        loadAnimation();
        intitHitbox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));
    }

    public void update()
    {
        updatePos();
        updateAnimation();
        setAnimation();
    }

    public void render(Graphics g)
    {
        g.drawImage((animation.get(playerAction)).get(aniIdx), (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), null);
        //drawHitbox(g);
    }

    private void updateAnimation()
    {
        ++aniTick;
        if (aniTick >= aniSpeed)
        {
            aniTick = 0;
            ++aniIdx;
            if (aniIdx >= GetSpriteAmount(playerAction))
            {
                aniIdx = 0;
                attacking = false;
            }
        }
    }

    public void setAnimation()
    {
        int startAni = playerAction;
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
        if (inAir)
        {
            if (airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }
        if (attacking)
            playerAction = ATTACK_1;
        if (startAni != playerAction)
            resetAniTick();
    }

    public void resetAniTick()
    {
        aniTick = aniIdx = 0;
    }

    public void updatePos()
    {
        moving = false;
        if (!left && !right && !inAir)
            return;
        float xSpeed = 0;
        if (jump)
            jump();
        if (left)
            xSpeed -= playerSpeed;
        else if (right)
            xSpeed += playerSpeed;
        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
        if (inAir)
        {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData))
            {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            }
            else
            {
                hitbox.y = GetEntityPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAin();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        }
        else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump()
    {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    public void resetInAin()
    {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed)
    {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed;
        else
        {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void loadAnimation()
    {
            BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.PLAYER_ATLAS);
            animation = new ArrayList<ArrayList<BufferedImage>>();
            for (int i = 0; i < 9; ++i)
            {
                ArrayList<BufferedImage> tmp = new ArrayList<BufferedImage>();
                for (int j = 0; j < 6; ++j)
                    tmp.add(img.getSubimage(j * 64, i * 40, 64, 40));
                animation.add(tmp);
            }
    }

    public void loadLvlData(int[][] lvlData)
    {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans()
    {
        left = right = up = down = false;
    }

    public void setAttacking(boolean attacking)
    {
        this.attacking = attacking;
    }

    public boolean isLeft()
    {
        return left;
    }

    public void setLeft(boolean left)
    {
        this.left = left;
    }

    public boolean isUp()
    {
        return up;
    }

    public void setUp(boolean up)
    {
        this.up = up;
    }

    public boolean isRight()
    {
        return right;
    }

    public void setRight(boolean right)
    {
        this.right = right;
    }

    public boolean isDown()
    {
        return down;
    }

    public void setDown(boolean down)
    {
        this.down = down;
    }

    public void setJump(boolean jump)
    {
        this.jump = jump;
    }
}
