package com.example.stepmeter

class SensorMath {

    fun sum(array: FloatArray): Float {
        var retval = 0f
        for (i in array.indices) {
            retval+=array[i]
        }
        return  retval
    }

    fun norm(array: FloatArray): Float {
        var retval = 0f
        for (i in array.indices) {retval+=array[i]*array[i]}
        return retval
    }

    fun dot(arrayA: FloatArray, arrayB: FloatArray): Float {
        return arrayA[0]*arrayB[0]+arrayA[1]*arrayB[1]+arrayA[2]*arrayB[2]
    }

}