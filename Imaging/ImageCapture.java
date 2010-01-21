/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.usfirst.frc;

import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.*;
import java.util.Vector;

/**
 *
 * @author Tyler
 */
public class ImageCapture {
AxisCamera camera = AxisCamera.getInstance();

public void init()
{
    camera.writeResolution( AxisCamera.ResolutionT.k320x240);
}

public void transferImages()
{
    try {
                ColorImage image = camera.getImage();
               MonoImage bwImage = image.getLuminancePlane();
               findEllipses(bwImage);
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            }
}

public EllipseMatch[] findEllipses(MonoImage image)
{
        try {
            return image.detectEllipses(new EllipseDescriptor(5, 10, 1, 5));
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }

return null;
}
}
