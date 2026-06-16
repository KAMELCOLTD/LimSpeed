package com.kamel.limspeed.models

import android.graphics.drawable.Drawable
import java.io.Serializable

data class AppSpeed(
    val packageName: String,
    val appName: String,
    val icon: Drawable?,
    var speedKbps: Int = 0
) : Serializable
