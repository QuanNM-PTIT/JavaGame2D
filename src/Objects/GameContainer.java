package Objects;

import Main.Game;

import static Utilz.Constants.ObjectConstants.*;

public class GameContainer extends GameObject
{
    public GameContainer(int x, int y, int objType)
    {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox()
    {
        if (objType == BOX)
        {
            intitHitbox(25, 18);
            xDrawOffset = (int) (7 * Game.SCALE);
            yDrawOffset = (int) (12 * Game.SCALE);
        }
        else
        {
            intitHitbox(23, 25);
            xDrawOffset = (int) (8 * Game.SCALE);
            yDrawOffset = (int) (5 * Game.SCALE);
        }
    }

    public void update()
    {
        if (doAnimation)
            updateAnimationTick();
    }
}
