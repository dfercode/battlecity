package falcon;

import robocode.*;

import java.awt.*;
import java.util.Random;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class falcon extends AdvancedRobot {
    int tankLength=50;
    double pi=3.141592653;
    double dist =30;
    @Override
    public void onDeath(DeathEvent event) {
        super.onDeath(event);
    }
    public  boolean avoidHitWall(double distace,double xcur,double ycur, double radians){
        System.out.println("checking ");
        double x=Math.cos(radians)*distace+xcur;
        double y=Math.sin(radians)*distace+ycur;
        System.out.println("X:"+x+"--Y:"+y);

        double wideGround=getBattleFieldWidth();
        double heightLength=getBattleFieldHeight();
        System.out.println("wide:"+wideGround+"--height:"+heightLength);
        boolean willHit=false;
        if (radians>pi/2&&radians<=pi//2
                &&(x>wideGround-tankLength||y<tankLength+0)){willHit=true;}
        if (radians<pi/2&&radians>=0//1
                &&(x>wideGround-tankLength||y>heightLength-tankLength)){willHit=true;}
        if (radians>=pi*3/2//4
                &&(x<tankLength+0||y>heightLength-tankLength)){willHit=true;}
        if (radians>=pi&&radians<pi*3/2//3
                &&(x<tankLength||y<tankLength)){willHit=true;}
        System.out.println(radians);
        if (willHit){
            System.out.println("willHit------------");
            return false;

        }
        System.out.println("unhit");
        return true;
    }
    @Override
    public void run() {
        setBodyColor(new Color(99, 66, 99));
        setGunColor(new Color(99, 66, 135));
        setRadarColor(new Color(210, 200, 255));
        setBulletColor(new Color(255, 255, 100));
        setScanColor(new Color(255, 200, 25));
        //
        double headDistance =5000;
        setMaxVelocity(6);
        System.out.println("start game!");

        while (true) {
            turnRadarLeft(500);
            headDistance=20;
            if (!avoidHitWall(headDistance,getX(),getY(),getHeadingRadians())){
                //set
                setMaxVelocity(8);
                setMaxTurnRate(8);
                setTurnRight(77);
                setBack(100);
                //waitFor(new TurnCompleteCondition(this));
                continue;
            }
            setMaxVelocity(8);
            System.out.println("go ahead"+headDistance);
            setAhead(headDistance);


            //execute();
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        setTurnRight(normalRelativeAngleDegrees(90 - (getHeading() - event.getHeading())));

        ahead(dist);
        dist *= -1;
        scan();
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {

        double turnGunAmt = normalRelativeAngleDegrees(event.getBearing() + getHeading() - getGunHeading());

        setTurnGunRight(turnGunAmt);
        setFire(2.5);
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        setBack(100);

        System.out.println("hit wall");

    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        double turnGunAmt = normalRelativeAngleDegrees(event.getBearing() + getHeading() - getGunHeading());
        setTurnGunRight(turnGunAmt);
        if (event.getDistance() < 50 && getEnergy() > 50) {

            setFire(3);
            //setFire(1);
        } // otherwise, fire 1.
        else if (event.getDistance() <150 ){
            setFire(2.0);
            //setFire(0.5);
        }
        else {

            setFire(1);
        }
        // Call scan again, before we turn the gun
        scan();
    }

    private void shotHim(double distance, double bearing) {
        if (distance < 10) {
            out.println("fire Rules.MAX_BULLET_POWER");
            setFire(Rules.MAX_BULLET_POWER);
            return;
        }
        if (distance < 100 && Math.abs(bearing) < 30) {
            out.println("fire Rules.MAX_BULLET_POWER");
            setFire(Rules.MAX_BULLET_POWER);
            return;
        }
        if (distance < 200 && Math.abs(bearing) < 10) {
            out.println("fire Rules.MIN_BULLET_POWER");
            this.setFire(Rules.MIN_BULLET_POWER);
            return;
        }

    }
}