package Main;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow
{
    private JFrame jFrame;

    public GameWindow(GamePanel gamePanel)
    {
        jFrame = new JFrame("The Simp Boiz");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(gamePanel);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.addWindowFocusListener(new WindowFocusListener()
        {
            @Override
            public void windowGainedFocus(WindowEvent e)
            {

            }

            @Override
            public void windowLostFocus(WindowEvent e)
            {
                gamePanel.getGame().windowFocusLost();
            }
        });
    }
}
