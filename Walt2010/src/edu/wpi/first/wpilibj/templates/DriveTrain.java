/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * Links controllers and drive-related mechanisms on the robot to joysticks
 * (and other input devices, if/as needed).  A good place for code that delivers
 * customized drive functionality (such as speed limiters/governors or speed
 * multipliers, just to name two).
 *
 * @author Admin
 */
public class DriveTrain
{

    private SpeedController leftSideSpeedController, rightSideSpeedController;
    private Joystick leftJoystick, rightJoystick;
    /**
     * "WALT, activate turbo mode..." :-D
     * Intended as a general trigger for a power multiplier action.
     * TODO: You must synchronize access to this attribute: it's triggered by
     * a joystick action under the current impl (and wants synching regardless).
     */
    public boolean afterBurners;

    /**
     * Takes a left and right speed controller and associated joysticks and
     * starts the instance with afterburners turned off.
     * @param left_SC
     * @param right_SC
     * @param left_JS
     * @param right_JS
     */
    public DriveTrain(SpeedController left_SC, SpeedController right_SC,
            Joystick left_JS, Joystick right_JS) {
        leftSideSpeedController = left_SC;
        rightSideSpeedController = right_SC;
        leftJoystick = left_JS;
        rightJoystick = right_JS;
        afterBurners = false;


    }

    /**
     * This method should be called at least every 0.05 seconds in order to
     * properly update the Drive Train based on Joystick Inputs
     */
    public void update() {
        if (leftJoystick == null || rightJoystick == null) {
            throw new NullPointerException("Null motor provided");
        }
        //Current impl allow
        if (!afterBurners) {
            activateABS(leftSideSpeedController.get(), rightSideSpeedController.get(),
                    leftJoystick.getY() / 2, rightJoystick.getY() / 2);
        } else {
            //doubling each of the joystick X & Y values squares the power? Is
            // that because "normal" mode is 1/2 those values, or...?
            activateABS(leftSideSpeedController.get(), rightSideSpeedController.get(),
                    leftJoystick.getY() * 2, rightJoystick.getY() * 2);//squares the power when in AfterBurner mode
        }
    }

    /**
     * Made private to enforce the current design, which requires that all
     * drive operations be directed through and controlled by the activateABS
     * method.  This method limits the value, which is not a bad idea, since
     * the current governor mechanism (represented by the activateABS method)
     * may well go away, and with an efficient impl of the limiting mechanism
     * this becomes a fast executing method.  And that is a Very Good Thing,
     * Indeed.
     * @param leftSpeed
     * @param rightSpeed after limiting, we set this on the right speed
     */
    private void drive(double leftSpeed, double rightSpeed) {
        if (leftSideSpeedController == null || rightSideSpeedController == null) {
            throw new NullPointerException("Null motor provided");
        }
        leftSpeed = limit(leftSpeed);
        rightSpeed = limit(rightSpeed);
        leftSideSpeedController.set(leftSpeed);
        rightSideSpeedController.set(rightSpeed);

    }

    /**
     * Currently redundant, as the activateABS method guarantees values will
     * not exceed |1.0|.
     * @param num
     * @return
     */
    private double limit(double num) {
        //TODO: Consider usage of Math.abs
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }

    /**
     * Calls the <code>drive</code> method with calculated speed values.
     * Currently serves as a governor (Antilock Braking System, is it?) which
     * cuts speed in two (versus just accepting the associated joystick's value)
     * if the val exceeds |1.0|).
     * @param leftSCVal the value pulled from the left side speed controller
     * @param rightSCVal the value pulled from the right side speed controller
     * @param leftJoystickVal self-explanatory
     * @param rightJoystickVal see <code>leftJoystickVal</code>
     */
    private void activateABS(double leftSCVal, double rightSCVal,
            double leftJoystickVal, double rightJoystickVal) {
        //
        double leftSpeed, rightSpeed;
        //TODO: Consider the pros and cons of the current impl. Now it performs
        // a subtraction and comparison, then possibly another subtraction and
        // comparison, then does the same thing for the right channel.  Would
        // something like the following be more efficient?  Would it be easier
        // or harder to understand the code and what it does?  If you can begin
        // now to code everything under the assumption that other people not as 
        // smart as you will have to support and modify it, you will find this
        // to pay amazing dividends!  Think on this: in three months, or three
        // years, will YOU remember exactly how you're thinking now?  Anyway,
        // here's one alternative to consider:
        // if (Math.abs(leftSCVal - leftJoystickVal) > 1.0) {
        //     leftSpeed = leftJoystickVal / 2;
        // } else ...
        //
        if (leftSCVal - leftJoystickVal > 1.0
                || leftSCVal - leftJoystickVal < -1.0) {
            leftSpeed = leftJoystickVal / 2;
        } else {
            leftSpeed = leftJoystickVal;
        }
        if (rightSCVal - rightJoystickVal > 1.0
                || rightSCVal - rightJoystickVal < -1.0) {
            rightSpeed = rightJoystickVal / 2;
        } else {
            rightSpeed = rightJoystickVal;
        }

        drive(leftSpeed, rightSpeed);
    }
}
