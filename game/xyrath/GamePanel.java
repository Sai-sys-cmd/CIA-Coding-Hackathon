import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {
    final int tileSize = 16;
    final int screenWidth = 512;
    final int screenHeight = 512;

    int fps = 60;
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    Player player = new Player(this, keyH);
    
    public int gameState = 0; // 0 = Menu, 1 = Playing, 2 = Game Over
    int score = 0;
    double speed = 2; // Initial speed

    Random random = new Random();
    ArrayList<Point> plastic = new ArrayList<>();
    ArrayList<Point> fish = new ArrayList<>();
    BufferedImage menuScreen; // Menu screen image
    BufferedImage fish1; // First fish image
    BufferedImage fish2; // Second fish image
    BufferedImage bottle; // Bottle image for plastic
    boolean fishAnimationToggle = true; // Toggle between fish1 and fish2

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.blue);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        loadMenuScreen();
        loadFishImages();
        loadBottleImage();
    }

    public void loadMenuScreen() {
        try {
            menuScreen = ImageIO.read(getClass().getResourceAsStream("/xyrath/TitleScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFishImages() {
        try {
            fish1 = ImageIO.read(getClass().getResourceAsStream("/xyrath/fish1.png"));
            fish2 = ImageIO.read(getClass().getResourceAsStream("/xyrath/fish2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBottleImage() {
        try {
            bottle = ImageIO.read(getClass().getResourceAsStream("/xyrath/bottle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000.0 / fps;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0) remainingTime = 0;

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (gameState == 1) { // Playing
            player.update();
            speed += 0.001; // Gradually increase speed
            
            // Move obstacles left
            plastic.removeIf(p -> {
                p.x -= speed;
                if (player.getBounds().intersects(new Rectangle(p.x, p.y, tileSize * 2, tileSize * 2))) {
                    score += 1; // Increase score when player collects a bottle
                    return true; // Remove the bottle
                }
                return p.x + tileSize * 2 < 0; // Remove if off-screen
            });

            fish.removeIf(f -> {
                f.x -= speed;
                if (player.getBounds().intersects(new Rectangle(f.x, f.y, tileSize * 2, tileSize * 2))) {
                    gameState = 2; // Game Over
                }
                return f.x + tileSize * 2 < 0;
            });

            // Respawn obstacles
            if (random.nextInt(100) < 2) spawnPlastic();
            if (random.nextInt(200) < 2) spawnFish();

            // Toggle fish animation
            fishAnimationToggle = !fishAnimationToggle;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == 0) { // Menu
            if (menuScreen != null) {
                g2.drawImage(menuScreen, 0, 0, screenWidth, screenHeight, null);
            } else {
                g2.setColor(Color.black);
                g2.setFont(new Font("Arial", Font.BOLD, 40));
                g2.drawString("Ocean Cleanup", 120, 200);
                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.drawString("Press ENTER to Start", 160, 250);
            }
        } else if (gameState == 1) { // Playing
            // Draw player
            player.draw(g2);

            // Draw plastic (bottle) scaled by 2x
            for (Point p : plastic) {
                g2.drawImage(bottle, p.x, p.y, tileSize * 2, tileSize * 2, null);
            }

            // Draw fish with animation, scaled by 2x
            for (Point f : fish) {
                if (fishAnimationToggle) {
                    g2.drawImage(fish1, f.x, f.y, tileSize * 2, tileSize * 2, null);
                } else {
                    g2.drawImage(fish2, f.x, f.y, tileSize * 2, tileSize * 2, null);
                }
            }

            // Draw score
            g2.setColor(Color.black);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("Score: " + score, 20, 30);
        } else if (gameState == 2) { // Game Over
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setColor(Color.red);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("Game Over!", 150, 200);
            g2.drawString("Score: " + score, 180, 250);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Press R to Restart", 170, 300);
        }

        g2.dispose();
    }

    public void spawnPlastic() {
        plastic.add(new Point(screenWidth, random.nextInt(screenHeight - tileSize * 2)));
    }

    public void spawnFish() {
        fish.add(new Point(screenWidth, random.nextInt(screenHeight - tileSize * 2)));
    }

    public void restartGame() {
        score = 0;
        player.x = 50;
        player.y = screenHeight / 2;
        speed = 2;
        plastic.clear();
        fish.clear();
        gameState = 1;
    }

    // Get the bounds of the player (used for collision detection)
    public Rectangle getBounds() {
        return new Rectangle(player.x, player.y, tileSize * 2, tileSize * 2);  // Use player's x, y for bounds
    }

    public void resetGame() {
        // Reset player position manually (update values as needed)
        player.x = 50; // Starting X position
        player.y = screenHeight / 2; // Starting Y position

        // Reset player's health if you have a health variable
        // player.health = player.maxHealth; // Uncomment and update based on your setup

        // Clear and reset obstacles (plastic, fish)
        plastic.clear();
        fish.clear();

        // Reset score or other game-related variables if needed
        score = 0;
    }
}
