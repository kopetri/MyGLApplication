package sebastianhartwig.com.myglapplication;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;


public class MyGLActivity extends Activity {

    private MyGLSurfaceView mGLView;
    private SensorManager mSensor;
    private Sensor accelerometer;
    private Sensor magnetic_field;

    float[] mGravs = new float[3];
    float[] mGeoMags = new float[3];
    float[] mRotationM = new float[16];
    float[] mInclinationM = new float[16];
    float[] mOrientation = new float[3];
    float[] mOldOreintation = new float[3];

    private SensorEventListener accListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }


        /* Get the Sensors */
        public void onSensorChanged(SensorEvent event) {

            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, mGravs, 0, 3);
                    break;
                default:
                    return;
            }

            // If mGravs and mGeoMags have values then find rotation matrix
            if (mGravs != null && mGeoMags != null) {

                // checks that the rotation matrix is found
                boolean success = SensorManager.getRotationMatrix(mRotationM, mInclinationM, mGravs, mGeoMags);
                if (success) {
                        /* getOrientation Values */
                    SensorManager.getOrientation(mRotationM, mOrientation);
                    mGLView.onSensorChangedHandle(mOrientation[0],mOrientation[1],mOrientation[2]);

                }
            }
        }
    };

    private SensorEventListener magnetListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }


        /* Get the Sensors */
        public void onSensorChanged(SensorEvent event) {

            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(event.values, 0, mGeoMags, 0, 3);

                    break;
                default:
                    return;
            }

            // If mGravs and mGeoMags have values then find rotation matrix
            if (mGravs != null && mGeoMags != null) {

                // checks that the rotation matrix is found
                boolean success = SensorManager.getRotationMatrix(mRotationM, mInclinationM, mGravs, mGeoMags);
                if (success) {
                        /* getOrientation Values */
                    SensorManager.getOrientation(mRotationM, mOrientation);
                    mGLView.onSensorChangedHandle(mOrientation[0],mOrientation[1],mOrientation[2]);

                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        mSensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic_field = mSensor.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
        if(accelerometer != null && magnetic_field != null){
            mSensor.unregisterListener(accListener);
            mSensor.unregisterListener(magnetListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
        mSensor.registerListener(accListener, mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensor.registerListener(magnetListener, mSensor.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
    }
}