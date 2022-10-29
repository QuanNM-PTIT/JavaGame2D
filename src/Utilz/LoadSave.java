package Utilz;

import Main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave
{
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";

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

    public static int[][] GetLevelData()
    {
        int[][] lvData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetPlayerAtlas(LEVEL_ONE_DATA);
        for (int i = 0; i < img.getHeight(); ++i)
            for (int j = 0; j < img.getWidth(); ++j)
            {
                Color color = new Color(img.getRGB(j, i));
                int val = color.getRed();
                if (val >= 48)
                    val = 0;
                lvData[i][j] = color.getRed();
            }
        return lvData;
    }
}
