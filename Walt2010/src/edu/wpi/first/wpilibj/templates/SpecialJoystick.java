/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Admin
 */
public class SpecialJoystick extends Joystick
{
    RobotMain Robot;
    boolean left_right;
    boolean ballDisabled;
    public SpecialJoystick(RobotMain mainRobot,int channel, boolean left_right)
    {
        super(channel);
        this.left_right = left_right;
        ballDisabled = false;

        Robot = mainRobot;
    }
    /**
     * This method checks the current state of a joystick and controls the robot accordingly.
     */
    public void check()
    {

        if((this.getY()>.25 && this.getY()>.25) || !ballDisabled)
        {
            Robot.ballControl.set(Relay.Value.kReverse);
        }
        else if(!ballDisabled)
        {
            Robot.ballControl.set(Relay.Value.kForward);
        }

        if(left_right)
        {
            if(this.getTrigger())
            {
                Robot.robot_drive.Overdrive_L=true;
            }
            else
            {
                Robot.robot_drive.Overdrive_L=false;
            }
        }
        else
        {
            if(this.getTrigger())
            {
                Robot.robot_drive.Overdrive_R=true;
            }
            else
            {
                Robot.robot_drive.Overdrive_R=false;
            }
        }
        if(this.getRawButton(3))
        {
            Robot.shooter.fireFull=true;
            Robot.shoot = true;

        }
            else if(this.getRawButton(2))
        {
            Robot.shooter.fireFull=false;
            Robot.shoot = true;
        }

            
            if(this.getRawButton(5))
        {
                ballDisabled = true;
            Robot.ballControl.set(Relay.Value.kOff);
        }
        if(this.getRawButton(4))
        {
                ballDisabled = false;

        }
        if(this.getRawButton(10))
        {
            Robot.reload = true;
        }
        if(this.getRawButton(9))
        {
            Robot.shooter.latch(false);
        }



    }


}
