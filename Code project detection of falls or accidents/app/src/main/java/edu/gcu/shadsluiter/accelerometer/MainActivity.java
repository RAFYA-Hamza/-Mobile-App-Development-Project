package edu.gcu.shadsluiter.accelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity {

    TextView txt_AccelX, txt_AccelY, txt_AccelZ;
    ProgressBar prog_Accel;

    // define the sensor variables
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    // define the variable declaration to calculate the acceleration value
    private double accelerationValue;

    // define the number of points plot
    private int pointsPlotted = 5;

    // define the variable for th graph view
    private Viewport viewport;

    // initialize graph view
    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
            new DataPoint(0, 1),
            new DataPoint(1, 5),
            new DataPoint(2, 3),
            new DataPoint(3, 2),
            new DataPoint(4, 6)
    });

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            // variable declaration
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // calculate the acceleration value
            accelerationValue = Math.sqrt((x * x + y * y + z * z));

            // update text views
            txt_AccelX.setText("Axe x : " + x);
            txt_AccelY.setText("Axe y : " + y);
            txt_AccelZ.setText("Axe z : " + z);


            // update the graph
            pointsPlotted++;

            // reset the data after 1000
            if(pointsPlotted > 1000){
                pointsPlotted = 1;
                series.resetData(new DataPoint[] {new DataPoint(1, 0)});
            }

            // set the data
            series.appendData(new DataPoint(pointsPlotted, accelerationValue), true, pointsPlotted);
            viewport.setMaxX(pointsPlotted);
            viewport.setMinX(pointsPlotted - 200);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find the text view by your IDs
        txt_AccelX = findViewById(R.id.txt_AccelX);
        txt_AccelY = findViewById(R.id.txt_AccelY);
        txt_AccelZ = findViewById(R.id.txt_AccelZ);


        // initialize sensor objects
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // sample graph code
        GraphView graph = (GraphView) findViewById(R.id.graph);
        viewport = graph.getViewport();
        viewport.setScrollable(true);
        viewport.setXAxisBoundsManual(true);
        graph.addSeries(series);

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }
}