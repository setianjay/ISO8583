package helper

import java.lang.StringBuilder
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*

class Iso8583Helper {


    fun formatDate(): String{
        val formatter = SimpleDateFormat("MMddHHmmss")
        return formatter.format(Date())
    }

    fun countBitmap(message:MutableMap<Int,String>):BigInteger {
        var bitmap = BigInteger.ZERO.setBit(128 - 1)
        for (dataElement in message.keys){
            bitmap = if (dataElement > 64){
                bitmap.setBit(128 - dataElement)
            }else{
                bitmap.setBit(128 - dataElement)
            }
        }
        return bitmap
    }

    fun setMessageData(mti: String,message: MutableMap<Int, String>):String{
    val result = StringBuilder()
        result.append(mti)
        result.append(countBitmap(message).toString(16))

        for (dataElement in message.keys){
            result.append(message[dataElement])
        }

        return result.toString()
    }
}