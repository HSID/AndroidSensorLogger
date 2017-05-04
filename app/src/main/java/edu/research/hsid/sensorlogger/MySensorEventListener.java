package edu.research.hsid.sensorlogger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.research.hsid.sensorlogger.MainActivity;

/**
 * Created by doraemon on 5/1/17.
 */

public class MySensorEventListener implements SensorEventListener {
    private StringBuilder log_perf = null;
    private BufferedWriter out = null;
    private static int count = 0;
    private final static int FLUSH_COUNT = 1000;

    public MySensorEventListener(){};

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public final void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                try {
                    out = new BufferedWriter(
                            new FileWriter(MainActivity.logFileRootDirectory.toString()
                                    + MainActivity.NAME_ACC_LOG + event.timestamp));
                } catch (IOException e) {
                    Log.e("AccLogger", e.toString());
                }
                log_perf.append(event.timestamp)
                        .append("\t" + event.values[0]
                                + " " + event.values[1]
                                + " " + event.values[2]
                                + "\n");
                count++;
                if (count >= FLUSH_COUNT) {
                    count = 0;
                    try {
                        out.write(log_perf.toString());
                        out.flush();
                    } catch (IOException e) {
                        Log.e("AccLogger", e.toString());
                    }
                    log_perf.delete(0, log_perf.length());
                }
                break;
            }
            //case Sensor.TYPE_STEP_DETECTOR: {
            //    File file = new File(MainActivity.logFileRootDirectory, MainActivity.NAME_STEPDETECTOR_LOG);
            //}
        }
    }
}
