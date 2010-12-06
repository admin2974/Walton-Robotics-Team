package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Watchdog;

/**
 *
 * @author Scott Adams
 */
public class AutoTargeting {

    RobotMain robot;
    private double degrees;
    private boolean enabled;
    private boolean targeting;
    private boolean m_run = true;
    private Thread m_task;
    int TimeoutCounter;

    public AutoTargeting(RobotMain r) {
        robot = r;
        degrees = 0;
        enabled = true;
        targeting = false;
        TimeoutCounter = 0;
        initTargeting();
    }

    private void initTargeting() {
        m_task = new TargetThread(this);
        m_task.start();
    }

    public void setEnabled(boolean enable) {
        enabled = enable;
    }

    public void stopTargeting() {
        targeting = false;
        robot.robot_drive.autoMode = false;
    }

    public boolean isTargeting() {
        return targeting;
    }

    public void autoTarget() {
        targeting = true;
        robot.robot_drive.autoMode = true;
    }

    public boolean gyroTarget() {
        double angle = robot.g.getAngle();

        if (angle < 3 && angle > -3) {
            System.out.println("stoping based on gyro angle of:" + angle);
            robot.robot_drive.drive(0, 0);
            return true;
        } else if ((angle > 180 && angle < 360) || (angle > -180 && angle < 0)) {
            System.out.println("turning based on gyro angle of" + angle);
            robot.robot_drive.drive(-.35, .35);
            TimeoutCounter++;
        } else if (angle < 180 && angle > 0 || (angle < -180 && angle > -360)) {
            System.out.println("turning based on gyro angle of(2)" + angle);
            robot.robot_drive.drive(.35, -.35);
            TimeoutCounter++;
        }
        if (TimeoutCounter > 200)//200 * .02 = 4 sec timeout for gyro target
        {
            targeting = false;
            robot.robot_drive.autoMode = false;
            TimeoutCounter = 0;
            System.out.println("Autotarget timeout in gyroTargeting");
        }
        return false;
    }

    public boolean cameraTarget() {
        degrees = robot.cam.calculateDegrees();
        System.out.println("Degrees" + degrees);

        TimeoutCounter++;
        if (TimeoutCounter > 40)//100 * .02 = 2 sec timeout for gyro target
        {
            targeting = false;
            robot.robot_drive.autoMode = false;
            TimeoutCounter = 0;
            System.out.println("Autotarget timeout in cameraTargeting");
        }


        if (degrees < 2 && degrees > -2) {
            Watchdog.getInstance().feed();
            robot.robot_drive.drive(0, 0);
            return true;
        } else if (degrees < 0) {
            robot.robot_drive.drive(.25, -.25);
            Watchdog.getInstance().feed();
        } else {
            robot.robot_drive.drive(-.25, .25);
            Watchdog.getInstance().feed();
        }
        return false;
    }

    /**
     * Internal thread.
     *
     * Task which checks the shooter and determines an apropriate action based on shooter's internal counter
     *
     *
     * Do not call this function directly.
     */
    private class TargetThread extends Thread {

        AutoTargeting autoTargeting;
        int c;

        TargetThread(AutoTargeting autoTarget) {
            autoTargeting = autoTarget;
            c = 0;

        }

        public void run() {
            while (m_run) {
                if (enabled) {
                    if (targeting) {
                        if (c == 0) {
                            robot.robot_drive.finalDrive(0, 0);
                        }
                        if (c < 10) {
                            if (gyroTarget()) {
                                c++;
                            }
                            if (c == 10) {
                                TimeoutCounter = 0;
                            }
                            System.out.println("Timeout counter" + TimeoutCounter);
                        } else if (c < 15) {
                            if (cameraTarget()) {
                                c++;
                            }
                            System.out.println("Timeout counter" + TimeoutCounter);
                        } else {
                            TimeoutCounter = 0;
                            robot.shooter.fire(1);
                            targeting = false;
                            robot.robot_drive.autoMode = false;
                            c = 0;
                        }
                    } else {
                        TimeoutCounter = 0;
                        robot.robot_drive.autoMode = false;
                        c = 0;
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }

        }
    }
}
