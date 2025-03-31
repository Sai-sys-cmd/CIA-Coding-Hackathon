//ENTITY FILE
import java.awt.image.BufferedImage;

//The entity class repersents any game object that has a position, speed, and direction. 

public class Entity {
  public int x; // X-coordinate position of entity
  public int y; // Y-coordinate position of entity
  public int speed; // speed at which entity moves

  //sprites images for diff movement direction (in animation for MC walking) MC = main character
  public BufferedImage up1,up2, down1,down2, left1,left2, right1,right2;
  public String direction; //stores current movement direction
  public int spriteCounter = 0; //counter for animation timing
  public int spriteNum = 0; //tracks current sprite frame

}