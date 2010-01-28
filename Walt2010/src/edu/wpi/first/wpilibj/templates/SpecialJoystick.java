package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This provides a mechanism for delivering customized functionality based on
 * joystick inputs.  It relies (read: will need to rely) upon well-defined
 * contracts between this class and (ideally) the active <code>RobotMain</code>
 * instance (i.e., the robot out there relying upon this guy to deliver the
 * goods for him!)
 *
 * @author Admin
 */
public class SpecialJoystick extends Joystick
{

    /**
     * TODO:  Consider scope - this ought be private by any metric, neh?
     */
    RobotMain robot;

    public SpecialJoystick(RobotMain mainRobot, int channel) {
        super(channel);
        robot = mainRobot;
    }

    /**
     * This method checks the current state of a joystick and controls the robot accordingly.
     */
    public void check() {
        //TODO:  Afterburners access must be synchronized!
        //TODO:  Consider delivering this functionality by defining a method on
        // RobotMain, perhaps "public void setAfterburners(boolean val)", which
        // would let you hide the implementation details from this class. The
        // joystick doesn't (and shouldn't) know that the robot uses a
        // DriveTrain which has a boolean afterBurners attribute.  This class
        // should not have to change if the way afterburners are implemented
        // changes, and a well-defined contract between the joystick and the
        // robot is the best way to accomplish this here.
        if (this.getTrigger()) {
            robot.robot_drive.afterBurners = true;
        } else {
            robot.robot_drive.afterBurners = false;
        }

    }
}
