/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Sam Meyer
 */
public class Shooter
{
    public void fire(double distance)
    {
        Solenoid shooter = new Solenoid(0);     // slot number
        shooter.set(true);      // set on
        // wait to charge up
        shooter.set(false); // fire
    }

}
