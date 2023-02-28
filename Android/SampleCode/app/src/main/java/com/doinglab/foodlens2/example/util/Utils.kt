package com.doinglab.foodlens2.example.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object Utils {

    fun getBitmapFromFile(filePath: String): Bitmap? {
        val options = BitmapFactory.Options()
        return BitmapFactory.decodeFile(filePath, options)
    }

    fun readContentIntoByteArray(file: File): ByteArray {
        val fileInputStream: FileInputStream?
        val bFile = ByteArray(file.length().toInt())
        try {
            //convert file into array of bytes
            fileInputStream = FileInputStream(file)
            fileInputStream.read(bFile)
            fileInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bFile
    }

    fun cropBitmap(orgBitmap: Bitmap?, left: Int, top: Int, right: Int, bottom: Int): Bitmap? {
        val width = right - left
        val height = bottom - top

        return try{
            Bitmap.createBitmap(
                orgBitmap!!, left, top, width, height
            )
        }catch (e: Exception) {
            null
        }
    }

    fun getOrientationOfImage(filepath : String) : Int {
        var exif: ExifInterface? = null

        exif = try {
            ExifInterface(filepath)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }

        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)

        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    fun getRotationBitmap(bitmap: Bitmap?, degrees : Int) : Bitmap? {
        if (bitmap == null) return null

        if (degrees == 0) return bitmap

        val m = Matrix()
        m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }
}