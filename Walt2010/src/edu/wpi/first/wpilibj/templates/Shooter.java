/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.VM;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Admin
 */
public class Shooter
{

    Solenoid fire1, fire2, returnValve, latchSolenoid;
    
    int power;
    boolean fireFull;
    


    public Shooter(SpecialJoystick js1, SpecialJoystick js2) {
        //compressor = new Compressor(1,1);

        //compressor.start();
        fire1 = new Solenoid(1);
        fire2 = new Solenoid(2);
        returnValve = new Solenoid(3);
        latchSolenoid = new Solenoid(4);
        fireFull = true;
        
        


    }



    
    public void releasePressureToReturn()
    {
        returnValve.set(false);
    }
    public void releasePressureToShooter()
    {
        fire1.set(false);
        fire2.set(false);
    }
    public void pressureToFire()
    {
        fire1.set(true);
        fire2.set(fireFull);
    }
    public void reload()
    {
        returnValve.set(true);
    }
    public void latch(boolean latched)
    {
        if(latched)
        {
            latchSolenoid.set(false);//false means latch is pressurized
        }
        else
        {
            latchSolenoid.set(true);//true means latch is NOT pressurized
        }

    }
   
}
