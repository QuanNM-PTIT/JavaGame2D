package Entities;

import Audio.AudioPlayer;
import GameStates.Playing;
import Main.Game;
import Utilz.LoadSave;

import static Utilz.Constants.*;
import static Utilz.Constants.Directions.*;
import static Utilz.HelpMethods.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilz.Constants.PlayerConstants.*;

public class Player extends Entity
{
    private ArrayList<ArrayList<BufferedImage>> animations;
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
        this.walkSpeed = Game.SCALE * 1.0f;
        loadAnimations();
        initHitbox(20, 27);
        initAttackBox();
    }

    public void setSpawn(Point spawn)
    {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Float(x, y, (int) (35 * Game.SCALE), (int) (20 * Game.SCALE));
        resetAttackBox();
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
                aniTick = 0;
                aniIdx = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);

                if (!IsEntityOnFloor(hitbox, lvlData))
                {
                    inAir = true;
                    airSpeed = 0;
                }
            }
            else if (aniIdx == GetSpriteAmount(DEAD) - 1 && aniTick >= ANIMATION_SPEED - 1)
            {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            }
            else
            {
                updateAnimationTick();
                if (inAir)
                    if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData))
                    {
                        hitbox.y += airSpeed;
                        airSpeed += GRAVITY;
                    }
                    else
                        inAir = false;

            }
            return;
        }

        updateAttackBox();

        if (state == HIT)
        {
            if (aniIdx <= GetSpriteAmount(state) - 3)
                pushBack(pushBackDir, lvlData, 1.0f);
            updatePushBackDrawOffset();
        }
        else
            updatePos();

        if (moving)
        {
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
            if (powerAttackActive)
            {
                powerAttackTick++;
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
        if (attackChecked || aniIdx != 1)
            return;
        attackChecked = true;

        if (powerAttackActive)
            attackChecked = false;

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void setAttackBoxOnRightSide()
    {
        attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE * 5);
    }

    private void setAttackBoxOnLeftSide()
    {
        attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
    }

    private void updateAttackBox()
    {
        if (right && left)
        {
            if (flipW == 1)
            {
                setAttackBoxOnRightSide();
            }
            else
            {
                setAttackBoxOnLeftSide();
            }

        }
        else if (right || (powerAttackActive && flipW == 1))
            setAttackBoxOnRightSide();
        else if (left || (powerAttackActive && flipW == -1))
            setAttackBoxOnLeftSide();

        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar()
    {
        healthWidth = (int) ((curHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar()
    {
        powerWidth = (int) ((powerVal / (float) powerMaxVal) * powerBarWidth);

        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed)
        {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    public void render(Graphics g, int lvlOffset)
    {
        g.drawImage(animations.get(state).get(aniIdx), (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset + (int) (pushDrawOffset)), width * flipW, height, null);
//		drawHitbox(g, lvlOffset);
//		drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g)
    {
        // Background ui
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Health bar
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

        // Power Bar
        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }

    private void updateAnimationTick()
    {
        aniTick++;
        if (aniTick >= ANIMATION_SPEED)
        {
            aniTick = 0;
            aniIdx++;
            if (aniIdx >= GetSpriteAmount(state))
            {
                aniIdx = 0;
                attacking = false;
                attackChecked = false;
                if (state == HIT)
                {
                    newState(IDLE);
                    airSpeed = 0f;
                    if (!IsFloor(hitbox, 0, lvlData))
                        inAir = true;
                }
            }
        }
    }

    private void setAnimation()
    {
        int startAni = state;

        if (state == HIT)
            return;

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

    private void resetAniTick()
    {
        aniTick = 0;
        aniIdx = 0;
    }

    private void updatePos()
    {
        moving = false;

        if (jump)
            jump();

        if (!inAir)
            if (!powerAttackActive)
                if ((!left && !right) || (right && left))
                    return;

        float xSpeed = 0;

        if (left && !right)
        {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right && !left)
        {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (powerAttackActive)
        {
            if ((!left && !right) || (left && right))
            {
                if (flipW == -1)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;
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

    private void resetInAir()
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

    public void changeHealth(int value)
    {
        if (value < 0)
        {
            if (state == HIT)
                return;
            else
                newState(HIT);
        }

        curHealth += value;
        curHealth = Math.max(Math.min(curHealth, maxHealth), 0);
    }

    public void changeHealthByShooted(int value, int dir)
    {
        if (value < 0)
        {
            if (state == HIT)
                return;
            else
                newState(HIT);
        }
        pushBackDir = dir;
        curHealth += value;
        curHealth = Math.max(Math.min(curHealth, maxHealth), 0);
    }

    public void changeHealth(int value, Enemy e)
    {
        if (state == HIT)
            return;
        changeHealth(value);
        pushBackOffsetDir = UP;
        pushDrawOffset = 0;

        if (e.getHitbox().x < hitbox.x)
            pushBackDir = RIGHT;
        else
            pushBackDir = LEFT;
    }

    public void killed()
    {
        curHealth = 0;
    }

    public void changePower(int value)
    {
        powerVal += value;
        powerVal = Math.max(Math.min(powerVal, powerMaxVal), 0);
    }

    private void loadAnimations()
    {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new ArrayList<>();
        for (int j = 0; j < 7; j++)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<>();
            for (int i = 0; i < 8; i++)
                tmp.add(img.getSubimage(i * 64, j * 40, 64, 40));
            animations.add(tmp);
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData)
    {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans()
    {
        left = false;
        right = false;
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
        airSpeed = 0f;
        state = IDLE;
        curHealth = maxHealth;
        powerAttackActive = false;
        powerAttackTick = 0;
        powerVal = powerMaxVal;

        hitbox.x = x;
        hitbox.y = y;
        resetAttackBox();

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    private void resetAttackBox()
    {
        if (flipW == 1)
            setAttackBoxOnRightSide();
        else
            setAttackBoxOnLeftSide();
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
