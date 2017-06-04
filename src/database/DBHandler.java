package database;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import items.Ball;
import items.Obstacle;
import items.StopItem;
import physicballs.Space;

/**
 * Created by pepe on 29/03/2017.
 */
public class DBHandler {

    EntityManagerFactory emf;
    EntityManager em;

    public DBHandler(EntityManager em) {
        this.em = em;

    }

    public DBHandler() {

        emf = Persistence.createEntityManagerFactory("PhysicBallsPU");
    }

    public void insertSpace(String spaceName) {
        DbSpace space = new DbSpace();
        space.setName(spaceName);
        DbSpaceJpaController dbspacejpa = new DbSpaceJpaController(emf);
        dbspacejpa.create(space);

    }

    public void insertBalls(ArrayList<Ball> list) {

        DbSpace dbspace = new DbSpace();
        dbspace = getlastIndexOfSpace();

        for (Ball ball : list) {
            DbBall dbBall = new DbBall();
            dbBall.setX(ball.getX());
            dbBall.setY(ball.getY());
            dbBall.setSpeed(ball.getSpeed());
            dbBall.setAccel(ball.getAccel());
            dbBall.setRadius(ball.getRadius());
            dbBall.setAngle(ball.getAngle());
            dbBall.setType(ball.getType().name().charAt(0));
            dbBall.setIdSpace(dbspace);

            DbBallJpaController dbballjpa = new DbBallJpaController(emf);
            dbballjpa.create(dbBall);
        }
    }

    public void insertObstacles(ArrayList<Obstacle> list) {

        DbSpace dbspace = new DbSpace();
        dbspace = getlastIndexOfSpace();

        for (Obstacle obstacle : list) {
            DbObstacle dbobstacle = new DbObstacle();
            dbobstacle.setX(obstacle.getX());
            dbobstacle.setY(obstacle.getY());
            dbobstacle.setWidth(obstacle.getWidth());
            dbobstacle.setHeight(obstacle.getHeight());
            dbobstacle.setIdSpace(dbspace);

            DbObstacleJpaController obstaclejpa = new DbObstacleJpaController(emf);
            obstaclejpa.create(dbobstacle);

        }
    }

    public void insertStopItems(ArrayList<StopItem> list) {

        DbSpace dbspace = new DbSpace();
        dbspace = getlastIndexOfSpace();

        for (StopItem stopItem : list) {
            DbStopitem dbstopitem = new DbStopitem();
            dbstopitem.setX(stopItem.getX());
            dbstopitem.setY(stopItem.getY());
            dbstopitem.setWidth(stopItem.getWidth());
            dbstopitem.setHeight(stopItem.getHeight());
            dbstopitem.setIdSpace(dbspace);

            DbStopitemJpaController dbstopitemjpa = new DbStopitemJpaController(emf);
            dbstopitemjpa.create(dbstopitem);

        }
    }

    public List<Space> selectSpace(String spaceName) {//String spaceName
        List<Space> spaceList = new ArrayList<Space>();

        DbSpaceJpaController dbspacejpa = new DbSpaceJpaController(emf);
        List<DbSpace> list = dbspacejpa.findDbSpaceEntities();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(spaceName)) {
                Space space = new Space();
                space.setName(list.get(i).getName());
                spaceList.add(space);
            }
        }

        return spaceList;
    }

    public List<Ball> selectBalls(String spaceName) {
        List<Ball> ballList = new ArrayList<Ball>();

        DbBallJpaController dbballjpa = new DbBallJpaController(emf);
        List<DbBall> list = dbballjpa.findDbBallEntities();

        for (int i = 0; i < list.size(); i++) {
            //System.out.println(list.get(i).getIdSpace());  
            if (list.get(i).getIdSpace().getName().equals(spaceName)) {
                Ball ball = new Ball();
                ball.setX(list.get(i).getX());
                ball.setY(list.get(i).getY());
                ball.setSpeed((float) list.get(i).getSpeed(), list.get(i).getAngle());
                ball.setAccel(list.get(i).getAccel());
                ball.setRadius(list.get(i).getRadius());
                //ball.setAngle
                ball.setType(list.get(i).getType().toString());

                ballList.add(ball);
            }
        }
        return ballList;

    }

    public List<Obstacle> selectObstacles(String spaceName) {
        List<Obstacle> obstacleList = new ArrayList<Obstacle>();

        DbObstacleJpaController dbobstaclejpa = new DbObstacleJpaController(emf);
        List<DbObstacle> list = dbobstaclejpa.findDbObstacleEntities();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdSpace().getName().equals(spaceName)) {
                Obstacle obstacle = new Obstacle();

                obstacle.setX(list.get(i).getX());
                obstacle.setY(list.get(i).getY());
                obstacle.setWidth(list.get(i).getWidth());
                obstacle.setHeight(list.get(i).getHeight());

                obstacleList.add(obstacle);
            }

        }

        return obstacleList;
    }

    public List<StopItem> selectStopItems(String spaceName) {
        List<StopItem> stopItemList = new ArrayList<StopItem>();

        DbStopitemJpaController dbstopitemjpa = new DbStopitemJpaController(emf);
        List<DbStopitem> list = dbstopitemjpa.findDbStopitemEntities();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdSpace().getName().equals(spaceName)) {
                StopItem stopItem = new StopItem();
                stopItem.setX(list.get(i).getX());
                stopItem.setY(list.get(i).getY());
                stopItem.setWidth(list.get(i).getWidth());
                stopItem.setHeight(list.get(i).getHeight());
//            stopItem.setParent
                stopItemList.add(stopItem);
            }
        }
        return stopItemList;
    }

    public List<String> getSpaceList() {
        List<String> list = null;
        DbSpaceJpaController spacejpa = new DbSpaceJpaController(emf);
        List<DbSpace> dbspacelist = spacejpa.findDbSpaceEntities();
        for (int i = 0; i < dbspacelist.size(); i++) {
            String spaceName = dbspacelist.get(i).getName();
            list.add(spaceName);
        }
        return list;
    }

    public String[] getSpaceList1() {

        DbSpaceJpaController spacejpa = new DbSpaceJpaController(emf);
        List<DbSpace> dbspacelist = spacejpa.findDbSpaceEntities();
        String[] list = new String[spacejpa.getDbSpaceCount()];
        for (int i = 0; i < dbspacelist.size(); i++) {
            list[i] = dbspacelist.get(i).getName();
        }
        return list;
    }

    /**
     * This method return true if there isn't a map in the db with the same name
     *
     * @param spaceName
     * @return
     */
    public boolean checkSpaceName(String spaceName) {
        DbSpaceJpaController spacejpa = new DbSpaceJpaController(emf);
        List<DbSpace> list = spacejpa.findDbSpaceEntities();
        for (DbSpace space : list) {
            space.getName();
            if (space.getName().equals(spaceName)) {
                return false;
            }
        }
        return true;
    }

    private DbSpace getlastIndexOfSpace() {

        DbSpaceJpaController dbspacejpa = new DbSpaceJpaController(emf);
        DbSpace dbspace = dbspacejpa.findDbSpaceEntities().get(dbspacejpa.findDbSpaceEntities().size() - 1);
        return dbspace;
    }
}
