
package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.Accelerometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot {
    DriveTrain robot_drive;
    Jaguar motorController1, motorController2;
    PWM cameraBase;
    SpecialJoystick JS1, JS2;
    Accelerometer accel;
    Relay ballControl;
    Solenoid shooterSol1, shooterSol2;
    Shooter shooter;
    Thread shooterThread;
    Compressor compressor;
    Camera cam;
    Servo cameraTiltServo;
    PWM cameraPanMotor;
    AxisCamera camera;
    Relay r;
    Gyro g;
    Vector v;
    ADXL345_I2C acceler;
    boolean shooting,shoot,reload;
    int shooterCounter;
    int initReload;
     /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        Watchdog.getInstance().setEnabled(false);
        motorController1 = new Jaguar(1);
        motorController2 = new Jaguar(2);
        JS1 = new SpecialJoystick(this,1,true);//true == left Joystick
        JS2 = new SpecialJoystick(this,2,false);//false == right Joystick
//        cameraTiltServo = new Servo(5);
//        cameraPanMotor = new PWM(7);
        
        compressor = new Compressor(1,1);
        compressor.start();
        shoot = false;
        shooting = false;
        shooterCounter=0;
         initReload = 0;
        reload=true;
//        enc1 = new Encoder(1,2);
//        enc1.start();
        g = new Gyro(1);
        g.reset();
        cameraBase = new PWM(10);
        cameraTiltServo= new Servo(9);
        shooter = new Shooter(JS1,JS2);
        v = new Vector(g);

//        cam = new Camera();
        ballControl = new Relay(2);
//        acceler = new ADXL345_I2C(4,ADXL345_I2C.DataFormat_Range.k4G);
      robot_drive = new DriveTrain(motorController1,motorController2,JS1, JS2);
      AxisCamera.getInstance();


    }

    public void disabledPeriodic()
    {
        
    }


    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
        //Testing needed
        Watchdog.getInstance().feed();
        
robot_drive.drive(.5, .5);
Timer.delay(.5);

    }

    public void teleopInit()
    {
         shooterCounter=0;
         initReload = 0;
        reload=true;
        System.out.println("(Custom Message) Starting teleopPeriodic()...");

    }


    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        shooterCounter=0;
        initReload = 0;
        reload=true;
        ballControl.set(Relay.Value.kReverse);
//        g.reset();
        
        while(true)//Infinite loop that checks the state of the robot and driver station every .02 seconds
        {
            Watchdog.getInstance().feed();
            robot_drive.update();
            JS1.check();
            JS2.check();
            v.update();
            cameraBase.setRaw(255);
//            System.out.println(acceler.getAcceleration(ADXL345_I2C.Axes.kX));

            if(initReload>0 || (!shooting && reload))
            {
                shooting = true;
                if(initReload<25)//reload
                {
                    shooter.latch(false);
                    shooter.releasePressureToShooter();
                    shooter.reload();
                    initReload++;
                    System.out.println("Reload");
                }
                else if(initReload<40)//wait .5 seconds then latch
                {
                    shooter.latch(true);
                    System.out.println("latching...");
                    initReload++;
                }
                else if(initReload<50)//wait .15 seconds then releasePressure to return
                {

                    shooter.releasePressureToReturn();
                    initReload=0;
                    System.out.println("depresurizing...");
                    reload =false;
                    shooting = false;
                }
            }
            if((shoot && !shooting) || shooterCounter>0 )
            {
                shooting = true;
                if(shooterCounter<50)
                {
                    shooter.latch(true);
                    shooter.pressureToFire();
                    shooterCounter++;
                    System.out.println("Presurizing fire");
                }
                else if(shooterCounter<75)//50 loops at .02 sec each = 1 sec
                {
                    shooter.latch(false);
                    System.out.println("Firing.....!");
                    shooterCounter++;
                }
                else if(shooterCounter<100)//wait .5 seconds then reload
                {
                    shooter.releasePressureToShooter();
                    shooter.reload();
                    shooterCounter++;
                }
                else if(shooterCounter<125)//wait .5 seconds then latch
                {
                    shooter.latch(true);
                    shooterCounter++;
                }
                else if(shooterCounter<130)//wait .15 seconds then releasePressure to return
                {
                    shooter.releasePressureToReturn();
                    shooterCounter=0;
                    shoot = false;
                    shooting = false;
                }

            }

//            cameraTiltServo.setAngle(JS1.getY()*180);
//            cameraPanMotor.setRaw((int)(JS1.getX()+1)*255/2);
            
           




            
            //System.out.println(accel.getAcceleration());
//            System.out.println(accel.getAcceleration(2));
//           if(JS1.getTrigger())
//           {
//               System.out.println("taking pictures...");
//            try {
//                ColorImage image = camera.getImage();
//                System.out.println("freshImage=" + camera.freshImage());
//                camera.writeResolution( AxisCamera.ResolutionT.k160x120);
//                image.write("image_" + counter + "_160x120.bmp");
//                camera.writeResolution( AxisCamera.ResolutionT.k320x240);
//                image.write("image_" + counter + "_320x240.bmp");
//                camera.writeResolution( AxisCamera.ResolutionT.k640x360);
//                image.write("image_" + counter + "_640x360.bmp");
//                camera.writeResolution( AxisCamera.ResolutionT.k640x480);
//                image.write("image_" + counter + "_640x480.bmp");
//                counter++;
//                        //MonoImage bwImage = image.getLuminancePlane();
//
//            } catch (AxisCameraException ex) {
//                System.out.println("CANT GET IMAGE!\n in axiscamera");
//                System.out.println(ex.getMessage());
//                //ex.printStackTrace();
//            } catch (NIVisionException ex) {
//                System.out.println("CANT GET IMAGE!\n in nivision");
//                System.out.println(ex.getMessage());
//            }
//           }




            Timer.delay(.02);
        }
        

    }

   
    

}
