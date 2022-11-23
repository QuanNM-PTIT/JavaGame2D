package Objects;

import Entities.Enemy;
import Entities.Player;
import GameStates.Playing;
import Levels.Level;
import Main.Game;
import Utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilz.Constants.Directions.*;
import static Utilz.Constants.ObjectConstants.*;
import static Utilz.Constants.Projectiles.*;
import static Utilz.HelpMethods.IsCannonCanSeePlayer;
import static Utilz.HelpMethods.IsProjectileHittingLevel;

public class ObjectManager
{
    private Playing playing;
    private ArrayList<ArrayList<BufferedImage>> potionImgs, containerImgs;
    private ArrayList<BufferedImage> cannonImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> gameContainers;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private BufferedImage spikeImage;
    private BufferedImage ballImg;
    private Level currentLevel;

    public ObjectManager(Playing playing)
    {
        this.playing = playing;
        currentLevel = playing.getLevelManager().getCurLevel();
        potions = new ArrayList<>();
        gameContainers = new ArrayList<>();
        loadImages();
    }

    public void checkSpikesTouched(Player player)
    {
        for (Spike i : spikes)
            if (i.getHitbox().intersects(player.getHitbox()))
                player.killed();
    }

    public void checkSpikesTouched(Enemy e)
    {
        for (Spike s : currentLevel.getSpikes())
            if (s.getHitbox().intersects(e.getHitbox()))
                e.hurt(200);
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox)
    {
        for (Potion i : potions)
        {
            if (i.isActive())
            {
                if (hitbox.intersects(i.getHitbox()))
                {
                    i.setActive(false);
                    applyEffectToPlayer(i);
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion potion)
    {
        if (potion.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox)
    {
        for (GameContainer i : gameContainers)
        {
            if (i.isActive() && !i.doAnimation)
            {
                if (attackbox.intersects(i.getHitbox()))
                {
                    i.setDoAnimation(true);
                    int type = 0;
                    if (i.getObjType() == BARREL)
                        type = 1;
                    potions.add(new Potion((int) (i.getHitbox().x + i.getHitbox().width / 2), (int) (i.getHitbox().y - i.getHitbox().height / 2), type));
                    return;
                }
            }
        }
    }

    private void loadImages()
    {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTIONS);
        potionImgs = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<>();
            for (int j = 0; j < 7; ++j)
                tmp.add(potionSprite.getSubimage(12 * j, 16 * i, 12, 16));
            potionImgs.add(tmp);
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINERS);
        containerImgs = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<>();
            for (int j = 0; j < 8; ++j)
                tmp.add(containerSprite.getSubimage(40 * j, 30 * i, 40, 30));
            containerImgs.add(tmp);
        }

        spikeImage = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
        cannonImgs = new ArrayList<>();
        BufferedImage tmp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < 7; ++i)
            cannonImgs.add(tmp.getSubimage(i * 40, 0, 40, 26));
        ballImg = LoadSave.GetSpriteAtlas(LoadSave.BALL_ATLAS);
    }

    public void update(int[][] lvlData, Player player)
    {
        for (Potion i : potions)
            if (i.isActive())
                i.update();
        for (GameContainer i : gameContainers)
            if (i.isActive())
                i.update();

        updateCannons(lvlData, player);
        updateProjectile(lvlData, player);
    }

    private void updateProjectile(int[][] lvlData, Player player)
    {
        for (Projectile i : projectiles)
        {
            if (i.isActive())
            {
                i.updatePos();
                if (i.getHitbox().intersects(player.getHitbox()))
                {
                    int dir = LEFT;
                    if (i.getHitbox().x < player.getHitbox().x)
                        dir = RIGHT;
                    player.changeHealthByShooted(-25, dir);
                    i.setActive(false);
                }
                else if (IsProjectileHittingLevel(i, lvlData))
                    i.setActive(false);
            }
        }
    }

