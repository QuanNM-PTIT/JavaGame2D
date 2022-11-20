package GameStates;

import Entities.EnemyManager;
import Entities.Player;
import Levels.LevelManager;
import Main.Game;
import UI.GameOverOverlay;
import UI.LevelCompletedOverlay;
import UI.PauseOverlay;
import Utilz.LoadSave;
import static Utilz.Constants.UI.Environment.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Playing extends State implements Statemethods
{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;
    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvlOffset;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private ArrayList<Integer> smallCloudsPos;
    private Random rnd = new Random();

    private boolean gameOver = false;
    private boolean levelCompleted = false;

    public Playing(Game game)
    {
        super(game);
        initClasses();
        backgroundImg = LoadSave.GetPlayerAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetPlayerAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetPlayerAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new ArrayList<Integer>();
        for (int i = 0; i < 8; ++i)
            smallCloudsPos.add((int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE)));
        calcLevelOffsets();
        loadStartLevel();
    }

    public void loadNextLevel()
    {
        resetAll();
        levelManager.loadNextLevel();
        player.setPlayerSpawn(levelManager.getCurLevel().getPlayerSpawn());
    }

    private void loadStartLevel()
    {
        enemyManager.loadEnemies(levelManager.getCurLevel());
    }

    private void calcLevelOffsets()
    {
        maxLvlOffset = levelManager.getCurLevel().getLevelOffset();
    }

    private void initClasses()
    {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (64 * game.SCALE), (int) (40 * game.SCALE), this);
        player.setPlayerSpawn(levelManager.getCurLevel().getPlayerSpawn());
        player.loadLvlData(levelManager.getCurLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update()
    {
        if (paused)
            pauseOverlay.update();
        else if (levelCompleted)
            levelCompletedOverlay.update();
        else if (!paused)
        {
            levelManager.update();
            enemyManager.update(levelManager.getCurLevel().getLevelData(), player);
            player.update();
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder()
    {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;
        if (diff > rightBorder)
            xLvlOffset += diff- rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;
        if (xLvlOffset > maxLvlOffset)
            xLvlOffset = maxLvlOffset;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);
        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        if (paused)
        {
            g.setColor(new Color(0, 0, 0, 125));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
        else if (gameOver)
            gameOverOverlay.draw(g);
        else if (levelCompleted)
            levelCompletedOverlay.draw(g);
    }

    private void drawClouds(Graphics g)
    {
        for (int i = 0; i < 3; ++i)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        for (int i = 0; i < 8; ++i)
            g.drawImage(smallCloud, i * SMALL_CLOUD_WIDTH * 4 - (int) (xLvlOffset * 0.5), smallCloudsPos.get(i), SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public void resetAll()
    {
        gameOver = false;
        paused = false;
        levelCompleted = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }

    public void setGameOver(boolean gameOver)
    {
        this.gameOver = gameOver;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox)
    {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void mouseDragged(MouseEvent e)
    {
        if (!gameOver && paused)
            pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!gameOver)
        {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (levelCompleted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (!gameOver)
        {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (levelCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (!gameOver)
        {
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (levelCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_LEFT:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_A:
                    player.setAttacking(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (!gameOver)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_LEFT:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void setLevelCompleted(boolean levelCompleted)
    {
        this.levelCompleted = levelCompleted;
    }

    public void setMaxLvlOffset(int maxLvlOffset)
    {
        this.maxLvlOffset = maxLvlOffset;
    }

    public void unpauseGame()
    {
        paused = false;
    }

    public void windowFocusLost()
    {
        player.resetDirBooleans();
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public EnemyManager getEnemyManager()
    {
        return enemyManager;
    }
}
