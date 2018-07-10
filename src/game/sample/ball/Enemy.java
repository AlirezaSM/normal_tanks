package game.sample.ball;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Enemy {
    BufferedImage enemyImg;
    // speed is tile per frame
    public  double speed;
    // firing speed is bullet per second
    public double firingSpeed;
    double centerTileX;
    double centerTileY;
    int enemyWidth;
    int enemyHeight;
    int locX;
    int locY;
    int firingLocX;
    int firingLocY;
    int startTile;
    public double movingAngle = 1;
    boolean visible;
    boolean triggered;
    boolean alive = true;
    int health = 100;
    int distanceBetweenFiringPointAndCenter;
    Rectangle enemyRectangle;
    int i = 0;


    public Enemy(String imageName, double centerTileX, double centerTileY,
                 int width,int height, double speed, double firingSpeed, int health, int distance,
                 int firingLocX,int firingLocY) {
        try {
            enemyImg = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.centerTileX = centerTileX;
        this.centerTileY = centerTileY;
        enemyWidth = width;
        enemyHeight = height;
        locX = ((int) centerTileX * Tile.tileWidth) + (enemyWidth / 2);
        locY = (Map.screenHeight - ((int) centerTileY - startTile) * Tile.tileHeight) + (enemyHeight / 2);
        startTile = GameState.cameraY / Tile.tileHeight;
        this.speed = speed;
        this.firingSpeed = firingSpeed;
        visible = false;
        triggered = false;
        this.health = health;
        distanceBetweenFiringPointAndCenter = distance;
        this.firingLocX =  firingLocX;
        this.firingLocY =  firingLocY;
    }

    public void checkTriggered () {
        if (Math.abs(GameState.tankCenterTileY - centerTileY) < 30)
            triggered = true;
    }

    public void draw (Graphics2D g2d) {
        if (alive) {
            checkTriggered();
            g2d.drawImage(enemyImg, locX, locY, null);
        }
    }

    public void move(Graphics2D g2d) {
        if (triggered) {
            movingAngle = Math.atan2((locY - GameState.tankCenterY), (locX - GameState.tankCenterX));
            centerTileX = (centerTileX - speed * Math.cos(movingAngle));
            centerTileY = (centerTileY + speed * Math.sin(movingAngle));
            firingBullet();
            updateLocs();
        }
    }

    public void firingBullet () {
        Random r = new Random();
        if (r.nextInt((int) (60 / firingSpeed)) == 1) {
            int currentFiringLocX = (int) (locX + firingLocX + distanceBetweenFiringPointAndCenter * Math.cos(movingAngle));
            int currentFiringLocY = (int) (locY + firingLocY - distanceBetweenFiringPointAndCenter * Math.sin(movingAngle));
            GameFrame.bullets.add(new EnemyBullet(currentFiringLocX, currentFiringLocY, movingAngle,true));
            i++;
            System.out.println(i);
        }
    }

    public void checkVisibility () {
        int endTile = startTile + (Tile.numOfVerticalTiles / Map.numOfVerticalScreens);
        if (centerTileY >= startTile && centerTileY <= endTile)
            visible = true;
    }

    public void updateLocs () {
        startTile = GameState.cameraY / Tile.tileHeight;
        locX = (int) (centerTileX * Tile.tileWidth) + (enemyWidth / 2);
        locY = (int) (Map.screenHeight - (centerTileY - startTile) * Tile.tileHeight) + (enemyHeight / 2);
    }

    public void updateRectangles () {
        enemyRectangle = new Rectangle(locX,locY,enemyWidth,enemyHeight);
    }


}
