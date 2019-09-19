package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@Autonomous (name = "PID", group = "6209")
public class PIDController extends CustomLinearOpMode
{
    public void RightTurn(double angle)
    {
        // numbers from last year ↓↓↓
        double kU = .0185;
        double tU = .55 / 1000;

        double kP = .6 * kU;
        double kI = tU / 2;
        double kD = tU / 8;

        double angleError = imu.getTrueDiff(angle);
        double oldTime = 0;
        double totalError = 0;
        double oldError = 0;
        double newTime = 0;
        double P = 0;
        double I = 0;
        double D = 0;
    }


}