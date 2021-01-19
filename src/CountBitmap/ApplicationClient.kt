package CountBitmap

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date
import java.math.BigInteger
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap

class ApplicationClient {
    /*
            "setBit" befungsi untuk men set bit pada posisi yand di inginkan.
            Misal kita ingin men set posisi binary ke 7 menjadi 1(True) maka dengan function setBit(7) dapat merubah
            posisi binary ke 7 yang tadinya 0 menjadi 1.

            "BigInteger" tidak bisa di isi valuenya seperti Integer biasa.
            Misal "val bitmap: BigInteger = 0" itu tidak bisa atau error, kita harus mengisi valuenya
            berdasarkan function yang ada di dalam BigInteger seperti "Zero, One, Ten".
     */
    fun main(vararg params: String){
        val formatter: DateFormat = SimpleDateFormat("MMddHHmmss")

        var logonRequest:MutableMap<Int,String> = LinkedHashMap()
        logonRequest[7] = formatter.format(Date())
        logonRequest[11] = "834624"
        logonRequest[70] = "001"

        // Set Bitmap
        val bitmapRequest  = countBitmap(logonRequest)
        println(bitmapRequest)

        // REQUEST
        // Convert bitmap to Binary
        val strBitToBin = bitmapRequest.toString(2)
        println("Request Binary : $strBitToBin")

        // Convert bitmap to hexa
        val strBitToHex = bitmapRequest.toString(16)
        println("Request Hexa : $strBitToHex")

        // RESPONSE
        val logonResponse:MutableMap<Int,String> = LinkedHashMap()
        logonResponse[7] = formatter.format(Date())
        logonResponse[11] = "834624"
        logonResponse[39] = "00"
        logonResponse[70] = "001"
        val bitmapResponse = countBitmap(logonResponse)
        println(bitmapResponse)

        val strBitResToBin = bitmapResponse.toString(2)
        println("Response Binary : $strBitResToBin")

        val strBitResToHexa = bitmapResponse.toString(16)
        println("Response Hexa : $strBitResToHexa")

    }

    private fun countBitmap(message: MutableMap<Int,String>): BigInteger{
        var bitmap: BigInteger = BigInteger.ZERO.setBit(128 - 1)

        for (dataElement in message.keys){
            bitmap = if (dataElement > 64){
                bitmap.setBit(128 - dataElement)
            }else{
                bitmap.setBit(128 - dataElement)
            }
        }
        return bitmap
    }
}