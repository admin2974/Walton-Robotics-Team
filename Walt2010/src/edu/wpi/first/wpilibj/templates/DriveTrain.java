/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
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
            drive(JoyStick_1.getY()/2,JoyStick_2.getY()/2);
         }
         else
         {
            drive(JoyStick_1.getY()*JoyStick_2.getY(),JoyStick_2.getY()*JoyStick_2.getY());//squares the power when in AfterBurner mode
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

}
