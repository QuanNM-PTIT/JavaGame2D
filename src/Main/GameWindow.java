package Main;

import javax.swing.*;

public class GameWindow
{
    private JFrame jFrame;

    public GameWindow(GamePanel gamePanel)
    {
        jFrame = new JFrame();
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(gamePanel);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
