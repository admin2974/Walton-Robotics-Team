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
public class RobotMain extends IterativeRobot {

    VirtualField field;
    DriveTrain robot_drive;

    Jaguar jag1, jag2;
    SpecialJoystick JS1, JS2;
     /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        jag1 = new Jaguar(1);
        jag2 = new Jaguar(2);
        JS1 = new SpecialJoystick(this,1);
        JS2 = new SpecialJoystick(this,2);
        robot_drive = new DriveTrain(jag1,jag2,JS1,JS2);

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
    public void teleopPeriodic()
    {
        while(true)//Infinate loop that checks the state of the robot and driver station every .02 seconds
        {
            robot_drive.update();
            JS1.check();
            JS2.check();
            Timer.delay(kDefaultPeriod);
        }
    }
    
}
