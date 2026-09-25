package com.naturalstate.poopshoot.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceView
import com.naturalstate.poopshoot.engine.PhysicsWorld
import com.naturalstate.poopshoot.engine.Vector2D
import com.naturalstate.poopshoot.entities.ToiletTarget

class GameView(context: Context) : SurfaceView(context), Runnable {
    private var drawingThread: Thread? = null
    private var isPlaying = false
    private val paint = Paint()
    
    private val physicsWorld = PhysicsWorld(1080f, 1920f)
    private val toilet = ToiletTarget(400f, 200f, 280f, 120f)
    
    private var nodePos = Vector2D(540f, 1800f)
    private var nodeVelocity = Vector2D(0f, 0f)
    private var isLaunched = false
    private var score = 0

    init { paint.isAntiAlias = true }

    fun resume() {
        isPlaying = true
        drawingThread = Thread(this)
        drawingThread?.start()
    }

    fun pause() {
        isPlaying = false
        try { drawingThread?.join() } catch (e: InterruptedException) { e.printStackTrace() }
    }

    override fun run() {
        var lastTime = System.nanoTime()
        while (isPlaying) {
            val currentTime = System.nanoTime()
            val delta = (currentTime - lastTime) / 1_000_000_000.0f
            lastTime = currentTime

            toilet.update(1080f)

            if (isLaunched) {
                nodeVelocity = physicsWorld.applyForce(nodePos.x, nodePos.y, nodeVelocity, delta)
                nodePos.x += nodeVelocity.x * delta * 60f
                nodePos.y += nodeVelocity.y * delta * 60f

                if (toilet.checkCollision(nodePos.x, nodePos.y)) {
                    score += 100
                    resetShot()
                    toilet.speedX *= 1.15f
                }

                if (nodePos.y < 0f || nodePos.y > 1920f) { resetShot() }
            }

            if (holder.surface.isValid) {
                val canvas: Canvas = holder.lockCanvas()
                canvas.drawColor(Color.parseColor("#1a1a1a"))

                paint.color = Color.parseColor("#FFFFFF")
                canvas.drawRect(toilet.x, toilet.y, toilet.x + toilet.width, toilet.y + toilet.height, paint)

                paint.color = Color.parseColor("#8D5524")
                canvas.drawCircle(nodePos.x, nodePos.y, 40f, paint)

                paint.color = Color.parseColor("#00E676")
                paint.textSize = 64f
                canvas.drawText("Score: $score", 50f, 150f, paint)

                holder.unlockCanvasAndPost(canvas)
            }
        }
    }

    private fun resetShot() {
        isLaunched = false
        nodePos.x = 540f
        nodePos.y = 1800f
        nodeVelocity.x = 0f
        nodeVelocity.y = 0f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isLaunched) {
                    nodeVelocity.x = (event.x - nodePos.x) * 0.12f
                    nodeVelocity.y = (event.y - nodePos.y) * 0.12f
                    isLaunched = true
                }
            }
        }
        return true
    }
}
