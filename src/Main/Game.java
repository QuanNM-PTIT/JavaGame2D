package Main;

public class Game implements Runnable
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 120;
    private final int UPS = 200;

    private void startGameLoop()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public Game()
    {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        startGameLoop();
        gamePanel.requestFocus();
    }

    public void update()
    {
        gamePanel.updateGame();
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
}
