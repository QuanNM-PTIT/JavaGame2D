package Main;

import Entities.Player;
import Levels.LevelManager;

import java.awt.*;

public class Game implements Runnable
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 120;
    private final int UPS = 200;
    private Player player;
    private LevelManager levelManager;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game()
    {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses()
    {
        player = new Player(200, 200);
        levelManager = new LevelManager(this);
    }

    private void startGameLoop()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update()
    {
        player.update();
        levelManager.update();
    }

    public void render(Graphics g)
    {
        levelManager.draw(g);
        player.render(g);
    }

    @Override
    public void run()
    {
        double timePerFrame = 1e9 / FPS;
        double timePerUpdate = 1e9 / UPS;
        long frame = 0, lastCheck = System.currentTimeMillis();
        long prevTime = System.nanoTime(), updates = 0;
        double deltaU = 0;
        double deltaF = 0;
        while (true)
        {
            long curTime = System.nanoTime();
            deltaU += (curTime - prevTime) / timePerUpdate;
            deltaF += (curTime - prevTime) / timePerFrame;
            prevTime = curTime;
            if (deltaU >= 1)
            {
                update();
                ++updates;
                --deltaU;
            }
            if (deltaF >= 1)
            {
                gamePanel.repaint();
                ++frame;
                --deltaF;
            }
            if (System.currentTimeMillis() - lastCheck >= 1000)
            {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frame + " | UPS: " + updates);
                frame = updates = 0;
            }
        }
    }

    public void windowFocusLost()
    {
        player.resetDirBooleans();
    }

    public Player getPlayer()
    {
        return this.player;
    }
}
