package Entities;

import GameStates.Playing;
import Main.Game;
import Utilz.LoadSave;

import static Utilz.Constants.*;
import static Utilz.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilz.Constants.PlayerConstants.*;

public class Player extends Entity
{
    private ArrayList<ArrayList<BufferedImage>> animation;
    private boolean moving = false, attacking = false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    private float jumpSpeed = -2.75f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int healthWidth = healthBarWidth;

    private int flipX = 0;
    private int flipW = 1;
    private int tileY = 0;

    private boolean attackChecked;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing)
    {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.curHealth = maxHealth;
        this.walkSpeed = 0.75f * Game.SCALE;
        loadAnimation();
        intitHitbox(20, 27);
        initAttackBox();
    }

    public void setPlayerSpawn(Point spawn)
    {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Float(x, y, (int) 20 * Game.SCALE, (int) 20 * Game.SCALE);
    }

    public void update()
    {
        updateHealthBar();
        if (curHealth <= 0)
        {
            playing.setGameOver(true);
            return;
        }
        uppdateAttackBox();
        updatePos();
        if (moving)
        {
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
        if (attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkSpikesTouched()
    {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched()
    {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack()
    {
        if (attackChecked  || aniIdx != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
    }

    private void uppdateAttackBox()
    {
        if (right)
        {
            attackBox.x = hitbox.x + hitbox.width + (int) (10 * Game.SCALE);
        }
        else if (left)
        {
            attackBox.x = hitbox.x - hitbox.width - (int) (10 * Game.SCALE);
        }
        attackBox.y = hitbox.y + 10 * Game.SCALE;
    }

    private void updateHealthBar()
    {
        healthBarWidth = (int) ((curHealth / (float) maxHealth) * healthWidth);
    }

    public void render(Graphics g, int lvlOffset)
    {
        g.drawImage((animation.get(state)).get(aniIdx), (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
        //drawHitbox(g, lvlOffset);
        //drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g)
    {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthBarWidth, healthBarHeight);
    }

    private void updateAnimationTick()
    {
        ++aniTick;
        if (aniTick >= ANIMATION_SPEED)
        {
            aniTick = 0;
            ++aniIdx;
            if (aniIdx >= GetSpriteAmount(state))
            {
                aniIdx = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    public void setAnimation()
    {
        int startAni = state;
        if (moving)
            state = RUNNING;
        else
            state = IDLE;
        if (inAir)
        {
            if (airSpeed < 0)
                state = JUMP;
            else
                state = FALLING;
        }
        if (attacking)
        {
            state = ATTACK;
            if (startAni != ATTACK)
            {
                aniIdx = 1;
                aniTick = 0;
                return;
            }
        }
        if (startAni != state)
            resetAniTick();
    }

    public void resetAniTick()
    {
        aniTick = aniIdx = 0;
    }

    public void updatePos()
    {
        moving = false;
        if (jump)
            jump();
        if (!left && !right && !inAir)
            return;
        float xSpeed = 0;
        if (left)
        {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        else if (right)
        {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }
        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
        if (inAir)
        {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData))
            {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            }
            else
            {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
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

    public void resetInAir()
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

    public void changeHealth(int val)
    {
        curHealth += val;
        if (curHealth <= 0)
        {
            curHealth = 0;
        }
        else if (curHealth >= maxHealth)
            curHealth = maxHealth;
    }

    public void killed()
    {
        curHealth = 0;
    }

    public void changePower(int val)
    {

    }

    private void loadAnimation()
    {
        BufferedImage img = LoadSave.GetPlayerAtlas(LoadSave.PLAYER_ATLAS);
        animation = new ArrayList<ArrayList<BufferedImage>>();
        for (int i = 0; i < 7; ++i)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<BufferedImage>();
            for (int j = 0; j < 8; ++j)
                tmp.add(img.getSubimage(j * 64, i * 40, 64, 40));
            animation.add(tmp);
        }
        statusBarImg = LoadSave.GetPlayerAtlas(LoadSave.STATUS_BAR);

    }

    public void loadLvlData(int[][] lvlData)
    {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans()
    {
        left = right = false;
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

    public boolean isRight()
    {
        return right;
    }

    public void setRight(boolean right)
    {
        this.right = right;
    }

    public void setJump(boolean jump)
    {
        this.jump = jump;
    }

    public void resetAll()
    {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        curHealth = maxHealth;
        hitbox.x = x;
        hitbox.y = y;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public int getTileY()
    {
        return tileY;
    }
}
