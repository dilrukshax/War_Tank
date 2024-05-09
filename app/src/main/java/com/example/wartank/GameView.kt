package com.example.wartank

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.util.Random

class GameView(private var context: Context) : View(context) {
    var backgrund: Bitmap
    var ground: Bitmap
    var tank: Bitmap
    var rectBackground: Rect
    var rectGround: Rect
    private var handler: Handler
    val UPDATE_MILLIS: Long = 30
    var runnable: Runnable
    var textPaint = Paint()
    var healthPaint = Paint()
    var TEXT_SIZE = 120f
    var points = 0
    var life = 3
    var random: Random
    var tankX: Float
    var tankY: Float
    var oldX = 0f
    var oldtankX = 0f
    var Missiles: ArrayList<Missile>
    var explosions: ArrayList<Explosion>

    fun getGameHandler(): Handler {
        return handler
    }


    fun getGameContext(): Context {
        return context
    }

    init {
        backgrund = BitmapFactory.decodeResource(resources, R.drawable.background)
        ground = BitmapFactory.decodeResource(resources, R.drawable.ground)
        tank = BitmapFactory.decodeResource(resources, R.drawable.tank)
        val display = (getContext() as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y
        rectBackground = Rect(0, 0, dWidth, dHeight)
        rectGround = Rect(0, dHeight - ground.height, dWidth, dHeight)
        handler = Handler()
        runnable = Runnable { invalidate() }
        textPaint.color = Color.rgb(225, 165, 0)
        textPaint.textSize = TEXT_SIZE
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.faren_height))
        healthPaint.color = Color.GREEN
        random = Random()
        tankX = (dWidth / 2 - tank.width / 2).toFloat()
        tankY = (dHeight - ground.height - tank.height).toFloat()
        Missiles = ArrayList()
        explosions = ArrayList()
        for (i in 0..2) {
            val missile = Missile(context)
            Missiles.add(missile)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(backgrund, null, rectBackground, null)
        canvas.drawBitmap(ground, null, rectGround, null)
        canvas.drawBitmap(tank, tankX, tankY, null)
        for (i in Missiles.indices) {
            canvas.drawBitmap(
                Missiles[i].getMissile(Missiles[i].MissileFrame)!!,
                Missiles[i].MissileX.toFloat(),
                Missiles[i].MissileY.toFloat(),
                null
            )
            Missiles[i].MissileFrame++
            if (Missiles[i].MissileFrame > 2) {
                Missiles[i].MissileFrame = 0
            }
            Missiles[i].MissileY += Missiles[i].MissileVelocity
            if (Missiles[i].MissileY + Missiles[i].MissileHeight >= dHeight - ground.height) {
                points += 10
                val explosion = Explosion(context)
                explosion.explosionX = Missiles[i].MissileX
                explosion.explosionY = Missiles[i].MissileY
                explosions.add(explosion)
                Missiles[i].resetPosition()
            }
        }
        val iterator = Missiles.iterator()
        while (iterator.hasNext()) {
            val Missile = iterator.next()
            if (Missile.MissileX + Missile.MissileWidth >= tankX &&
                Missile.MissileX <= tankX + tank.width &&
                Missile.MissileY + Missile.MissileHeight >= tankY &&
                Missile.MissileY + Missile.MissileHeight <= tankY + tank.height) {
                life--
                Missile.resetPosition()
                iterator.remove()  // Remove the bomb from the list
                if (life == 0) {
                    val intent = Intent(context, GameOver::class.java)
                    intent.putExtra("points", points)
                    context.startActivity(intent)
                    (context as Activity).finish()
                    break  // Exit the loop since the game is over
                }
            }
        }

        val iterator1 = explosions.iterator()
        while (iterator1.hasNext()) {
            val explosion = iterator1.next()
            val explosionBitmap = explosion.getExplosion(explosion.explosionFrame)
            if (explosionBitmap != null) {
                canvas.drawBitmap(
                    explosionBitmap,
                    explosion.explosionX.toFloat(),
                    explosion.explosionY.toFloat(),
                    null
                )
            }

            explosion.explosionFrame++
            if (explosion.explosionFrame > 3) {
                iterator1.remove() // Remove the current element using iterator
            }
        }

        if (life == 2) {
            healthPaint.color = Color.YELLOW
        } else if (life == 1) {
            healthPaint.color = Color.RED
        }
        canvas.drawRect(
            (dWidth - 200).toFloat(),
            30f,
            (dWidth - 200 + 60 * life).toFloat(),
            80f,
            healthPaint
        )
        canvas.drawText("" + points, 20f, TEXT_SIZE, textPaint)
        handler.postDelayed(runnable, UPDATE_MILLIS)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        if (touchY >= tankY) {
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.x
                oldtankX = tankX
            }
            if (action == MotionEvent.ACTION_MOVE) {
                val shift = oldX - touchX
                val newRabbitX = oldtankX - shift
                tankX =
                    if (newRabbitX <= 0) 0f else if (newRabbitX >= dWidth - tank.width) (dWidth - tank.width).toFloat() else newRabbitX
            }
        }
        return true
    }

    companion object {
        var dWidth: Int = 0
        var dHeight: Int = 0
    }
}
