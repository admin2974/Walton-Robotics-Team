/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.CurveOptions;
import edu.wpi.first.wpilibj.image.EllipseDescriptor;
import edu.wpi.first.wpilibj.image.EllipseMatch;
import edu.wpi.first.wpilibj.image.MonoImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.RegionOfInterest;
import edu.wpi.first.wpilibj.image.ShapeDetectionOptions;


/**
 *
 * @author Tyler
 */
public class ImageManipulator {

    AxisCamera camera;
    EllipseDescriptor imgEllipseDescriptor;
    CurveOptions imgCurveOptions;
    ShapeDetectionOptions imgShapeDetectionOptions;
    RegionOfInterest imgRegionOfInterest;
    EllipseMatch[] circles;


public ImageManipulator()
{
    camera = AxisCamera.getInstance();
    //set image attributes here

    imgEllipseDescriptor = new EllipseDescriptor(25,500,25,500); //todo calc geom & revise params

    MonoImage img = null;
    while(true)
    {
            try
            {
                img = camera.getImage().getLuminancePlane();
                circles = img.detectEllipses(imgEllipseDescriptor, imgCurveOptions, null, null);
                for(int i = 0; i < circles.length; i++)
                {
                    System.out.println(circles[i].toString());
                }
                img.free();
            }
            catch (AxisCameraException ex)
            {
                ex.printStackTrace();
            }
            catch (NIVisionException ex)
            {
                ex.printStackTrace();
            }
            try
            {
                img.free();
            }
            catch (NIVisionException ex)
            {
                ex.printStackTrace();
            }
    }
}

}
