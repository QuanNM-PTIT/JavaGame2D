package Utilz;

import Main.Game;

public class Constants
{
    public static class UI
    {
        public static class Environment
        {
            public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
            public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
            public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
            public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;

            public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
            public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
            public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
            public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class EnemyConstants
        {
            public static final int CRABBY = 0;
            public static final int IDLE = 0;
            public static final int RUNNING = 1;
            public static final int ATTACK = 2;
            public static final int HIT = 3;
            public static final int DEAD = 4;

            public static final int CRABBY_WIDTH_DEFAULT = 72;
            public static final int CRABBY_HEIGHT_DEFAULT = 32;

            public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
            public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);

            public static int GetSpriteAmount(int enemyType, int enemyState)
            {
                switch (enemyType)
                {
                    case CRABBY:
                        switch (enemyState)
                        {
                            case IDLE:
                                return 9;
                            case RUNNING:
                                return 6;
                            case ATTACK:
                                return 7;
                            case HIT:
                                return 4;
                            case DEAD:
                                return 5;
                        }
                }
                return 0;
            }
        }

        public static class Buttons
        {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons
        {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class URMButtons
        {
            public static final int URM_SIZE_DEFAULT = 56;
            public static final int URM_SIZE = (int) (URM_SIZE_DEFAULT * Game.SCALE);
        }

        public static class VolumeButtons
        {
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class Directions
    {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants
    {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int GROUND = 4;
        public static final int HIT = 5;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_JUMP_1 = 7;
        public static final int ATTACK_JUMP_2 = 8;

        public static int GetSpriteAmount(int playerAction)
        {
            switch (playerAction)
            {
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 4;
                case JUMP: case ATTACK_1: case ATTACK_JUMP_1: case ATTACK_JUMP_2:
                    return 3;
                case GROUND:
                    return 2;
                default:
                    return 1;
            }
        }
    }
}
