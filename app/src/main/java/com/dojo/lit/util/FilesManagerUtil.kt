package com.dojo.lit.util

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class FilesManagerUtil {

    fun readFile(filePath: String): String? {
        //        filePath = normalisePath(filePath);
        try {
            val `is` = FileInputStream(filePath)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            return String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            return null
        }

    }

    @Throws(IOException::class)
    fun writeToFile(filePath: String, response: ByteArray) {
        //        filePath = normalisePath(filePath);
        val outputStream = FileOutputStream(filePath)
        outputStream.write(response)
        outputStream.close()
    }

    fun writeToFileLarge(filePath: String, str: String?) {
        if (str == null) return
        var startPointer = 0
        val strLen = str.length
        val MAX_LENGTH_ALLOWED = 4000
        try {
            //            filePath = normalisePath(filePath);
            val outputStream = FileOutputStream(filePath)
            while (startPointer < strLen) {
                var endPointer = startPointer + MAX_LENGTH_ALLOWED
                if (endPointer > strLen) endPointer = strLen
                startPointer = endPointer
                outputStream.write(str.substring(startPointer, endPointer).toByteArray())
            }
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
