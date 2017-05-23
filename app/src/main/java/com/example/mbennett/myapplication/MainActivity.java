package com.example.mbennett.myapplication;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.mbennett.myapplication.R.id.introText;
import static java.lang.Math.pow;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAeccelerometer;
    private long lastUpdate = 0;

    private float speedX, speedY, speedInitialX, speedInitialY = 0;

    private static final int SHAKE_THRESHOLD = 600;
    TextView IntroText;
    TextView star;
    private float last_posX, last_posY, posX, posY;

    float xmax = 250;
    float ymax = 700;

    float pixelMeter = 3779;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAeccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAeccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        //IntroText = (TextView) findViewById(R.id.introText);
        IntroText = (TextView) findViewById(R.id.introText);
        IntroText.setText("testing");
        star = (TextView) findViewById(R.id.star);


        last_posX = star.getX()/pixelMeter;
        last_posY = star.getY()/pixelMeter;



    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        Sensor mySensor = sensorEvent.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){ //Check to make sure this is the correct sensor type
            float x = sensorEvent.values[0]*-1; //acceleration (minus gravity) on each respective axis)
            float y = sensorEvent.values[1]*-1;


            long curTime = System.currentTimeMillis(); //storing the systems current time in milliseconds

         //   if ((curTime - lastUpdate) > 100){ //test to make sure that it has been more than 100 milliseconds since this was last run

                float diffTime = curTime - lastUpdate;
                lastUpdate = curTime;
                diffTime = diffTime/1000;




            speedX = speedInitialX+x*diffTime;
            posX = 1/2*x*diffTime*diffTime+speedInitialX*diffTime + last_posX;

            speedX = speedInitialX;

            speedY = speedInitialY+y*diffTime;
            posY = 1/2*y*diffTime*diffTime+speedInitialY*diffTime+last_posY;

            speedY = speedInitialY;



                posX = posX*pixelMeter;
                posY = posY*pixelMeter;

                if (posX > xmax)
                      posX = xmax;
                else if (posX < 0)
                    posX = 0;
                if(posY > ymax)
                    posY = ymax;
                else if(posY < 0)
                    posY = 0;

                star.setX(posX);
                star.setY(posY);

                last_posX = posX/pixelMeter;
                last_posY = posY/pixelMeter;

               /* if(speed > SHAKE_THRESHOLD){
                    Toast.makeText(getApplicationContext(), "Speed by Cheating:"+speed, Toast.LENGTH_LONG);
                    IntroText.setText("Shake Shake, Shake Shake, ah Shake YA!");
                }*/
           // }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    protected void onPause(){
        super.onPause();;
        senSensorManager.unregisterListener(this);
    }
    //unregister sensor when application hibernates

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAeccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    //reregister sensor when application is back in use
}

//https://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125