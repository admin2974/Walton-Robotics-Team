package edu.wpi.first.wpilibj.templates;

public class TestCamera {
    public static void main(String[] args) {
        Camera camera=new Camera();
camera.FindMainTarget();
System.out.println(camera.toString());
    }

}
