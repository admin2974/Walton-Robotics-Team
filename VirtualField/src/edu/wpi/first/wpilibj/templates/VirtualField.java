/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Accelerometer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;

    /**
     * Keeps track of the robot's location in real time updating itself every 0.01 seconds.
     * Default (x, y) and velocity values are zero.
     * TODO: Add a constructor that takes a number 1-3 that places the robot on 1 of 3 preset places on the field
     * @author Sam Meyer
     */
public class VirtualField {
    /**
     * Stores the current velocity of the robot as a vector
     */
    private Vector velocity;
    /**
     * The poller runs an infinite while loop with a delay of 0.01 seconds.
     * Each time the loop runs, it updates the vector increase rate stored in the Vector class.
     * It also updates the angle at which the robot is pointed.
     */
    private Poller poller;
    private Accelerometer accel;
    private Gyro gyro;
    /**
     * The robot's X position on the field
     */
    private double X;
    /**
     * The robot's Y position on the field
     */
    private double Y;

    /**
     * Sets default values (x, y) = (0, 0) and velocity is zero
     */
    public VirtualField()
    {
        velocity = new Vector(0, 0);
        X = Y = 0;
        init();
    }
    /**
     * Takes the initial X and Y coordinates and assumes a velocity of zero
     * @param initial_X initial X coordinate
     * @param initial_Y initial Y coordinate
     */
    public VirtualField(double initial_X, double initial_Y)
    {
        X = initial_X;
        Y = initial_Y;
        velocity = new Vector(0, 0);
        init();
    }
    /**
     * Takes initial X and Y coordinates and velocity as a vector
     * @param initial_X the initial X coordinate
     * @param initial_Y the initial Y coordinate
     * @param Velocity the initial Velocity as a Vector
     */
    public VirtualField(double initial_X, double initial_Y, Vector Velocity)
    {
        X = initial_X;
        Y = initial_Y;
        velocity = Velocity;
        init();
    }
    /**
     * Initializes the acclerometer and gyroscope and starts the poller
     */
    private void init()
    {
///////////////////////////////////////
        accel = new Accelerometer(0);    // channel/slot number needed
        gyro =  new Gyro(2);             // channel number needed
///////////////////////////////////////
        poller = new Poller();
        poller.start();
    }
    /**
     * returns the current X coordinate of the robot
     * @return the X coordinate of the robot
     */
    public double getX()
    {
        return X;
    }
    /**
     * returns the current Y coordinate of the robot
     * @return the Y coordinate of the robot
     */
    public double getY()
    {
        return Y;
    }
    /**
     * NOT YET IMPLEMENTED!!! This method will return the distance to the target as a vector
     * @return the vector to the target (angle associated with the vector may be negative)
     */
    public Vector getVectorToTarget()
    {
        return new Vector(0, 0);
    }
    /**
     * Polls every 0.01 seconds for change in acceleration and angle
     */
    private class Poller extends Thread
    {
        private final double ROBOT_MASS = 1.0;  // mass of robot in kg (idk if this is really necessary)

        /**
         * Runs the polling process and changes the increaseRate in the Vector class
         */
        public void run()
        {
            while(true)
            {
                double g = accel.getAcceleration();
                double angle = gyro.getAngle();
                velocity.setIncreaseRate(g*9.8, angle);

                Timer.delay(0.01);
            }
        }
    }

}
