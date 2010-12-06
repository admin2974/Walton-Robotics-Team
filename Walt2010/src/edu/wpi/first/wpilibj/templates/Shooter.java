package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Admin
 */
public class Shooter {

    private Solenoid fire1, fire2, returnValve, latchSolenoid;
    private double power;
    private boolean FFM;
//    private boolean reloaded;
    private boolean m_run = true;
    private boolean m_enabled;
    private boolean shooting, reloading;
    private Thread m_task;

    /**
     * This creates an Object that represents the mechanisms that make up the shooter for the robot.
     * An internal thread is created that is known as a task.
     * This task handles all complecated timed events such as shooting and reloading.
     * All this task is doing is calling the basic methods of the shooter object at given times.
     * To alter what this tasks does you must alter the state of the shooter object it is observing.
     * you can do that with methods like:
     * -fire()
     * -reload()
     * -unload()
     * these methods tell the Shooter task what to do.
     */
    public Shooter() {
        fire1 = new Solenoid(1);
        fire2 = new Solenoid(4);
        returnValve = new Solenoid(3);
        latchSolenoid = new Solenoid(2);
        FFM = true;
        m_enabled = true;
        shooting = false;
        reloading = false;
        initShooter();
    }
    /*
     * This Initializes the internal thread that will determine the shooter's actions
     */

    private void initShooter() {
        m_task = new ShooterThread(this);
        m_task.start();
    }

    /**
     * Start the shooter.
     * This method will allow the polling loop to actually operate the shooter. The
     * is started by default so this method is only needed if the shooter was stopped.
     */
    public void start() {
        m_enabled = true;
    }

    /**
     * Stop the shooter.
     * This method will stop the shooter from turning on.
     */
    public void stop() {
        m_enabled = false;
    }

    /**
     * Get the state of the enabled flag.
     * Return the state of the enabled flag for the shooter
     *
     * @return The state of the shooter thread's enable flag.
     */
    public boolean enabled() {
        return m_enabled;
    }

    /**
     *This method triggers the firing sequence in the Shooter Thread
     * @param power is the power from 0 - 1.0 which represents the how long the the valve to the systems main pressure will be opened
     */
    public void fire(double power) {
        if (!shooting) {
            this.power = limit(power);
            shooting = true;
        }
    }

    /**
     * This method changes a boolean that tells the Shooters internal Thread to start the reload sequence
     */
    public void reload() {
        reloading = true;
    }

    /**
     * This will depresurize the entire shooter system.
     * -releases all presure to shooter cylinders
     * -drops kicker
     *
     * Also sets shooter's state to unloaded so that the next time the shooter shoots it will reload
     */
    public void unload() {
        releasePressureToReturn();
        releasePressureToShooter();
        latch(false);
//        reloaded = false;
    }

    /**
     * FFM stands for Full Fire Mode
     * if FFM is enabled(true) then the shooter will fire with both cylinders.
     *  -this will generally provide a more powerful kick however requires double the amount of air
     * if FFM is disabled(false) shooter will operate with only one cylinder
     *  -this will produce a less powerfull kick but will not use as much air.
     * @param FullFireMode represents if Full Fire Mode(FFM) is on(true) or off(false)
     */
    public void setFFM(boolean FullFireMode) {
        FFM = FullFireMode;
    }

    /**
     * @return a boolean representing if FFM(Full Fire Mode) is activated
     *              ex) returns true - the shooter will fire with 2 cylinders
     *                  returns false - the shooter will fire with 1 cylinder
     */
    public boolean getFFM() {
        return FFM;
    }

    /**
     * Opens the valve powering the side of the cylinder used to pull the shooter back or reload.
     * This allows any pressure used to pull the kicker back to be released.
     * if this method is not called the cylinder will have an excess amount of pressure in it and will not be able to be fired
     */
    public void releasePressureToReturn() {
        returnValve.set(false);
    }

    /**
     * Opens valve to the two shooting cylinders. This allows for any air that was used for firing to escape.
     * if this air is not released the kicker CANNOT be retracted!!!
     * THIS METHOD MUST BE CALLED AFTER KICKING
     */
    public void releasePressureToShooter() {
        fire1.set(false);
        fire2.set(false);
    }

    /**
     * Allows pressurized air to flow into one or two of the firing cylinders.
     * The number of cylinders this method uses is determined based on the state of the Shooter object.
     * see setFullFireMode(boolean), and getFullFireMode()
     */
    public void pressureToFire() {
        fire1.set(true);
        fire2.set(FFM);
    }

    /**
     * Allows pressurized air to flow into one return cylinder
     * assuming that pressure to the top of the cylinder(firing pressure) has been released
     * the kicker will pull itself up off the floor.
     * Note: This does NOT latch the kicker back
     */
    public void pressureToReturn() {
        returnValve.set(true);
    }

    /**
     * This method determines the behavior of the mechanism that holds the kicker back.
     * @param latched if this is true the latch will close and try to latch the kicker in place
     * if false it will release the kicker(assuming the kicker is there)
     */
    public void latch(boolean latched) {
        if (latched) {
            latchSolenoid.set(false); //false means latch is pressurized
        } else {
            latchSolenoid.set(true); //true means latch is NOT pressurized
        }

    }

    //this method limits the value from 0 to 1.0
    private double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < 0) {
            return 0;
        }
        return num;
    }

    /**
     * Internal thread.
     *
     * Task which checks the shooter and determines an apropriate action based on shooter's internal counter
     *
     *
     * Do not call this function directly.
     */
    private class ShooterThread extends Thread {

        Shooter m_shooter;

        ShooterThread(Shooter shooter) {
            m_shooter = shooter;
        }

        public void run() {
            while (m_run) {
                if (m_shooter.enabled()) {
                    if (reloading) {
                        m_shooter.latch(false);
                        m_shooter.releasePressureToShooter();
                        m_shooter.pressureToReturn();
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                        }
                        m_shooter.latch(true);
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                        }
                        m_shooter.releasePressureToReturn();
//                            reloaded = true;
                        reloading = false;
                    } else if (shooting) {
//                        if(!reloaded)//if someone released shooter then it reloads it before shooting
//                        {
//                            m_shooter.latch(false);
//                            m_shooter.releasePressureToShooter();
//                            m_shooter.pressureToReturn();
//                            try {Thread.sleep(800);} catch (InterruptedException e) {}
//                            m_shooter.latch(true);
//                            try {Thread.sleep(400);} catch (InterruptedException e) {}
//                            m_shooter.releasePressureToReturn();
//                            reloaded = true;
//                        }

                        //pressurize the shooter
                        m_shooter.pressureToFire();
                        try {
                            Thread.sleep((int) (power * 1000));
                        } catch (InterruptedException e) {
                        }
                        //shooter is pressurized

                        //fire the shooter
                        m_shooter.latch(false);
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                        }
                        //shooter is fully extended and has kicked the ball .... hopefully into the goal

                        //reload the shooter
                        m_shooter.reload();
                        shooting = false;//shooting is complete and will not be fired on next loop
                    }
                } else {
                    unload();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
