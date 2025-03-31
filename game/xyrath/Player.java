// imports
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Color;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    int spriteCounter = 0;
    int spriteNum = 1; // Tracks which sprite to display for animation
    BufferedImage sub1, sub2; // Define sprite images

    // Constructor to initialize with references to GamePanel and KeyHandler
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues(); // Set initial position and speed
        getPlayerImage(); // Load player sprites
    }

    // Set the default position, speed, and direction of the player
    public void setDefaultValues() {
        x = 100; // Initial x-position
        y = 100; // Initial y-position
        speed = 2; // Player movement speed
        direction = "down"; // Initial movement direction
    }

    // Load the sprite images
    public void getPlayerImage() {
        try {
            sub1 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/sub1.png"));
            sub2 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/sub2.png"));
        } catch (IOException e) {
            e.printStackTrace(); // Print error if loading fails
        } catch (NullPointerException e) {
            System.out.println("Error: Image not found");
        }
    }

    // Update player position based on key inputs
    public void update() {
        // Play the animation only if a key is pressed
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            // Move player in the respective direction
            if (keyH.upPressed) {
                direction = "up";
                y -= speed;
            } else if (keyH.downPressed) {
                direction = "down";
                y += speed;
            } else if (keyH.leftPressed) {
                direction = "left";
                x -= speed;
            } else if (keyH.rightPressed) {
                direction = "right";
                x += speed;
            }

            // Handle sprite animation
            spriteCounter++;
            if (spriteCounter > 15) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    // Draw the player sprite with shadow and lighting effects
public void draw(Graphics2D g2) {
    BufferedImage image = (spriteNum == 1) ? sub1 : sub2;
    
    // Draw shadow effect
    g2.setColor(new Color(0, 0, 0, 100));
    g2.fillOval(x + gp.tileSize / 4, y + gp.tileSize * 2 - 10, gp.tileSize, 10);
    
    // Draw player sprite (2x size)
    g2.drawImage(image, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
    
    // Simulate simple lighting by adding a highlight
    g2.setColor(new Color(255, 255, 255, 50));
    g2.fillOval(x + gp.tileSize / 3, y + gp.tileSize / 3, gp.tileSize / 2, gp.tileSize / 2);
}


    // Get the bounds of the player (used for collision detection)
    public Rectangle getBounds() {
        return new Rectangle(x, y, gp.tileSize, gp.tileSize); // Use tileSize from GamePanel
    }
}
