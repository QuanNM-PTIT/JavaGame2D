package Entities;

import Main.Game;

import static Utilz.Constants.UI.EnemyConstants.*;

public class Crabby extends Enemy
{

    public Crabby(float x, float y)
    {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        intitHitbox(x, y, (int) (22 * Game.SCALE), (int) (19 * Game.SCALE));
    }
}
