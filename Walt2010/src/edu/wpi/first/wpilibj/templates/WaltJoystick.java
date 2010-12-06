
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Scott Adams
 */
public class WaltJoystick extends Joystick
{
    RobotMain Robot;
    boolean left ;
    boolean ballDisabled;

    /**
     *This class is used to place all the controls of the robot in one object.
     * it extends the standard Joystick object and adds a method that will update the mechanisms on the robot based on the
     * inputs given by the user
     * @param mainRobot the refrence to the robot object. This allows the joystick to control the robot directly
     * @param channel the channel the joystick is plugged into on the dashboard
     * @param left if this is true then the joystick will conider itself to be the left joystick. If false it will act like its the right joystick.
     */
    public WaltJoystick(RobotMain mainRobot,int channel, boolean left)
    {
        super(channel);
        this.left = left;//if this is true this is the left joystick if false it is right
        ballDisabled = false;
        Robot = mainRobot;
    }
    /**
     * This method checks the current state of a joystick and controls the robot accordingly.
     */
    public void check()
    {

        //if the 6 button is held down turn the ball roller on, otherwise check if the 7 button is held down
        //if so turn the ball roller off, if both are pressed down the roller will default to on
        if(this.getRawButton(6))
        {
            ballDisabled = false;
            Robot.ballControl.set(Relay.Value.kReverse);
        }
        else if(this.getRawButton(7))
        {
            ballDisabled = true;
            Robot.ballControl.set(Relay.Value.kOff);
        }


        //checks if this is the left joystick, if so this joystick object will control the Left overdrive
        if(left)
        {
            if(this.getTrigger())
            {
                Robot.robot_drive.Overdrive_L=true;
//                Robot.autoTarget.stopTargeting();
            }
            else
            {
                Robot.robot_drive.Overdrive_L=false;
            }
        }
        //if the joystick is not the left joystick the code assumes its the right joystick and accordingly controls the Right Overdrive
        else
        {
            if(this.getTrigger())
            {
                Robot.robot_drive.Overdrive_R=true;
//                Robot.autoTarget.stopTargeting();
            }
            else
            {
                Robot.robot_drive.Overdrive_R=false;
            }
        }



        //if this object is the left joystick make the joystick control the autoshoot.
        if(left)
        {
             //button 3 fires the shooter at maximum power
            if(this.getRawButton(3))
            {
                Robot.shooter.setFFM(true);
                Robot.shooter.fire(1.0);
                Robot.autoTarget.stopTargeting();
            }
            //button 5 fires the shooter at slightly less than maximum power.
            //This should be used to conserve air when maximum power is not neccisarily needed
            else if(this.getRawButton(5))
            {
                Robot.shooter.setFFM(true);
                Robot.shooter.fire(.75);
                Robot.autoTarget.stopTargeting();
            }
            //button 2 fires at half power with only one cylinder
            //this should be used to pass the ball
            else if(this.getRawButton(2))
            {
                Robot.shooter.setFFM(false);
                Robot.shooter.fire(.5);
                Robot.autoTarget.stopTargeting();
            }
            //button 4 fires at 1/4 power with only one cylinder
            //this should be used to bump the ball into the goal
            else if(this.getRawButton(4))
            {
                Robot.shooter.setFFM(false);
                Robot.shooter.fire(.25);
                Robot.autoTarget.stopTargeting();
            }
            //comented out this code for testing
//            if(this.getRawButton(3))
//            {
//                Robot.autoTarget.autoTarget();
//            }
//            else if(this.getRawButton(2))
//            {
//                Robot.autoTarget.stopTargeting();
//            }
        }
        //if the joystick is not left(the right joystick) the joystick controls the manual shooting of the robot's shooter
        else
        {
            //button 3 fires the shooter at maximum power
            if(this.getRawButton(3))
            {
                Robot.shooter.setFFM(true);
                Robot.shooter.fire(1.0);
                Robot.autoTarget.stopTargeting();
            }
            //button 5 fires the shooter at slightly less than maximum power.
            //This should be used to conserve air when maximum power is not neccisarily needed
            else if(this.getRawButton(5))
            {
                Robot.shooter.setFFM(true);
                Robot.shooter.fire(.75);
                Robot.autoTarget.stopTargeting();
            }
            //button 2 fires at half power with only one cylinder
            //this should be used to pass the ball
            else if(this.getRawButton(2))
            {
                Robot.shooter.setFFM(false);
                Robot.shooter.fire(.5);
                Robot.autoTarget.stopTargeting();
            }
            //button 4 fires at 1/4 power with only one cylinder
            //this should be used to bump the ball into the goal
            else if(this.getRawButton(4))
            {
                Robot.shooter.setFFM(false);
                Robot.shooter.fire(.25);
                Robot.autoTarget.stopTargeting();
            }
            
        }
            

        //if button 10 is held down reload the shooter
        if(this.getRawButton(10))
        {
            Robot.shooter.reload();
        }
        //if button 10 was not held down and button 9 is the shooter releases all pressure to the shooting mechanism
        else if(this.getRawButton(9))
        {
            Robot.shooter.unload();
        }
   }
}