    private void updateCannons(int[][] lvlData, Player player)
    {
        for (Cannon i : cannons)
        {
            if (!i.doAnimation)
                if (i.getTileY() == player.getTileY())
                    if (isPlayerInRange(i, player))
                        if (isPlayerInFrontOfCannon(i, player))
                            if (IsCannonCanSeePlayer(lvlData, player.getHitbox(), i.getHitbox(), i.getTileY()))
                                i.setDoAnimation(true);
            i.update();
            if (i.getAniIdx() == 4 && i.getAniTick() == 0)
                shootCannon(i);
        }
    }

    private void shootCannon(Cannon i)
    {
        i.setDoAnimation(true);
        int dir = 1;
        if (i.getObjType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int) i.getHitbox().x, (int) i.getHitbox().y, dir));
    }

    private boolean isPlayerInFrontOfCannon(Cannon i, Player player)
    {
        if (i.getObjType() == CANNON_LEFT)
        {
            if (i.getHitbox().x > player.getHitbox().x)
                return true;
        }
        else if (i.getObjType() == CANNON_RIGHT)
        {
            if (i.getHitbox().x < player.getHitbox().x)
                return true;
        }
        return false;
    }

    private boolean isPlayerInRange(Cannon i, Player player)
    {
        int absVal = (int) Math.abs(player.getHitbox().x - i.getHitbox().x);
        return absVal <= 5 * Game.TILES_SIZE;
    }

    public void draw(Graphics g, int xLevelOffset)
    {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);
        drawTraps(g, xLevelOffset);
        drawProjectiles(g, xLevelOffset);
        drawCannons(g, xLevelOffset);
    }

    private void drawProjectiles(Graphics g, int xLevelOffset)
    {
        for (Projectile i : projectiles)
            if (i.isActive())
                g.drawImage(ballImg, (int) (i.getHitbox().x - xLevelOffset), (int) (i.getHitbox().y), BALL_WIDTH, BALL_HEIGHT, null);
    }

    private void drawCannons(Graphics g, int xLevelOffset)
    {
        for (Cannon i : cannons)
        {
            int x = (int) (i.getHitbox().x - xLevelOffset);
            int width = CANNON_WIDTH;
            if (i.getObjType() == CANNON_RIGHT)
            {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs.get(i.getAniIdx()), x, (int) (i.getHitbox().y + Game.SCALE), width, CANNON_HEIGHT, null);
        }
    }

    private void drawTraps(Graphics g, int xLevelOffset)
    {
        for (Spike i : spikes)
            g.drawImage(spikeImage, (int) (i.getHitbox().x - xLevelOffset), (int) (i.getHitbox().y - i.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
    }

    private void drawPotions(Graphics g, int xLevelOffset)
    {
        for (Potion i : potions)
        {
            if (i.isActive())
            {
                int idx = 0;
                if (i.getObjType() == RED_POTION)
                    idx = 1;
                g.drawImage(potionImgs.get(idx).get(i.getAniIdx()), (int) (i.getHitbox().x - i.getxDrawOffset() - xLevelOffset), (int) (i.getHitbox().y - i.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT, null);
            }
        }
    }

    private void drawContainers(Graphics g, int xLevelOffset)
    {
        for (GameContainer i : gameContainers)
        {
            if (i.isActive())
            {
                int idx = 0;
                if (i.getObjType() == BARREL)
                    idx = 1;
                g.drawImage(containerImgs.get(idx).get(i.getAniIdx()), (int) (i.getHitbox().x - i.getxDrawOffset() - xLevelOffset), (int) (i.getHitbox().y - i.getyDrawOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
        }
    }

    public void loadObjects(Level newLevel)
    {
        potions = new ArrayList<>(newLevel.getPotions());
        gameContainers = new ArrayList<>(newLevel.getGameContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
        projectiles.clear();
    }

    public void resetAll()
    {
        loadObjects(playing.getLevelManager().getCurLevel());

        for (Potion i : potions)
            i.reset();
        for (GameContainer i : gameContainers)
            i.reset();
        for (Cannon i : cannons)
            i.reset();
    }
}
