package com.naturalstate.poopshoot.engine

class PhysicsWorld(
    private val screenWidth: Float,
    private val screenHeight: Float
) {
    var gravity: Vector2D = Vector2D(0f, 9.8f)
    var windVector: Vector2D = Vector2D(0f, 0f)

    fun applyForce(nodeX: Float, nodeY: Float, velocity: Vector2D, delta: Float): Vector2D {
        velocity.x += (gravity.x + windVector.x) * delta
        velocity.y += (gravity.y + windVector.y) * delta

        var newX = nodeX + velocity.x * delta
        var newY = nodeY + velocity.y * delta

        if (newX < 0f || newX > screenWidth) {
            velocity.x *= -0.75f
            newX = newX.coerceIn(0f, screenWidth)
        }
        if (newY > screenHeight) {
            velocity.y *= -0.6f
            newY = screenHeight
        }

        return velocity
    }
}
