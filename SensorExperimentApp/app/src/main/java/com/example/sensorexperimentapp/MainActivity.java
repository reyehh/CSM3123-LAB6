package com.example.sensorexperimentapp;

import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Sensor accelerometer, proximitySensor, lightSensor, rotationVectorSensor;
    private TextView accelerometerData, proximityData, lightData, orientationData; // Added orientationData TextView
    private SensorManager sensorManager;
    private TextView sensorListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SensorManager FIRST
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Initialize rotation vector sensor
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Initialize Sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Find TextViews
        accelerometerData = findViewById(R.id.accelerometerData);
        proximityData = findViewById(R.id.proximityData);
        lightData = findViewById(R.id.lightData);
        sensorListTextView = findViewById(R.id.sensorListTextView);
        orientationData = findViewById(R.id.orientationData); // Initialize orientationData TextView

        // Register listeners
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // Detect Sensors Button
        Button detectSensorButton = findViewById(R.id.detectSensorsButton);
        detectSensorButton.setOnClickListener(v -> ListAvailableSensors());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Handle rotation vector sensor
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);

            // Display orientation data
            orientationData.setText("Orientation : " +
                    "Azimuth = " + Math.toDegrees(orientation[0]) + ", " +
                    "Pitch = " + Math.toDegrees(orientation[1]) + ", " +
                    "Roll = " + Math.toDegrees(orientation[2]));
        }

        // Handle other sensors (uncommented for full functionality)
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            accelerometerData.setText("Accelerometer Data : X = " + x + ", Y = " + y + ", Z = " + z);
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityData.setText("Proximity Data: " + event.values[0]);
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightData.setText("Light Sensor Data: " + event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    private void ListAvailableSensors() {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorInfo = new StringBuilder("Available Sensors:\n");
        for (Sensor sensor : sensorList) {
            sensorInfo.append(sensor.getName()).append(" (").append(sensor.getType()).append(")\n");
        }
        sensorListTextView.setText(sensorInfo.toString());
    }
}