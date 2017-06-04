/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicballs;

import database.DBHandler;
import items.Ball;
import items.Obstacle;
import items.StopItem;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import rules.SpaceRules;

/**
 *
 * @author Liam-Portatil
 */
public class Space extends Canvas implements Runnable {

    /**
     * Global parameters
     */
    private static int spaceWidth = 1280;
    private static int spaceHeight = 720;
    private Dimension d;
    private String name;

    private int ballLimit = 2;
    private int stopItemsLimit = 1;

    private CopyOnWriteArrayList<Ball> balls;
    private ArrayList<StopItem> stopItems;
    private ArrayList<Obstacle> obstacleList;
    private Obstacle obstaculo;

    private final float gravityX = -3f;
    private final float gravityY = 6f;


    /**
     * Main constructor
     *
     * @param spaceWidth
     * @param spaceHeigth
     * @param ballLimit
     */
    public Space(int spaceWidth, int spaceHeigth, int ballLimit) {
        this.ballLimit = ballLimit;
        d = new Dimension(spaceWidth, spaceHeight);

        init();

    }

    public Space(int spaceWidth, int spaceHeigth, String spaceName) {
        this.name = spaceName;
        d = new Dimension(spaceWidth, spaceHeight);
//        System.out.println(getName());
        init();
    }

    public Space() {
    }

    /**
     * Init
     */
    private void init() {
        //JPanel parameters
        setPreferredSize(d);

        //Player
        //player = new Player(30, 300, 10, 10, 10, 1, this);
        //Ball parameters
        balls = new CopyOnWriteArrayList<>();
//        balls = new ArrayList<>();
        stopItems = new ArrayList<>();
        obstacleList = new ArrayList<>();

        Ball b;
        DBHandler db = new DBHandler();
        List<Ball> balllist = db.selectBalls(name);
        //System.out.println(name);
        String typechar = "";
        for (int i = 0; i < balllist.size(); i++) {

            typechar = String.valueOf(balllist.get(i).getType().name().charAt(0));
            if (SpaceRules.sizes) {
                //System.out.println(list.get(i).getType().name());
                b = new Ball(balllist.get(i).getX(), balllist.get(i).getY(), (float) balllist.get(i).getSpeed(), balllist.get(i).getAccel(), balllist.get(i).getRadius(), balllist.get(i).getAngle(), typechar);
                balls.add(b);
            } else {
                if (i < 8) {
                    // System.out.println(list.get(i).getX()+" "+ list.get(i).getY()+" "+  (float) list.get(i).getSpeed()+" "+  list.get(i).getAccel()+" "+ list.get(i).getRadius()+" "+  list.get(i).getAngle()+" "+ typechar);
                    b = new Ball(balllist.get(i).getX(), balllist.get(i).getY(), (float) balllist.get(i).getSpeed(), balllist.get(i).getAccel(), balllist.get(i).getRadius(), balllist.get(i).getAngle(), typechar);
                } else {
                    b = new Ball(balllist.get(i).getX(), balllist.get(i).getY(), (float) balllist.get(i).getSpeed(), balllist.get(i).getAccel(), balllist.get(i).getRadius(), balllist.get(i).getAngle(), typechar);
                }
                balls.add(b);
            }
        }

        StopItem stopItem;
        List<StopItem> stoplist = db.selectStopItems(name);
        for (int i = 0; i < stoplist.size(); i++) {
            stopItem = new StopItem(stoplist.get(i).getX(), stoplist.get(i).getY(), stoplist.get(i).getWidth(), stoplist.get(i).getHeight(), this);
            stopItems.add(stopItem);
        }

        Obstacle obstacle;
        List<Obstacle> obstlist = db.selectObstacles(name);
        for (int i = 0; i < obstlist.size(); i++) {

            obstacle = new Obstacle(obstlist.get(i).getX(), obstlist.get(i).getY(), obstlist.get(i).getWidth(), obstlist.get(i).getHeight());
            obstacleList.add(obstacle);

        }

        for (int con = 0; con < balls.size(); con++) {
//           new Thread(balls.get(con)).start();
            new Thread(new ThreadBall(balls.get(con), this)).start();
        }

    }

    //Space painter
    public synchronized void paint() {
        BufferStrategy bs;

        bs = this.getBufferStrategy();
        if (bs == null) {
            return; // =======================================================>>
        }

        Graphics gg = bs.getDrawGraphics();

        gg.setColor(Color.black);
        gg.fillRect(0, 0, spaceWidth, spaceHeight);

        for (int i = 0; i < stopItems.size(); i++) {
            stopItems.get(i).draw(gg);
        }
        //stopItems.get(0).draw(gg);
        //stopItems.get(1).draw(gg);
        
        for (int i = 0; i < obstacleList.size(); i++) {
            obstacleList.get(i).draw(gg);
        }

        for (int con = 0; con < balls.size(); con++) {
            balls.get(con).draw(gg);
        }

        //player.draw(gg);
        bs.show();

        gg.dispose();

    }

    public synchronized void delete(Ball b, int con) {
        balls.get(con).stopBall();
        balls.remove(con);
    }

    public synchronized void delete(int con) {
        balls.get(con).stopBall();
        balls.remove(con);
    }

    public CopyOnWriteArrayList<Ball> getBalls() {
        return balls;
    }

    public ArrayList<StopItem> getStopItems() {
        return stopItems;
    }

//    public Obstacle getObstaculo() {
//        return obstaculo;
//    }
    
    /**
     * método mio
     * @return 
     */
    public ArrayList<Obstacle> getObstacle(){
        return obstacleList;
    }

    public float getGravityX() {
        return gravityX;
    }

    public float getGravityY() {
        return gravityY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBallLimit() {
        return ballLimit;
    }

    public void setBallLimit(int ballLimit) {
        this.ballLimit = ballLimit;
    }

    public static int getSpaceWidth() {
        return spaceWidth;
    }

    public static int getSpaceHeight() {
        return spaceHeight;
    }

    public static void setSpaceWidth(int spaceWidth) {
        Space.spaceWidth = spaceWidth;
    }

    public static void setSpaceHeight(int spaceHeight) {
        Space.spaceHeight = spaceHeight;
    }

    public Dimension getD() {
        return d;
    }

    public void addBall() {
        Ball b = new Ball(240, 240, 2, 1, 20, 325, "N");
        b.setColor(Color.yellow);
        //new Thread(b).start();
        balls.add(b);
    }

    /**
     * Main life cicle
     */
    @Override
    public void run() {
        this.createBufferStrategy(2);
        while (true) {
            this.paint();
            //this.checkHoles();
            try {
                Thread.sleep(15); // nano -> ms
            } catch (InterruptedException ex) {
            }
        }
    }

}
