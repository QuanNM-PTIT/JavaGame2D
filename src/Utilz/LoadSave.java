package Utilz;

import Entities.Crabby;
import Main.Game;

import static Utilz.Constants.UI.EnemyConstants.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class LoadSave
{
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
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
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String POTIONS = "potions_sprites.png";
    public static final String CONTAINERS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String BALL_ATLAS = "ball_atlas.png";

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

    public static ArrayList<BufferedImage> GetAllLevels()
    {
        URL url = LoadSave.class.getResource("/Levels");
        File file = null;
        try
        {
            file = new File(url.toURI());
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        File[] files = file.listFiles();

        ArrayList<File> sortedFile = new ArrayList<>();
        for (int i = 0; i < files.length; ++i)
        {
            for (File j : files)
            {
                if (j.getName().equals((i + 1) + ".png"))
                {
                    sortedFile.add(j);
                    break;
                }
            }
        }

        ArrayList<BufferedImage> imgs = new ArrayList<>();
        for (File i : sortedFile)
        {
            try
            {
                imgs.add(ImageIO.read(i));
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        return imgs;
    }
}
