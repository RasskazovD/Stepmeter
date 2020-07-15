package com.example.stepmeter

import android.util.Log
import kotlin.math.min

class StepDetector {

    private val ACCEL_RING_SIZE = 50
    private val VEL_RING_SIZE = 10
    private val STEP_THRESHOLD = -600f
    private val STEP_DELAY_NS = 250000000

    lateinit var listener: StepListener
    private var accelRingCounter = 0
    private var velRingCounter = 0
    private var accelRingX = FloatArray(ACCEL_RING_SIZE)
    private var accelRingY = FloatArray(ACCEL_RING_SIZE)
    private var accelRingZ = FloatArray(ACCEL_RING_SIZE)
    private var velRing = FloatArray(VEL_RING_SIZE)
    private var oldVelocityEstimate = 0f
    private var lastStepTimeNs: Long = 0
    private val sensorMath = SensorMath()

    fun registerListener(listener: StepListener) {
        this.listener = listener
    }

    fun updateAccel(timeNS: Long, x: Float, y: Float, z: Float) {

        val currentAccel = floatArrayOf(x,y,z)

        accelRingCounter++

        accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0]
        accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1]
        accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2]

        val worldZ = FloatArray(3)
        worldZ[0] = sensorMath.sum(accelRingX)/ min(accelRingCounter, ACCEL_RING_SIZE)
        worldZ[1] = sensorMath.sum(accelRingY)/ min(accelRingCounter, ACCEL_RING_SIZE)
        worldZ[2] = sensorMath.sum(accelRingZ)/ min(accelRingCounter, ACCEL_RING_SIZE)

        val normalization_factor = sensorMath.norm(worldZ)

        worldZ[0] = worldZ[0] / normalization_factor
        worldZ[1] = worldZ[1] / normalization_factor
        worldZ[2] = worldZ[2] / normalization_factor

        var currentZ = sensorMath.dot(worldZ,currentAccel) - normalization_factor
        velRingCounter++
        velRing[velRingCounter % VEL_RING_SIZE] = currentZ

        val velocityEstimate = sensorMath.sum(velRing)

        Log.d("Test","$velocityEstimate")

        if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD && (timeNS - lastStepTimeNs > STEP_DELAY_NS)) {
            listener.step(timeNS)
            lastStepTimeNs = timeNS
        }
        oldVelocityEstimate = velocityEstimate
    }
}