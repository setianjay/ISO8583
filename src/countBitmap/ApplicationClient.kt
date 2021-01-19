package countBitmap

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.math.BigInteger
import java.net.Socket
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

        // REQUEST
        val logonRequest:MutableMap<Int,String> = LinkedHashMap()
        logonRequest[7] = formatter.format(Date())
        logonRequest[11] = "834624"
        logonRequest[70] = "001"

        println("----------------REQUEST-------------------")

        // Set Bitmap
        val bitmapRequest  = countBitmap(logonRequest)

        // Isi dari bitmap
        // println(bitmapRequest)

        // Cek apakah binary nya sudah benar atau belum.
        /*
            - Cara men ceknya yaitu dengan menconvert nya menjadi binary dengan fungsi toString(2), yang mana arti angka 2
            tersebut merupakan base dari binary yaitu 2 (terdiri dari 1 dan 0)
         */
       println("Binary : ${bitmapRequest.toString(2)}")


        // Cek apakah hexa nya sudah benar atau belum.
        /*
            - Cara men ceknya yaitu dengan menconvert nya menjadi binary dengan fungsi toString(16), yang mana arti
            angka 16 tersebut merupakan base dari binary yaitu 16 (terdiri dari 0 dan 15)
         */
        println("Hexa : ${bitmapRequest.toString(16)}")

        // Set Message Data untuk di kirim
        val message = messageString("0800",logonRequest)
        println("Message Data : $message")

        // Menampilkan panjang dari Message Data
        val messageLength = message.length + 2
        println("Message Length : $messageLength")

        // Menampilkan Length Byte Order dari Message Data
        val baLength = ByteArray(2)
        baLength[0] = (messageLength shr 8 and 0xff).toByte()
        baLength[1] = (messageLength and 0xff).toByte()
        println("Message Length Byte Order : " + String(baLength))




        // RESPONSE
        val logonResponse:MutableMap<Int,String> = LinkedHashMap()
        logonResponse[7] = formatter.format(Date())
        logonResponse[11] = "834624"
        logonResponse[39] = "00"
        logonResponse[70] = "001"

        println("----------------RESPONSE-------------------")

        val bitmapResponse = countBitmap(logonResponse)
        // println(bitmapResponse) // Return value dari function countBitmap()

        // Binary
        println("Binary : ${bitmapResponse.toString(2)}")
        // Hexa
        println("Hexa : ${bitmapResponse.toString(16)}")

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

    private fun messageString(mti: String, message: MutableMap<Int, String>): String{
        val hasil = StringBuilder()
        hasil.append(mti)
        hasil.append(countBitmap(message).toString(16))
        for (dataElement in message.keys){
            hasil.append(message[dataElement])
        }
        return hasil.toString()
    }

    private fun send(message: String){
        val messageLength = message.length + 2
        println("Message Length : $messageLength")


        try {
            // Mengirim Data
            val koneksi = Socket("localhost",12345)
            val out = DataOutputStream(koneksi.getOutputStream())
            out.writeShort(messageLength)
            out.writeBytes(message)
            out.flush()
            println("Data terkirim")

            // Menerima response
            val inn = DataInputStream(koneksi.getInputStream())
            val respLength = inn.readShort()
            println("Panjang response : $respLength")
            val responseData = ByteArray(respLength - 2)
            inn.readFully(responseData)
            println("Response Data  : " + String(responseData))

            koneksi.close()

        }catch (e: IOException){
            e.printStackTrace();
        }

    }
}