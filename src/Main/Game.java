package Main;

import GameStates.GameOptions;
import GameStates.Gamestate;
import GameStates.Menu;
import GameStates.Playing;
import UI.AudioOptions;

import java.awt.*;

public class Game implements Runnable
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 120;
    private final int UPS = 200;

    private Menu menu;
    private Playing playing;
    private AudioOptions audioOptions;
    private GameOptions gameOptions;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.6f;
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
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses()
    {
        audioOptions = new AudioOptions();
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this);
    }

    private void startGameLoop()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update()
    {
        switch (Gamestate.state)
        {
            case PLAYING:
                playing.update();
                break;
            case MENU:
                menu.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g)
    {
        switch (Gamestate.state)
        {
            case PLAYING:
                playing.draw(g);
                break;
            case MENU:
                menu.draw(g);
                break;
            case OPTIONS:
                gameOptions.draw(g);
                break;
            default:
                break;
        }
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
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Menu getMenu()
    {
        return menu;
    }

    public Playing getPlaying()
    {
        return playing;
    }

    public AudioOptions getAudioOptions()
    {
        return audioOptions;
    }

    public GameOptions getGameOptions()
    {
        return gameOptions;
    }
}
