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
    int loopTime;
    public boolean Overdrive_L, Overdrive_R;

    
    public DriveTrain(SpeedController right_SC, SpeedController left_SC, Joystick left_JS, Joystick right_JS)
    {
        SpeedController_1 = left_SC;
        SpeedController_2 = right_SC;
        JoyStick_1 = left_JS;
        JoyStick_2 = right_JS;
        Overdrive_L = false;
        Overdrive_R = false;
        loopTime = 0;

    }
    /**
    * This method should be called at least every 0.05 seconds in order to properly update the Drive Train based on Joystick Inputs
    */
    public void update()
    {
        double speedRight=0,speedLeft=0;
             if(!Overdrive_L)
             {
                speedLeft=JoyStick_1.getY()/2;
             }
             else
             {
                speedLeft=JoyStick_1.getY()*2;
             }
             if(!Overdrive_R)
             {
                 speedRight=JoyStick_2.getY()/2;
             }
             else
             {
                 speedRight=JoyStick_2.getY()*2;
             }
             drive(speedLeft,speedRight);
        

        
    }
    public void drive(double leftSpeed, double rightSpeed)
    {
         if (SpeedController_1 == null || SpeedController_2 == null)
            throw new NullPointerException("Null motor provided");
        leftSpeed = limit(leftSpeed)*-1;
        rightSpeed = limit(rightSpeed)*1;
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

   
}
