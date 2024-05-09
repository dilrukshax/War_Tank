package com.example.wartank

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.Random

class Missile(context: Context) {
    var Missile = arrayOfNulls<Bitmap>(3)
    var MissileFrame = 0
    var MissileX = 0
    var MissileY = 0
    var MissileVelocity = 0
    var random: Random

    init {
        Missile[0] = BitmapFactory.decodeResource(context.resources, R.drawable.missile0)
        Missile[1] = BitmapFactory.decodeResource(context.resources, R.drawable.missile1)
        Missile[2] = BitmapFactory.decodeResource(context.resources, R.drawable.missile2)
        random = Random()
        resetPosition()
    }

    fun getMissile(MissileFrame: Int): Bitmap? {
        return Missile[MissileFrame]
    }

    val MissileWidth: Int
        get() = Missile[0]!!.width
    val MissileHeight: Int
        get() = Missile[0]!!.height

    fun resetPosition() {
        MissileX = random.nextInt(GameView.dWidth - MissileWidth)
        MissileY = -200 + random.nextInt(600) * -1
        MissileVelocity = 35 + random.nextInt(16)
    }
}
