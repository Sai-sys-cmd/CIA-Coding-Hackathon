//MAIN.JAVA FILE
//imports
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

      //create the main game window
      JFrame window = new JFrame();
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setResizable(false);
      window.setTitle("Xyrath 9");

      //create an instance of gamepanel and add it to the window
      GamePanel gamePanel = new GamePanel();
      window.add(gamePanel);

      window.pack(); //adjust window size to preferred size

      window.setLocationRelativeTo(null); //centers the window
      window.setVisible(true); // makes window visible
      gamePanel.startGameThread(); //start the game loop
    }
}
