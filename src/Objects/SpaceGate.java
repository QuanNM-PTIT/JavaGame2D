package Objects;

import Main.Game;

import static Utilz.Constants.ObjectConstants.SPACE_GATE;

public class SpaceGate extends GameObject
{
    public SpaceGate(int x, int y, int objType)
    {
        super(x, y, SPACE_GATE);
        initHitbox(10, 10);
        doAnimation = true;
        hitbox.x -= (int) (4 * Game.SCALE);
        hitbox.y += (int) (6 * Game.SCALE);
        active = false;
    }

    public void update()
    {
        if (doAnimation)
            updateAnimationTick();
    }
}
