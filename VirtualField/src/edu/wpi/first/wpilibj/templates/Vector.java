/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Timer;

/**
 * Represents a vector
 * @author Sam Meyer
 */
public class Vector {
    private double X;
    private double Y;
    private double Angle;
    private X_Increaser xRate;
    private Y_Increaser yRate;

    /**
     * Takes vector quantity with angle and breaks the vector into it's components
     * @param Value the vector quantity
     * @param angle the vector angle in degrees
     */
    public Vector(double Value, double angle)       // in m/s and degrees
    {
        Angle = angle;
        angle = Math.toRadians(angle);  // convert to Radians for the math functions
        X = Value * Math.cos(angle);
        Y = Value * Math.sin(angle);
        init();
    }
    /**
     * Takes input as vector components and an angle
     * @param Value_X the vector X component
     * @param Value_Y the vector Y component
     * @param angle   the vector angle
     */
    public Vector(double Value_X, double Value_Y, double angle) // in m/s and degrees
    {
        X = Value_X;
        Y = Value_Y;
        Angle = angle;
        init();
    }
    /**
     * Sets the X and Y change rate to zero
     */
    private void init()
    {
        xRate = new X_Increaser(0);
        yRate = new Y_Increaser(0);
    }
    /**
     * returns the X component of the vector
     * @return the X component of the vector
     */
    public double getX()
    {
        return X;
    }
    /**
     * returns the Y component of the vector
     * @return the Y component of the vector
     */
    public double getY()
    {
        return Y;
    }
    /**
     * Returns the vector quantity
     * @return the vector quantity
     */
    public double getVectorQuantity()
    {
        return Math.sqrt(X*X + Y*Y);
    }
    /**
     * returns the angle
     * @return the angle
     */
    public double getAngle()
    {
        return Angle;
    }
    /**
     * sets the increase rate in the X direction
     * @param Xrate the X increase rate
     */
    public void setXIncreaseRate(double Xrate)
    {
        xRate.changeRate(Xrate);
    }
    /**
     * sets the increase rate in the Y direction
     * @param Yrate the Y increase rate
     */
    public void setYIncreaseRate(double Yrate)
    {
        yRate.changeRate(Yrate);
    }
    /**
     * Accepts input as a vector quantity and an angle.
     * This method uses the angle to break the vector quantity into its components.
     * @param rate the rate of increase as a double
     * @param angle the angle of increase
     */
    public void setIncreaseRate(double rate, double angle)
    {
        angle = Math.toRadians(angle);
        xRate.changeRate(rate*Math.cos(angle));
        yRate.changeRate(rate*Math.sin(angle));

    }
    /**
     * Sets the X component of the vector
     * @param x the new X component
     */
    public void setX(double x)
    {
        X = x;
    }
    /**
     * Sets the Y component of the vector
     * @param y the new Y component
     */
    public void setY(double y)
    {
        Y = y;
    }
    /**
     * The X_Increaser subclass increases the X component of the vector every 0.01 seconds
     */
    private class X_Increaser extends Thread
    {
        private double increaseRate;
        /**
         * Constructs a new X_Increaser
         * @param IncreaseValue the X direction increase rate
         */
        public X_Increaser(double IncreaseValue)        // in m/s^2
        {
            increaseRate = IncreaseValue;
        }
        /**
         * Starts the process
         */
        public void run()
        {
            while(true)
            {
                X += increaseRate * 0.01;
                Timer.delay(0.01);
            }
        }
        /**
         * Changes the X direction increase rate
         * @param newRate the new X increase rate
         */
        public void changeRate(double newRate)
        {
            increaseRate = newRate;
        }
    }
    /**
     * The Y_Increaser subclass increases the Y component of the vector every 0.01 seconds
     */
    private class Y_Increaser extends Thread
    {
        private double increaseRate;
        /**
         * Constructs a new Y_Increaser
         * @param IncreaseValue the Y direction increase rate
         */
        public Y_Increaser(double IncreaseValue)        // in m/s^2
        {
            increaseRate = IncreaseValue;
        }
        /**
         * Starts the process
         */
        public void run()
        {
            while(true)
            {
                Y += increaseRate * 0.01;
                Timer.delay(0.01);
            }
        }
        /**
         * Changes the Y direction increase rate
         * @param newRate the new Y increase rate
         */
        public void changeRate(double newRate)
        {
            increaseRate = newRate;
        }
    }
}
