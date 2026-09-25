package com.naturalstate.poopshoot.entities

import kotlin.math.sqrt

class PipeObstacle(
    var x: Float,
    var y: Float,
    var length: Float,
    var rotationSpeed: Float
) {
    var angle: Float = 0f

    fun update() {
        angle += rotationSpeed
        if (angle >= 360f) angle = 0f
    }

    fun checkCollision(projX: Float, projY: Float): Boolean {
        val distance = sqrt((projX - x) * (projX - x) + (projY - y) * (projY - y))
        return distance < (length / 2.5f)
    }
}
