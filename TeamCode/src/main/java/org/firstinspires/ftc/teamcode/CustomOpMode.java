package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREV2mDistance;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class CustomOpMode extends OpMode
{
   DcMotor motorFR;
   DcMotor motorFL;
   DcMotor motorBR;
   DcMotor motorBL;


   IMU imu;

   ModernRoboticsI2cRangeSensor rangeSensor;

   public void init()
   {
        initialize();
      //  loop();
   }

   public void loop()
   {
       // extend mecanum drive class?
       /*
            // Vd = Desired robot speed [−1,1]
            // angle = Desired robot angle [0, 360]
            // Vt = Desired speed for changing direction [−1,1]

            // double angle = gamepad.leftStick(Get360degrees)
            // double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            // int Vd = gamepad.
            // int

           motorBR.setPower(WheelSpeed(Vd, angle, Vt));

        */

       gamepad1.left_stick_y = 0;

   }

   public void initialize()
   {
       motorFR = hardwareMap.dcMotor.get("motorFR");
       motorFL = hardwareMap.dcMotor.get("motorFL");
       motorBR = hardwareMap.dcMotor.get("motorBR");
       motorBL = hardwareMap.dcMotor.get("motorBL");

       rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

       motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

       motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


       motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
       motorBL.setDirection(DcMotorSimple.Direction.REVERSE);


       motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


       stopDriveMotors();


       telemetry.addData("Motor Initialization Complete", "");

       imu = new IMU(hardwareMap.get(BNO055IMU.class, "imu"));
       imu.IMUinit(hardwareMap);

       telemetry.addData("IMU Initialization Complete", "");
       telemetry.addData("Initialization Complete", "");

   }

    public void stopDriveMotors() {
        motorFR.setPower(0);
        motorFL.setPower(0);
        motorBR.setPower(0);
        motorBL.setPower(0);
    }


    public void setLeftMotors(double left){
        motorFL.setPower(left);
        motorBL.setPower(left);
    }

    public void setRightMotors(double right){
        motorFR.setPower(right);
        motorBR.setPower(right);
    }


    public double rightABSMotorVal(double joyStickVal) {
        double maxJump = .4;
        double c = .1;
        if (joyStickVal >= motorBR.getPower() + maxJump) {
            return Range.clip(motorBR.getPower() + c, -1, joyStickVal);
        }
        else if (joyStickVal < motorBR.getPower() - maxJump) {
            return Range.clip(motorBR.getPower() - c, joyStickVal, 1);
        }
        else return joyStickVal;
    }


    public double leftABSMotorVal(double joyStickVal) {
        double maxJump = .4;
        double c = .1;
        if (joyStickVal >= motorBL.getPower() + maxJump) {
            return Range.clip(motorBL.getPower() + c, -1, joyStickVal);
        }
        else if (joyStickVal < motorBL.getPower() - maxJump) {
            return Range.clip(motorBL.getPower() - c, joyStickVal, 1);
        }
        else return joyStickVal;
    }

    public double getDist()
    {
        double dist = rangeSensor.getDistance(DistanceUnit.INCH);
        while ((dist > 200 || Double.isNaN(dist))) {
            dist = rangeSensor.getDistance(DistanceUnit.INCH);
        }
        return dist;
    }


}

/*
        public void arcadeDrive() {
        //checking for valid range to apply power (has to give greater power than .1)
        if (((Math.abs(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y))) > .1) ||
                Math.abs(Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4) > .1) {

            // r can be sqrt(2)
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double theta = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = -gamepad1.right_stick_x;

            //as per unit circle cos gives x, sin gives you y
            double FL = r * Math.cos(theta) + rightX;
            double FR = r * Math.sin(theta) - rightX;
            double BL = r * Math.sin(theta) + rightX;
            double BR = r * Math.cos(theta) - rightX;

            //make sure you don't try and give power bigger than 1
            if (((Math.abs(FL) > 1) || (Math.abs(BL) > 1)) || ((Math.abs(FR) > 1) || (Math.abs(BR) > 1))) {
                FL /= Math.max(Math.max(Math.abs(FL), Math.abs(FR)), Math.max(Math.abs(BL), Math.abs(BR)));
                BL /= Math.max(Math.max(Math.abs(FL), Math.abs(FR)), Math.max(Math.abs(BL), Math.abs(BR)));
                FR /= Math.max(Math.max(Math.abs(FL), Math.abs(FR)), Math.max(Math.abs(BL), Math.abs(BR)));
                BR /= Math.max(Math.max(Math.abs(FL), Math.abs(FR)), Math.max(Math.abs(BL), Math.abs(BR)));

            }
            dt.fl.setPower(FL);
            dt.fr.setPower(FR);
            dt.bl.setPower(BL);
            dt.br.setPower(BR);

        }
        else {
            dt.fl.setPower(0);
            dt.fr.setPower(0);
            dt.bl.setPower(0);
            dt.br.setPower(0);

        }

    }
 */