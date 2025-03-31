//PLAYER.JAVA FILE
//imports
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Color;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    int spriteCounter = 0;
    int spriteNum = 1; //tracks which sprite to display for animation
  
//constructor to initialize with references to GamePanel and KeyHandler
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues(); //set initial position and speed
        getPlayerImage(); //load player sprites
    }

  //set the default position, speed, and direction of the player
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 2;
        direction = "down";
    }

  //load the sprite images based on controls
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/up2.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/down2.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/right2.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/xyrath/Player_Sprites/left2.png"));
        } catch (IOException e) {
            e.printStackTrace(); // print error ifg fail to load
        }
    }

  //update player position based on key points
    public void update() {
      //play the animation only if the key is pressed
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
          //play the animation depending on the direction
            if (keyH.upPressed) {
                direction = "up";
                y -= speed;
            } else if (keyH.downPressed) {
                y += speed;
                direction = "down";
            } else if (keyH.leftPressed) {
                x -= speed;
                direction = "left";
            } else if (keyH.rightPressed) {
                x += speed;
                direction = "right";
            }

          //handle sprite animation
            spriteCounter++;
            if (spriteCounter > 15) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

  //draw the player sprite with shadow and lighting effects
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
          case "up":
            if (spriteNum == 1) {
              image = up1;
            } else {
              image = up2;
            }
            break;
          case "down":
            if (spriteNum == 1) {
              image = down1;
            } else {
              image = down2;
            }
            break;
          case "left":
            if (spriteNum == 1) {
              image = left1;
            } else {
              image = left2;
            }
            break;
          case "right":
            if (spriteNum == 1) {
              image = right1;
            } else {
              image = right2;
            }
            break;
        }

        // Draw shadow effect
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillOval(x + gp.tileSize / 4, y + gp.tileSize - 10, gp.tileSize / 2, 10);

        // Draw player sprite with lighting effect
        // Draw player sprite with lighting effect
        g2.drawImage(image, x, y, 32, 32, null);


        // Simulate simple lighting by adding a highlight
        g2.setColor(new Color(255, 255, 255, 50));
        g2.fillOval(x + gp.tileSize / 6, y + gp.tileSize / 6, gp.tileSize / 3, gp.tileSize / 3);
    }
}
