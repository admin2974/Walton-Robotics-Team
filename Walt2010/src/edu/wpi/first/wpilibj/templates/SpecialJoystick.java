/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Admin
 */
public class SpecialJoystick extends Joystick
{
    RobotMain Robot;
    public SpecialJoystick(RobotMain mainRobot,int channel)
    {
        super(channel);
        Robot = mainRobot;
    }
    /**
     * This method checks the current state of a joystick and controls the robot accordingly.
     */
    public void check()
    {
        if(this.getTrigger())
        {
            Robot.robot_drive.AfterBurners=true;
        }
        else
        {
            Robot.robot_drive.AfterBurners=false;
        }
            
    }


}
