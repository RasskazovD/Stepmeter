package com.example.stepmeter

import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener, StepListener {

    private lateinit var sensorManager: SensorManager
    private var stepDetector: StepDetector = StepDetector()
    private lateinit var accel : Sensor

    private var numSteps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepDetector.registerListener(this)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,accel,SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            Log.d("Test","event sencor")
            stepDetector.updateAccel(
                    event.timestamp,event.values[0],event.values[1],event.values[2]
            )
        }
        textViewAccel.text = event.values[0].toString()
        textViewAccel2.text = event.values[1].toString()
        textViewAccel3.text = event.values[2].toString()
    }

    override fun step(timeNs: Long) {
        numSteps++
        textViewCounter.text = "$numSteps"
    }
}