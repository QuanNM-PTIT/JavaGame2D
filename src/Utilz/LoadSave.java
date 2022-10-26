package Utilz;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave
{
    public static final String PLAYER_ATLAS = "player_sprites.png";

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
}
