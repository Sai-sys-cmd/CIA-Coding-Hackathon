//KEYHANDLER FILE
//imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
  //Boolean variables to track key presses
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    GamePanel gp;

  // constructor that takes a reference to the GamePanel
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

  
    public void keyTyped(KeyEvent e) {}

  //handles key press events
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

      //if on the title screen and Enter is pressed, switch to game state
        if (gp.gameState == 0 && code == KeyEvent.VK_ENTER) {
            gp.gameState = 1;
        }

        if (code == KeyEvent.VK_H) {
          if (gp.gameState == 2) {
            gp.gameState = 0;
          } else {
            gp.gameState = 2;
          }
        }
        else if (code == KeyEvent.VK_T) {
          if (gp.gameState == 3) {
            gp.gameState = 0;
          } else {
            gp.gameState = 3;
          }
        }

      //only allow movement when in playing state
        if (gp.gameState == 1) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
        }
    }

  //handles key release events
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

      //stop movement when the key is released
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
    }
}
