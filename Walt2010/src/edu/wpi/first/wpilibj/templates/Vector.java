/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author Developer
 */
public class Vector 
{
    private Gyro gyro;
    private ADXL345_I2C accel;
    private double angle;
    private double previousAngle;
    private double globalCounter;
    
    public Vector(Gyro g)
    {
        angle=0;
        gyro = g;
//        accel = a;
        previousAngle = 0;
        angle = 0;
        globalCounter = 0;
    }
    
    public void update()
    {
        globalCounter++;
        updateAngle();
        if(globalCounter>5)
        {
            globalCounter = 0;
        }
            
    }
    public void updateAngle()
    {
        
        if(globalCounter>5)
        {
        angle = gyro.getAngle();
        
        previousAngle = angle; 
        }
        
        

    }
}
