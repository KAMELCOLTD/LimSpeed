package com.kamel.limspeed

import java.util.concurrent.atomic.AtomicLong

class TrafficShaper(private val limitKbps: Int) {
    private val lastTime = AtomicLong(System.currentTimeMillis())
    private val bucket = AtomicLong(0L)
    private val maxBucket = (limitKbps * 1024 * 2L) / 8 // 2 seconds buffer

    @Synchronized
    fun allowPacket(sizeBytes: Int): Boolean {
        val now = System.currentTimeMillis()
        val elapsed = (now - lastTime.get()) / 1000.0
        
        // Add tokens to bucket
        val tokensToAdd = (elapsed * limitKbps * 1024 / 8).toLong()
        bucket.addAndGet(tokensToAdd)
        
        // Cap bucket size
        if (bucket.get() > maxBucket) {
            bucket.set(maxBucket)
        }
        
        lastTime.set(now)
        
        // Check if enough tokens
        return if (bucket.get() >= sizeBytes) {
            bucket.addAndGet(-sizeBytes.toLong())
            true
        } else {
            false
        }
    }
}
