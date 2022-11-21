package Objects;

import GameStates.Playing;
import Utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Utilz.Constants.ObjectConstants.*;

public class ObjectManager
{
    private Playing playing;
    private ArrayList<ArrayList<BufferedImage>> potionImg, containerImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> gameContainers;

    public ObjectManager(Playing playing)
    {
        this.playing = playing;
        loadImages();
        potions = new ArrayList<>();
        gameContainers = new ArrayList<>();
        potions.add(new Potion(300, 300, RED_POTION));
        potions.add(new Potion(400, 300, BLUE_POTION));
        gameContainers.add(new GameContainer(500, 300, BARREL));
        gameContainers.add(new GameContainer(600, 300, BOX));
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
}
