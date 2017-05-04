package edu.research.hsid.sensorlogger;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.research.hsid.sensorlogger.MySensorEventListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public final static String LOG_TAG = "Runtime_info";
    public final static String NAME_APP_ROOT = "SensorLogger";
    public final static String NAME_LOG_ROOT = "Log_files";
    public final static String NAME_ACC_LOG = "Accelerometer";
    public final static String NAME_GYRO_LOG = "Gyroscope";
    public final static String NAME_STEPDETECTOR_LOG = "StepDetector";
    public final static String NAME_STEPCOUNTER_LOG = "StepCounter";
    public final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public final static int FLUSH_COUNT = 1;

    public static File logFileRootDirectory;
    private static SensorManager mSensorManager;
    private static int count = 0;

    private static String NAME_ACC_LOG_FILE = null;
    private static String NAME_GYRO_LOG_FILE = null;
    private static String NAME_STEPDETECTOR_LOG_FILE = null;
    private static String NAME_STEPCOUNTER_LOG_FILE = null;

    private StringBuilder log_perf = new StringBuilder();
    private BufferedWriter out = null;
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            createSensorDataLogDirectory(NAME_LOG_ROOT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createSensorDataLogDirectory(NAME_LOG_ROOT);
                }
                return;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            /* For the current version of this sensor logger, the FLUSH_COUNT can only be set to 1.
            *   Values larger than 1 will cause any single file contains data from multiple sensors.*/
            case Sensor.TYPE_ACCELEROMETER: {
                log_perf.append(event.sensor.getName() + " ")
                        .append(event.timestamp)
                        .append("\t" + event.values[0]
                                + " " + event.values[1]
                                + " " + event.values[2]
                                + "\n");
                count++;
                if (count >= FLUSH_COUNT) {
                    count = 0;
                    try {
                        out = new BufferedWriter(
                                new FileWriter(logFileRootDirectory.toString()
                                + "/" + NAME_ACC_LOG
                                + "/" + NAME_ACC_LOG_FILE, true)
                        );
                        out.write(log_perf.toString());
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        Log.e("AccLogger", e.toString());
                    }
                    log_perf.delete(0, log_perf.length());
                }
                Log.e("RUN_TIME_ACC", Long.toString(event.timestamp));
                break;
            }
            case Sensor.TYPE_GYROSCOPE: {
                log_perf.append(event.sensor.getName() + " ")
                        .append(event.timestamp)
                        .append("\t" + event.values[0]
                                + " " + event.values[1]
                                + " " + event.values[2]
                                + "\n");
                count++;
                if (count >= FLUSH_COUNT) {
                    count = 0;
                    try {
                        out = new BufferedWriter(
                                new FileWriter(logFileRootDirectory.toString()
                                        + "/" + NAME_GYRO_LOG
                                        + "/" + NAME_GYRO_LOG_FILE, true)
                        );
                        out.write(log_perf.toString());
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        Log.e("GyroLogger", e.toString());
                    }
                    log_perf.delete(0, log_perf.length());
                }
                Log.e("RUN_TIME_GYRO", Long.toString(event.timestamp));
                break;
            }
            case Sensor.TYPE_STEP_DETECTOR: {
                log_perf.append(event.sensor.getName() + " ")
                        .append(event.timestamp)
                        .append("\t" + event.values[0]
                                + " " + event.values[1]
                                + " " + event.values[2]
                                + "\n");
                count++;
                if (count >= FLUSH_COUNT) {
                    count = 0;
                    try {
                        out = new BufferedWriter(
                                new FileWriter(logFileRootDirectory.toString()
                                        + "/" + NAME_STEPDETECTOR_LOG
                                        + "/" + NAME_STEPDETECTOR_LOG_FILE, true)
                        );
                        out.write(log_perf.toString());
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        Log.e("StepDetectorLogger", e.toString());
                    }
                    log_perf.delete(0, log_perf.length());
                }
                break;
            }
            case Sensor.TYPE_STEP_COUNTER: {
                log_perf.append(event.sensor.getName() + " ")
                        .append(event.timestamp)
                        .append("\t" + event.values[0]
                                + "\n");
                count++;
                if (count >= FLUSH_COUNT) {
                    count = 0;
                    try {
                        out = new BufferedWriter(
                                new FileWriter(logFileRootDirectory.toString()
                                        + "/" + NAME_STEPCOUNTER_LOG
                                        + "/" + NAME_STEPCOUNTER_LOG_FILE, true)
                        );
                        out.write(log_perf.toString());
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        Log.e("StepCounterLogger", e.toString());
                    }
                    log_perf.delete(0, log_perf.length());
                }
                displayOnMainActivity(Float.toString(event.values[0]));
                Log.e("RUN_TIME_STEP_COUNTER", Long.toString(event.timestamp));
                break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void createSensorDataLogDirectory(String logFileName) {
        if (isExternalStorageWritable()) {
            File logFileDirectory = getSensorDataExternalStorageDir(logFileName);
            if (logFileDirectory.exists()) {
                Log.e(LOG_TAG, "Directory exists!");
                Log.e(LOG_TAG, logFileDirectory.toString());
                logFileRootDirectory = logFileDirectory;
                mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
                displayOnMainActivity(formatSensorInfoForDisplay(deviceSensors));
            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getSensorDataInternalStorageDir(Context context, String logFileName) {
        File file = new File(context.getExternalFilesDir(null), logFileName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created!");
        }
        return file;
    }

    public File getSensorDataExternalStorageDir(String logFileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(NAME_APP_ROOT), logFileName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created!");
        }
        return file;
    }

    public void startAcc(View view) {
        File file = new File(logFileRootDirectory, NAME_ACC_LOG);
        file.mkdirs();
        if (!file.exists()) {
            Log.e("ACC_START", "Directory not exists!");
        } else {
            cal.setTimeInMillis(System.currentTimeMillis());
            Date date = cal.getTime();
            NAME_ACC_LOG_FILE = date.toString();
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST);
            Button button = (Button) findViewById(R.id.button_start_acc);
            button.setBackgroundColor(Color.BLUE);
        }
    }

    public void stopAcc(View view) {
        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        Button button = (Button) findViewById(R.id.button_start_acc);
        button.setBackgroundColor(Color.GRAY);
    }

    public void startGyro(View view) {
        File file = new File(logFileRootDirectory, NAME_GYRO_LOG);
        file.mkdirs();
        if (!file.exists()) {
            Log.e("GYRO_START", "Directory not exists!");
        } else {
            cal.setTimeInMillis(System.currentTimeMillis());
            Date date = cal.getTime();
            NAME_GYRO_LOG_FILE = date.toString();
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    SensorManager.SENSOR_DELAY_FASTEST);
            Button button = (Button) findViewById(R.id.button_start_gyro);
            button.setBackgroundColor(Color.BLUE);
        }
    }

    public void stopGyro(View view) {
        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        Button button = (Button) findViewById(R.id.button_start_gyro);
        button.setBackgroundColor(Color.GRAY);
    }

    public void startStepDetector(View view) {
        File file = new File(logFileRootDirectory, NAME_STEPDETECTOR_LOG);
        file.mkdirs();
        if (!file.exists()) {
            Log.e("STEPDETECTOR_START", "Directory not exists!");
        } else {
            cal.setTimeInMillis(System.currentTimeMillis());
            Date date = cal.getTime();
            NAME_STEPDETECTOR_LOG_FILE = date.toString();
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                    SensorManager.SENSOR_DELAY_FASTEST);
            Button button = (Button) findViewById(R.id.button_start_stepDetector);
            button.setBackgroundColor(Color.BLUE);
        }
    }

    public void stopStepDetector(View view) {
        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR));
        Button button = (Button) findViewById(R.id.button_start_stepDetector);
        button.setBackgroundColor(Color.GRAY);
    }

    public void startStepCounter(View view) {
        File file = new File(logFileRootDirectory, NAME_STEPCOUNTER_LOG);
        file.mkdirs();
        if (!file.exists()) {
            Log.e("STEPCOUNTER_START", "Directory not exists!");
        } else {
            cal.setTimeInMillis(System.currentTimeMillis());
            Date date = cal.getTime();
            NAME_STEPCOUNTER_LOG_FILE = date.toString();
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                    SensorManager.SENSOR_DELAY_UI);
            Button button = (Button) findViewById(R.id.button_start_stepCounter);
            button.setBackgroundColor(Color.BLUE);
        }
    }

    public void stopStepCounter(View view) {
        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));
        Button button = (Button) findViewById(R.id.button_start_stepCounter);
        button.setBackgroundColor(Color.GRAY);
    }

    //Utils for debugging
    public void displayOnMainActivity(String whatToDisplay) {
        TextView textView = (TextView) findViewById(R.id.text_display);
        textView.setTextSize(10);
        textView.setText(whatToDisplay);
    }

    public String formatSensorInfoForDisplay(List<Sensor> sensorList) {
        String sensorInfo = "";
        Sensor temp = null;
        for (int i = 0; i < sensorList.size(); i++) {
            temp = sensorList.get(i);
            sensorInfo += temp.getName() + "\n";
            sensorInfo += "\t\tVendor: " + temp.getVendor();
            sensorInfo += "\tMaximumRange: " + temp.getMaximumRange();
            sensorInfo += "\tMinimumDelay: " + temp.getMinDelay() + "\n";
        }
        return sensorInfo;
    }
}
