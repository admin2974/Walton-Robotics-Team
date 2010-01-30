/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;

/**
 *
 * @author ported to Java by Sam Meyer
 *
 * ADXL345 Accelerometer on I2C.
 *
 * This class alows access to a Analog Devices ADXL345 3-axis accelerometer on an I2C bus.
 * This class assumes the default (not alternate) sensor address of 0x3A (8-bit address).
 */
public class ADXL345 extends SensorBase
{
	private static final int kAddress = 0x3A;
	private static final int kPowerCtlRegister = 0x2D;
	private static final int kDataFormatRegister = 0x31;
	private static final int kDataRegister = 0x32;
	private static final double kGsPerLSB = 0.004;

        /***********************************************
         * PowerCtlFields
         * *********************************************/
        private static final int kPowerCtl_Link = 0x20;
        private static final int kPowerCtl_AutoSleep = 0x10;
        private static final int kPowerCtl_Measure = 0x08;
        private static final int kPowerCtl_Sleep = 0x04;

         /***********************************************
         * PowerCtlFields
         * *********************************************/
        private static final int kDataFormat_SelfTest = 0x80;
        private static final int kDataFormat_SPI = 0x40;
        private static final int kDataFormat_IntInvert = 0x20;
        private static final int kDataFormat_FullRes = 0x08;
        private static final int kDataFormat_Justify = 0x04;


         /***********************************************
         * DataFormat_Range
         * *********************************************/
        private static final int kRange_2G = 0x00;
        private static final int kRange_4G = 0x01;
        private static final int kRange_8G = 0x02;
        private static final int kRange_16G = 0x03;
         /***********************************************
         * Axes
         * *********************************************/
        private static final int kAxis_X = 0x00;
        private static final int kAxis_Y = 0x02;
        private static final int kAxis_Z = 0x04;

        /**
         * The Default Constructor
         * @param slot The slot of the digital module that the sensor is plugged into.
         * The default range is 2G (0x00)
         */
        public ADXL345(int slot)
        {
            this(slot, 0x00);    // kRange_2G (0x00) is default
        }
         /**
         * Constructor.
         *
         * @param slot The slot of the digital module that the sensor is plugged into.
         * @param range The range (+ or -) that the accelerometer will measure.
         */
        public ADXL345(int slot, int range)
        {
            m_i2c = null;
            DigitalModule module = DigitalModule.getInstance(slot);
            m_i2c = module.getI2C(kAddress);
            m_i2c.write(kPowerCtlRegister, kPowerCtl_Measure);
            m_i2c.write(kDataFormatRegister, kDataFormat_FullRes | (int)range);
        }

        
        
        public static final int[] Axes = {0x00, 0x02, 0x04};

        private I2C m_i2c;

        /**
         *
         * @param Axis IMPORTANT Axes must be an integer: X = 0x00, Y = 0x02, Z = 0x04
         * @return the acceleration in Gs
         */
        public double getAcceleration(int Axis)
        {
            byte[] rawAccel = new byte[2];
            m_i2c.read(kDataRegister + (int)Axis, 2, rawAccel);

            // Sensor is little endian... swap bytes
            byte temp = rawAccel[0];
            rawAccel[0] = rawAccel[1];
            rawAccel[1] = temp;

            int partialAccel = rawAccel[0] * 0x100 + rawAccel[1];
            return partialAccel * kGsPerLSB;
        }
}

