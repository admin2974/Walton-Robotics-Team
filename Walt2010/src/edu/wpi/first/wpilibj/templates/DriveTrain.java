/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author Admin
 */
public class DriveTrain
{
    private SpeedController SpeedController_1, SpeedController_2;
    private Joystick JoyStick_1, JoyStick_2;

    public boolean AfterBurners;
    
    public DriveTrain(SpeedController left_SC, SpeedController right_SC, Joystick left_JS, Joystick right_JS)
    {
        SpeedController_1 = left_SC;
        SpeedController_2 = right_SC;
        JoyStick_1 = left_JS;
        JoyStick_2 = right_JS;
        AfterBurners = false;


    }
    /**
    * This method should be called at least every 0.05 seconds in order to properly update the Drive Train based on Joystick Inputs
    */
    public void update()
    {
         if (JoyStick_1 == null || JoyStick_2 == null)
            throw new NullPointerException("Null motor provided");
         if(!AfterBurners)
         {
            ABS(SpeedController_1.get(),SpeedController_2.get(),JoyStick_1.getY()/2,JoyStick_2.getY()/2);
         }
         else
         {
            ABS(SpeedController_1.get(),SpeedController_2.get(),JoyStick_1.getY()*2,JoyStick_2.getY()*2);//squares the power when in AfterBurner mode
         }
    }
    public void drive(double leftSpeed, double rightSpeed)
    {
         if (SpeedController_1 == null || SpeedController_2 == null)
            throw new NullPointerException("Null motor provided");
        leftSpeed = limit(leftSpeed);
        rightSpeed = limit(rightSpeed);
        SpeedController_1.set(leftSpeed);
        SpeedController_2.set(rightSpeed);

    }

    private double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }

    private void ABS(double speed1,double speed2,double JS1,double JS2)
    {
        double leftSpeed,rightSpeed;
        if(speed1-JS1>1.0||speed1-JS1<-1.0)
        {
            leftSpeed = JS1/2;
        }
        else
        {
            leftSpeed = JS1;
        }
        if(speed2-JS2>1.0||speed2-JS2<-1.0)
        {
            rightSpeed = JS2/2;
        }
        else
        {
            rightSpeed = JS2;
        }
        
        drive(leftSpeed,rightSpeed);
    }

}
