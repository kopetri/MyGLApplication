package sebastianhartwig.com.myglapplication;


        import android.content.Context;
        import android.hardware.SensorManager;
        import android.opengl.GLSurfaceView;
        import android.util.Log;
        import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;
    private float[] accelerometer_values;
    private float[] magnetic_field_values;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    public void onSensorChangedHandle(float azimuth, float pitch, float roll) {
        mRenderer.setzAngle(azimuth*0.1f);
        mRenderer.setxAngle(pitch);
        mRenderer.setyAngle(roll*0.1f);
        requestRender();
    }


    public MyGLRenderer getmRenderer() {
        return mRenderer;
    }

    public float[] getMagnetic_field_values() {
        return magnetic_field_values;
    }

    public void setMagnetic_field_values(float[] magnetic_field_values) {
        this.magnetic_field_values = magnetic_field_values;
    }

    public float[] getAccelerometer_values() {
        return accelerometer_values;
    }

    public void setAccelerometer_values(float[] accelerometer_values) {
        this.accelerometer_values = accelerometer_values;
    }
}
