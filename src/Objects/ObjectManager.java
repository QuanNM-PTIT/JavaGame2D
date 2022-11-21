package Objects;

import Entities.Player;
import GameStates.Playing;
import Levels.Level;
import Utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Utilz.Constants.ObjectConstants.*;

public class ObjectManager
{
    private Playing playing;
    private ArrayList<ArrayList<BufferedImage>> potionImg, containerImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> gameContainers;
    private ArrayList<Spike> spikes;
    private BufferedImage spikeImage;

    public ObjectManager(Playing playing)
    {
        this.playing = playing;
        loadImages();
        potions = new ArrayList<>();
        gameContainers = new ArrayList<>();
    }

    public void checkSpikesTouched(Player player)
    {
        for (Spike i : spikes)
            if (i.getHitbox().intersects(player.getHitbox()))
                player.killed();
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
        BufferedImage potionSprite = LoadSave.GetPlayerAtlas(LoadSave.POTIONS);
        potionImg = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<>();
            for (int j = 0; j < 7; ++j)
                tmp.add(potionSprite.getSubimage(12 * j, 16 * i, 12, 16));
            potionImg.add(tmp);
        }

        BufferedImage containerSprite = LoadSave.GetPlayerAtlas(LoadSave.CONTAINERS);
        containerImg = new ArrayList<>();
        for (int i = 0; i < 2; ++i)
        {
            ArrayList<BufferedImage> tmp = new ArrayList<>();
            for (int j = 0; j < 8; ++j)
                tmp.add(containerSprite.getSubimage(40 * j, 30 * i, 40, 30));
            containerImg.add(tmp);
        }

        spikeImage = LoadSave.GetPlayerAtlas(LoadSave.TRAP_ATLAS);
    }

    public void update()
    {
        for (Potion i : potions)
            if (i.isActive())
                i.update();
        for (GameContainer i : gameContainers)
            if (i.isActive())
                i.update();
    }

    public void draw(Graphics g, int xLevelOffset)
    {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);
        drawTraps(g, xLevelOffset);
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
                g.drawImage(potionImg.get(idx).get(i.getAniIdx()), (int) (i.getHitbox().x - i.getxDrawOffset() - xLevelOffset), (int) (i.getHitbox().y - i.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT, null);
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
                g.drawImage(containerImg.get(idx).get(i.getAniIdx()), (int) (i.getHitbox().x - i.getxDrawOffset() - xLevelOffset), (int) (i.getHitbox().y - i.getyDrawOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
        }
    }

    public void loadObjects(Level newLevel)
    {
        potions = new ArrayList<>(newLevel.getPotions());
        gameContainers = new ArrayList<>(newLevel.getGameContainers());
        spikes = newLevel.getSpikes();
    }

    public void resetAll()
    {
        loadObjects(playing.getLevelManager().getCurLevel());

        for (Potion i : potions)
            i.reset();
        for (GameContainer i : gameContainers)
            i.reset();
    }
}
