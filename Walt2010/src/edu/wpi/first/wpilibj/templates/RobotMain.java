/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot
{

    /**
     * TODO: Be conscious of attribute scope.  It is convenient when developing
     * to scope everything in a friendly and accessible fashion, but it can lead
     * to concurrency issues (among other ill effects) if not managed.  If
     * multiple threads will be accessing attributes (will they?) then you must
     * ensure they do so one at a time.  A better strategy might be to scope
     * private by default:  this will force you to think about how bits of data
     * are used, which in turn helps concretize the system's structure by
     * forcing you to think about where info is stored and how it might (or
     * sometimes must!) flow through your code.  One final thing to research:
     * is there any advantage in a J2ME environment to allowing direct access
     * to attributes (versus accessor methods) in terms of execution speed or
     * active memory overhead?
     */
    VirtualField field;
    /**
     * TODO: This must have concurrency control under the current impl!!
     * DriveTrain and SpecialJoystick both use this, and not very well! If
     * SpecialJoystick is to set the afterburner trigger (or any other state
     * modifier attribute's value) in DriveTrain, consider the different threads
     * involved in brokering signals from the physical joystick & mechanisms on
     * the robot - unless I'm very much mistaken you're going to need to
     * synchronize not only the afterburner set operation but also the reading
     * of that value within DriveTrain itself.
     */
    DriveTrain robot_drive;
    Jaguar leftJag, rightJag;
    SpecialJoystick leftJoystick, rightJoystick;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        leftJag = new Jaguar(1);
        rightJag = new Jaguar(2);
        leftJoystick = new SpecialJoystick(this, 1);
        rightJoystick = new SpecialJoystick(this, 2);
        robot_drive = new DriveTrain(leftJag, rightJag, leftJoystick,
                rightJoystick);

        field = new VirtualField();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //Infinite loop that checks the state of the robot and driver station
        // every .02 seconds
        while (true) {
            robot_drive.update();
            leftJoystick.check();
            rightJoystick.check();
            Timer.delay(kDefaultPeriod);
        }
    }
}
