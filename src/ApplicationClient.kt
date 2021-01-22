import helper.Iso8583Helper
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
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
    fun client(){

        // REQUEST
        val logonRequest:MutableMap<Int,String> = LinkedHashMap()
        logonRequest[7] = Iso8583Helper().formatDate()
        logonRequest[11] = "834624"
        logonRequest[70] = "001"

        println("----------------REQUEST-------------------")

        // Set Bitmap
        val bitmapRequest  = Iso8583Helper().countBitmap(logonRequest)

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
        val message = Iso8583Helper().setMessageData("0800",logonRequest)
        println("Message Data : $message")

        // Menampilkan panjang dari Message Data
        val messageLength = message.length + 2
        println("Message Length : $messageLength")

        // Menampilkan Length Byte Order dari Message Data
        val byteOrderLength = ByteArray(2)
        byteOrderLength[0] = (messageLength shr 8 and 0xff).toByte()
        byteOrderLength[1] = (messageLength and 0xff).toByte()
        println("Message Length Byte Order : " + String(byteOrderLength))




        // RESPONSE
        val logonResponse:MutableMap<Int,String> = LinkedHashMap()
        logonResponse[7] = Iso8583Helper().formatDate()
        logonResponse[11] = "834624"
        logonResponse[39] = "00"
        logonResponse[70] = "001"

        println("----------------RESPONSE-------------------")

        val bitmapResponse = Iso8583Helper().countBitmap(logonResponse)
        // println(bitmapResponse) // Return value dari function countBitmap()

        // Binary
        println("Binary : ${bitmapResponse.toString(2)}")
        // Hexa
        println("Hexa : ${bitmapResponse.toString(16)}")

        send(message,messageLength)
    }


    private fun send(message: String,messageLength: Int){
        println("----------------PORT LISTENER-------------------")



        try {
            // Mengirim Data
            val koneksi = Socket("localhost",12345)
            val out = DataOutputStream(koneksi.getOutputStream())
            out.writeShort(messageLength) // 57
            out.writeBytes(message) // Message Data
            out.flush()
            println("Data terkirim")

            // Menerima response (class Server)
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