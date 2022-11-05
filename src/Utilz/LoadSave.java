package Utilz;

import Entities.Crabby;
import Main.Game;

import static Utilz.Constants.UI.EnemyConstants.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoadSave
{
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";

    public static BufferedImage GetPlayerAtlas(String fileName)
    {
        BufferedImage img = null;
        InputStream inp = LoadSave.class.getResourceAsStream("/" + fileName);
        try
        {
            img = ImageIO.read(inp);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                inp.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static ArrayList<Crabby> GetCrabs()
    {
        BufferedImage img = GetPlayerAtlas(LEVEL_ONE_DATA);
        ArrayList<Crabby> res = new ArrayList<Crabby>();
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getGreen();
                if (val == CRABBY) // Color: 0b009a
                    res.add(new Crabby(j * Game.TILES_SIZE, i * Game.TILES_SIZE));
            }
        }
        return res;
    }

    public static int[][] GetLevelData()
    {
        BufferedImage img = GetPlayerAtlas(LEVEL_ONE_DATA);
        int[][] lvData = new int[img.getHeight()][img.getWidth()];
        for (int i = 0; i < img.getHeight(); ++i)
        {
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getRed();
                if (val >= 48)
                    val = 0;
                lvData[i][j] = color.getRed();
            }
        }
        return lvData;
    }
}
