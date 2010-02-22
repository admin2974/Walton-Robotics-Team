package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.templates.Target;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
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

    public void FindMainTarget() {
        Target[] targets;

        ColorImage image;
        try {
            image = camera.getImage();
            targets = MainTarget.findCircularTargets(image);
            MainTarget = targets[0];
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        }



    }

    public double calculateDistance() {
        return 56.0 - MainTarget.m_majorRadius * 11.2;

    }

    public String toString() {
        String targetInfo = MainTarget.toString();
        return "current Target: " + targetInfo + "\nand distance: " + calculateDistance();

    }
}
