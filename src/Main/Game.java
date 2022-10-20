package Main;

public class Game implements Runnable
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 120;

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

    @Override
    public void run()
    {
        double timePerFrame = 1e9 / FPS;
        long now, lastFrame = System.nanoTime();
        long frame = 0, lastCheck = System.currentTimeMillis();
        while (true)
        {
            now = System.nanoTime();
            if (now - lastFrame >= timePerFrame)
            {
                gamePanel.repaint();
                lastFrame = now;
                ++frame;
            }
            if (System.currentTimeMillis() - lastCheck >= 1000)
            {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frame);
                frame = 0;
            }
        }
    }
}
