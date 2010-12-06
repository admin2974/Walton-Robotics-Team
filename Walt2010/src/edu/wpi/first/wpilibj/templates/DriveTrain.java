package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author Scott Adams, Tyler Durkota
 */
public class DriveTrain {

    /**
     * SpeedControler is the superclass of the Jaguar, and Victor motor controlers
     * This allows the DriveTrain object to be used with any type of speedControler
     */
    private SpeedController SpeedController_1, SpeedController_2;
    /**
     * The joysticks the update method of the DriveTrain uses to determine the speed of the robot.
     */
    private Joystick JoyStick_1, JoyStick_2;
    /**
     * previous value of speed for left and right
     * used to limit the speed change of the wheels. which reduces the number of chain slips/failures
     */
    double prevLeft, prevRight;
    /**
     * Controls the Overdrive for the left and right side of the robot.
     * Overdrive multiplies the speed of the robot by 4 on that side
     */
    public boolean Overdrive_L, Overdrive_R;
    /**
     * autoMode determines if the driver has control of the robot.
     */
    public boolean autoMode;

    /**
     * This object represents the components that make up the drive train of the robot.
     * Also it allows the user and programmer to control the motors that make up the DriveTrain.
     * @param right_SC the speed controller(Jaguar, Victor) that is on the RIGHT side of the robot
     * @param left_SC the speed controller(Jaguar, Victor) that is on the LEFT side of the robot
     * @param right_JS the joystick that controls the right side of the robot
     * @param left_JS the joystick that controls the left side of the robot
     */
    public DriveTrain(SpeedController right_SC, SpeedController left_SC, Joystick right_JS, Joystick left_JS) {
        SpeedController_1 = left_SC;
        SpeedController_2 = right_SC;
        JoyStick_1 = left_JS;
        JoyStick_2 = right_JS;
        Overdrive_L = false;
        Overdrive_R = false;
        prevLeft = 0;
        prevRight = 0;
        autoMode = false;
    }

    /**
     * This method should be called at least every 0.05 seconds in order to properly update the Drive Train based on Joystick Inputs
     */
    public void update() {
        /*if auto mode is enabled/true ths method does nothing, 
        otherwise it would overide any manual entered commands with the Joysick vaules.
         *WARNING USER WILL LOSE CONTROL OF ROBOT UNTIL autoMode IS DISABLED(SET TO FALSE)
         */
        if (!autoMode) {
            double speedRight = 0, speedLeft = 0;

            /* the value of Overdrive_L should be changed by a modified version of the Joystick class
             * this is to avoid breaking up the controls of the robot into multiple objects.
             */
            if (!Overdrive_L) {
                speedLeft = JoyStick_1.getY() / 2;
            } else {
                speedLeft = JoyStick_1.getY() * 2;
            }

            /* the value of Overdrive_R should be changed by a modified version of the Joystick class
             * this is to avoid breaking up the controls of the robot into multiple objects.
             */
            if (!Overdrive_R) {
                speedRight = JoyStick_2.getY() / 2;
            } else {
                speedRight = JoyStick_2.getY() * 2;
            }

            //calls the drive method with the modified values of the left and right speeds read from the joystick
            drive(speedLeft, speedRight);
        }
    }

    /**
     * This method is used to manual change the values of the left and right speed controllers on the robots drivetrain
     * @param leftSpeed the left speed of the robot from -1.0 to 1.0
     * @param rightSpeed the right speed of the robot from -1.0 to 1.0
     */
    public void drive(double leftSpeed, double rightSpeed) {
        //if any of the speed Controlers dont exist throw an exception
        if (SpeedController_1 == null || SpeedController_2 == null) {
            throw new NullPointerException("Null motor provided");
        }

        /*
         * if the change in speed of the right motor of the robot is greater than a certain tolerance,
         * it limits the speed to the maximum tolerance.
         */
        if (Math.abs(prevRight - rightSpeed) > 0.2) {
            if (prevRight > rightSpeed) //limit the speed on deceleration
            {
                rightSpeed = prevRight - 0.2;
            } else //limit the speed on acceleration
            {
                rightSpeed = prevRight + 0.2;
            }
        }

        if (Math.abs(prevLeft - leftSpeed) > 0.2) {
            if (prevLeft > leftSpeed) {
                leftSpeed = prevLeft - 0.2;//limit the speed on deceleration
            } else {
                leftSpeed = prevLeft + 0.2;//limit the speed on acceleration
            }
        }

        //sets the speeds of the motors to there final values limit() makes
        // sure the values are not greater than 1.0 and -1.0
        SpeedController_1.set(-1 * limit(leftSpeed));
        SpeedController_2.set(limit(rightSpeed));

        //saves the right and left speeds, so they can be compared on the next change of speed
        prevRight = rightSpeed;
        prevLeft = leftSpeed;

    }

    public void finalDrive(double leftSpeed, double rightSpeed) {
        SpeedController_1.set(-1 * limit(leftSpeed));
        SpeedController_2.set(limit(rightSpeed));
    }

    /**
     * bounds the number between -1.0 and 1.0
     * @param num to be limiteds
     * @return number is greater than 1 returns 1.0 if lower than -1 returns -1.0
     */
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
