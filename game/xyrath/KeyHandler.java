import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    // Boolean variables to track key presses
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    GamePanel gp;

    // Constructor that takes a reference to the GamePanel
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    // Handles key press events
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // if on the title screen and Enter is pressed, switch to game state
        if (gp.gameState == 0 && code == KeyEvent.VK_ENTER) {
            gp.gameState = 1;
        }

        // only allow movement when in playing state
        if (gp.gameState == 1) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
        }

        // Restart the game when 'R' is pressed
        if (code == KeyEvent.VK_R) {
          gp.resetGame(); // Call a method to reset all game variables
          gp.gameState = 0;
        }
    }

    // Handles key release events
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        // stop movement when the key is released
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
    }

    public void keyTyped(KeyEvent e) {
        // not used, but must be defined as part of the KeyListener interface
    }
}
