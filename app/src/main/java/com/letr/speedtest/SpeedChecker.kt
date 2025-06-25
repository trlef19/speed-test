package com.letr.speedtest

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.URI
import java.net.URL
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis


class SpeedChecker() {
    // Measure download speed (returns speed in Mbps)
    suspend fun measureDownloadSpeed(): Float {
       return withContext(Dispatchers.IO) {
            try {
                val testUrl = "https://nbg1-speed.hetzner.com/100MB.bin"
                val startTime = System.currentTimeMillis()
                val connection = URL(testUrl).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                connection.connect()

                val inputStream: InputStream = connection.inputStream
                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalBytesRead = 0L

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    totalBytesRead += bytesRead

                    // Stop test after 10 seconds to avoid excessive data usage
                    if (System.currentTimeMillis() - startTime > 10000) break
                }

                val endTime = System.currentTimeMillis()

                // Convert to seconds
                val duration = (endTime - startTime) / 1000.0

                inputStream.close()
                connection.disconnect()

                // Calculate speed in Mbps (Megabits per second)
                val speedMbps = ((totalBytesRead * 8) / 1000000.0) / duration
                // Round to 2 decimal places
                 (speedMbps * 100).roundToInt() / 100.0f
            } catch (e: Exception) {
                e.printStackTrace()
                0.0f
            }
        }
    }

    // Measure upload speed (returns speed in Mbps)
    suspend fun measureUploadSpeed(testUrl: String = "https://httpbin.org/post"): Double = withContext(Dispatchers.IO) {
        try {
            val dataSize = 1000000 // 1MB

            // Fill with 'a'
            val data = ByteArray(dataSize) { 0x61 }

            val startTime = System.currentTimeMillis()
            val connection = URL(testUrl).openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.connect()

            val outputStream: OutputStream = connection.outputStream
            outputStream.write(data)
            outputStream.flush()
            outputStream.close()

            // Read response to complete the request
            val inputStream: InputStream = connection.inputStream
            val buffer = ByteArray(8192)
            while (inputStream.read(buffer) != -1) { /* Just consuming the response */ }

            val endTime = System.currentTimeMillis()

            // Convert to seconds
            val duration = (endTime - startTime) / 1000.0

            inputStream.close()
            connection.disconnect()

            // Calculate speed in Mbps (Megabits per second)
            val speedMbps = ((dataSize * 8) / 1000000.0) / duration
            (speedMbps * 100).roundToInt() / 100.0
            // Round to 2 decimal places

        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }

    // Get network quality based on download speed
    fun getNetworkQuality(downloadSpeedMbps: Double): String {
        return when {
            downloadSpeedMbps <= 0 -> "No Connection"
            downloadSpeedMbps < 1 -> "Poor"
            downloadSpeedMbps < 5 -> "Fair"
            downloadSpeedMbps < 10 -> "Good"
            downloadSpeedMbps < 30 -> "Very Good"
            else -> "Excellent"
        }
    }
}
