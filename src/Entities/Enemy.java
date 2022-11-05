package Entities;

import static Utilz.Constants.UI.EnemyConstants.*;

public abstract class Enemy extends Entity
{
    private int animationIdx, enemyState, enemyType;
    private int animationTick, anmationSpeed = 25;

    public Enemy(float x, float y, int width, int height, int enemyType)
    {
        super(x, y, width, height);
        this.enemyType = enemyType;
        intitHitbox(x, y, width, height);
    }

    private void updateAnimationTick()
    {
        ++animationTick;
        if (animationTick >= anmationSpeed)
        {
            animationTick = 0;
            ++animationIdx;
            if (animationIdx >= GetSpriteAmount(enemyType, enemyState))
                animationIdx = 0;
        }
    }

    public void update()
    {
        updateAnimationTick();
    }

    public int getAnimationIdx()
    {
        return animationIdx;
    }

    public int getEnemyState()
    {
        return enemyState;
    }
}
