package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot {

    WaltJoystick leftJS, rightJS;
    DriveTrain robot_drive;
    Jaguar motorController1, motorController2;
    Shooter shooter;
    Compressor compressor;
    Relay ballControl;
    Gyro g;
    Camera cam;
    AutoTargeting autoTarget;
    Encoder enc, enc2;
    double startTime = 0;
    private int autonCounter;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        Watchdog.getInstance().setEnabled(true);
        Watchdog.getInstance().setExpiration(2);
        autonCounter = 0;

        motorController1 = new Jaguar(1);
        motorController2 = new Jaguar(2);

        leftJS = new WaltJoystick(this, 1, true);//true == left Joystick
        rightJS = new WaltJoystick(this, 2, false);//false == right Joystick

        compressor = new Compressor(1, 1);
        compressor.start();
//
//        enc = new Encoder(2,3);
//        enc2 = new Encoder(4,5);
//        enc.start();
//        enc2.start();
//        enc.setDistancePerPulse(8);
//        enc2.setDistancePerPulse(8);

        g = new Gyro(1);
        g.reset();
        shooter = new Shooter();
        cam = new Camera();
        ballControl = new Relay(2);

        robot_drive = new DriveTrain(motorController1, motorController2, rightJS, leftJS);
        autoTarget = new AutoTargeting(this);
    }

    public void disabledInit() {
        System.out.println("Disabeling robot and all threads.");
        autoTarget.setEnabled(false);
        shooter.stop();
    }

    public void disabledPeriodic() {
    }

    public void autonomousInit() {
        shooter.start();
        autoTarget.setEnabled(true);
        shooter.pressureToReturn();
        ballControl.set(Relay.Value.kReverse);
        startTime = System.currentTimeMillis();
        autonCounter = 0;
//        robot_drive.finalDrive(-.5,-.5);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
//        //Testing needed
        Watchdog.getInstance().feed();

//        while (true) {
//            Watchdog.getInstance().feed();
//            shooter.fire(1);
//        }

        if (autonCounter > 15 && autonCounter < 50) {
            //System.out.println("set latch");
            shooter.latch(true);
        } else if (autonCounter > 50 && autonCounter < 80) {
            robot_drive.finalDrive(0, 0);
        } else if (autonCounter > 350 && autonCounter < 380) {
            shooter.releasePressureToReturn();
            shooter.pressureToFire();
        } else if (autonCounter < 400 && autonCounter > 350) {
            shooter.latch(false);
        } else if (autonCounter < 420 && autonCounter > 350) {
            shooter.releasePressureToShooter();
            shooter.pressureToReturn();
        } else if (autonCounter < 425 && autonCounter > 350) {
            shooter.latch(true);
        }

        autonCounter++;

        if (autonCounter == 1) {
            if (!compressor.getPressureSwitchValue()) {
                robot_drive.finalDrive(0, 0);
            } else {
                robot_drive.finalDrive(.5, .5);
                autonCounter++;
            }
        } else if (autonCounter == 2 || autonCounter == 3) {
            //if auton counter is 2 then the robot is moving forward at a
            // speed of .5,.5
            //if auton coutner is 3 then the robot has started to autoshoot
            // but not yet finished
            //if one of the encoders is at 30 and the autonCounter is equal
            // to 2(the autoTarget has not been called before)
            if (autonCounter == 2) {
//            if ((enc.getDistance() == 30 || enc2.getDistance() == 30)
//                    && autonCounter == 2) {
                autoTarget.autoTarget();
                autonCounter++;
            }
            if (autonCounter == 3) {
                //if the robot has stoped targeting then increment the autonCounter
                if (!autoTarget.isTargeting()) {
                    autonCounter++;
                }
            }
        }

        //if (enc.getDistance() == 60 || enc.getDistance() == 60) {
        //}

        Timer.delay(kDefaultPeriod);
    }

    /**
     *
     * @return a double representing the number of seconds since the robot began autonomous
     */
    public double getTime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public void teleopInit() {
//        enc.reset();
//        enc2.reset();
//        System.out.println("Starting teleopPeriodic()...");
//        ballControl.set(Relay.Value.kReverse);
        g.reset();
        shooter.start();
        autoTarget.setEnabled(true);
    }

    /**
     * This function is called periodically (100 times per second) during operator control
     */
    public void teleopPeriodic() {
        Watchdog.getInstance().feed();
        if (g.getAngle() > 360 || g.getAngle() < -360) {
            g.reset();
        }
    }

    /**
     * This Function is called as fast as possible when in Teleop Mode
     */
    public void teleopContinuous() {
        robot_drive.update();
        leftJS.check();
        rightJS.check();

    }
}
