//GAMEANEL FILE
//imports
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


//The GamePanel file here will handel the main game loop and the user input.
public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int tileSize = originalTileSize; // Remove scale, keep original size, so each tile size remains 16 pixels
    final int maxScreenCol = 32; //   number of columns on the screen
    final int maxScreenRow = 24; //   number of rows on the screen
    final int screenWidth = tileSize * maxScreenCol; // total screen width
    final int screenHeight = tileSize * maxScreenRow; // total screen height

    int fps = 60; //frames per second 
    KeyHandler keyH = new KeyHandler(this); //handles user input
    Thread gameThread; //thread for running the game loop
    Player player = new Player(this, keyH); 

    public int gameState = 0; //how to work main menu 0 = menu, 1 = playing, 2 = help, 3 = story. Also to switch state its enter = play, h = instruction and t = storyline.

    BufferedImage titleScreen; //image for game title screen

  //set up game panel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black); //background color for panel
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        loadTitleScreen();
    }

  //load title screen image from the title screen file
    public void loadTitleScreen() {
        try {
            titleScreen = ImageIO.read(getClass().getResourceAsStream("/xyrath/TitleScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  //starts the game thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

  //the main game loop, updates and repains the screen at 60fps

    public void run() {
        double drawInterval = 1000000000.0 / fps;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update(); //updates game logic
            repaint();//repains the screen

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000; //convert nanoseconds to milliseconds

                if (remainingTime < 0) remainingTime = 0;

                Thread.sleep((long) remainingTime); // pause to maintain FPS / framerate
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

  //updates game state (Only when playing game)
    public void update() {
        if (gameState == 1) {
            player.update(); //updates player movement and animations
        }
    }

  //handles rendering the game and menus
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == 0) {
            // Draw title screen without scaling
            g2.drawImage(titleScreen, 0, 0, null); // Remove scaling logic and draws title screen
        } else if (gameState == 2) {
            drawHelpScreen(g2); // displays help screen
        } else if (gameState == 3) {
            drawStoryScreen(g2);//displays story screen
        } else {
            player.draw(g2); //draws the player
        }

        g2.dispose();
    }

  //displays control instructions
    public void drawHelpScreen(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));

        g2.drawString("Controls:", 20, 30);
        g2.drawString("W - Move Up", 20, 60);
        g2.drawString("A - Move Left", 20, 90);
        g2.drawString("S - Move Down", 20, 120);
        g2.drawString("D - Move Right", 20, 150);
        g2.drawString("Press 'H' to return to the title screen.", 20, 180);
        g2.drawString("Press 'Enter' to start playing.", 20, 210);
        g2.drawString("Press 'T' to see the story from the title screen.", 20, 240);
    }

  // displays the game story
    public void drawStoryScreen(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));

        String[] story = {
            "Alien Hunter: The Xyrrath Invasion",
            "In the year 2147, a deep-space mining colony on Xyrrath-9 went dark.",
            "The United Earth Federation sent distress signals, but no response came back.",
            "Scans detected unnatural heat signatures and strange bio-readings.",
            "You are Lieutenant Dax Von, an elite soldier from the UEF’s Xenothreat Response Division.",
            "Your mission: Find survivors. Eliminate the threat. Secure the colony.",
            "But this isn't a rescue mission — it's a warzone.",
            "The colony is overrun with Xyrrathian abominations — twisted, insectoid creatures.",
            "Your only allies: a plasma rifle, a combat blade, and your wits.",
            "The only way off this rock? Kill everything that moves.",
            "Press 'S' to return to the title screen."
        };

        int y = 30;
        for (String line : story) {
            g2.drawString(line, 20, y);
            y += 30;
        }
    }
}
