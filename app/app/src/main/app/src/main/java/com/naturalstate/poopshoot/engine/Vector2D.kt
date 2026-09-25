package com.naturalstate.poopshoot.engine

data class Vector2D(var x: Float, var y: Float) {
    fun add(v: Vector2D) { x += v.x; y += v.y }
    fun mult(scalar: Float) { x *= scalar; y *= scalar }
}
