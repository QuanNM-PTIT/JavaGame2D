package Entities;

import Audio.AudioPlayer;
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

    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxVal = 200;
    private int powerVal = powerMaxVal;

    private int flipX = 0;
    private int flipW = 1;
    private int tileY = 0;
    private boolean powerAttackActive;
    private int powerAttackTick = 0;
    private int powerGrowSpeed = 15;
    private int powerGrowTick;

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
        initHitbox(20, 27);
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
        updatePowerBar();
        if (curHealth <= 0)
        {
            if (state != DEAD)
            {
                state = DEAD;
                aniIdx = aniTick = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            }
            else if (aniIdx == GetSpriteAmount(DEAD) - 1 && aniTick >= ANIMATION_SPEED - 1)
            {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            }
            else
                updateAnimationTick();
            return;
        }

        updateAttackBox();
        updatePos();
        if (moving)
        {
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
            if (powerAttackActive)
            {
                ++powerAttackTick;
                if (powerAttackTick >= 35)
                {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }
        if (attacking || powerAttackActive)
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
        if (powerAttackActive)
            attackChecked = false;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox()
    {
        if (right || powerAttackActive && flipW == 1)
        {
            attackBox.x = hitbox.x + hitbox.width + (int) (10 * Game.SCALE);
        }
        else if (left || powerAttackActive && flipW == -1)
        {
            attackBox.x = hitbox.x - hitbox.width - (int) (10 * Game.SCALE);
        }
        attackBox.y = hitbox.y + 10 * Game.SCALE;
    }

    private void updateHealthBar()
    {
        healthBarWidth = (int) ((curHealth / (float) maxHealth) * healthWidth);
    }

    private void updatePowerBar()
    {
        powerWidth = (int) (1.0 * powerVal / powerMaxVal * powerBarWidth);
        ++powerGrowTick;
        if (powerGrowTick >= powerGrowSpeed)
        {
            powerGrowTick = 0;
            changePower(1);
        }
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

        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
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
        if (powerAttackActive)
        {
            state = ATTACK;
            aniIdx = 1;
            aniTick = 0;
            return;
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
        if (!inAir)
            if (!powerAttackActive)
                if ((!left && !right) || (right && left))
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
        if (powerAttackActive)
        {
            if (!left && !right)
            {
                if (flipW == -1)
                    xSpeed -= walkSpeed;
                else
                    xSpeed += walkSpeed;
            }
            xSpeed *= 3;
        }
        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;
        if (inAir && !powerAttackActive)
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
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
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
            if (powerAttackActive)
            {
                powerAttackActive = false;
                powerAttackTick = 0;
            }
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
        powerVal += val;
        if (powerVal >= powerMaxVal)
            powerVal = powerMaxVal;
        else if (powerVal <= 0)
            powerVal = 0;
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

    public void powerAttack()
    {
        if (powerAttackActive)
            return;
        if (powerVal >= 60)
        {
            powerAttackActive = true;
            changePower(-60);
        }
    }
}
