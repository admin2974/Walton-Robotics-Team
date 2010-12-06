package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.camera.AxisCameraException;

public class Camera {

    private AxisCamera camera;
    Target MainTarget;

    public Camera() {
        camera = AxisCamera.getInstance();
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        camera.writeBrightness(0);

    }

    public double calculateDegrees()
    {
        FindMainTarget();

        if(MainTarget!=null)
        {
            return MainTarget.getHorizontalAngle();
        }
        return Double.NaN;
    }

    public void FindMainTarget() {
        Target[] targets;

        ColorImage image;
        try {
            image = camera.getImage();
            targets = Target.findCircularTargets(image);
            if(targets.length!=0)
            {
            MainTarget = targets[0];
            }
            else
            {
                System.out.println("no targets found!");
                MainTarget = null;
            }
            image.free();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        }



    }

    public double calculateDistance() 
    {
        FindMainTarget();
        if(MainTarget!=null)
        {
        return 56.0 - MainTarget.m_majorRadius * 11.2;
        }
        return Double.NaN;
    }

    public String toString() {
        String targetInfo = MainTarget.toString();
        return "current Target: " + targetInfo + "\nand distance: " + calculateDistance();

    }
}
