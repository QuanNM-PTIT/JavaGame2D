package Main;

public class Game
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    public Game()
    {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
    }
}
