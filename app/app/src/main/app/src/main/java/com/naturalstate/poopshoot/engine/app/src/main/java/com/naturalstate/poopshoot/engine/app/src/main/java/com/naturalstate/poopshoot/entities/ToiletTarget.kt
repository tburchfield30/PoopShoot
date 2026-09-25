package com.naturalstate.poopshoot.entities

class ToiletTarget(var x: Float, var y: Float, var width: Float, var height: Float) {
    var speedX: Float = 4.0f

    fun update(screenWidth: Float) {
        x += speedX
        if (x < 0f || x + width > screenWidth) {
            speedX *= -1f
            x = x.coerceIn(0f, screenWidth - width)
        }
    }

    fun checkCollision(projX: Float, projY: Float): Boolean {
        return projX in x..(x + width) && projY in y..(y + height)
    }
}
