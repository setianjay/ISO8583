import helper.Iso8583Helper
import java.io.DataInputStream
import java.io.DataOutputStream
import java.math.BigInteger
import java.net.ServerSocket

class ApplicationServer {
    fun server(){
        val port = 12345
        val server = ServerSocket(port)
        println("Server siap di port $port")

        // Kalau ada yang koneksi ke port ini, maka tampilkan pesan
        val koneksi = server.accept()
        println("----------Client Connected----------")


        // "DataInputStream" di gunakan untuk menangkap data
        val read = DataInputStream(koneksi.getInputStream())

        // Menangkap panjang dari "Message Data" yang di kirim melalui "Class ApplicationClient"
        val length = read.readShort()
        println("Message length : $length") // 57

        // Menangkap "Message Data" yang di kirim melalui "Class ApplicationClient"
        val data = ByteArray(length - 2) // Length(57) - 2 = 55
        read.readFully(data)
        val message = String(data)
        println("Request Client : $message")

        // Mengambil nilai MTI dengan menggunakan "substring"
        val mti = message.substring(0,4)
        println("MTI : $mti")

        // Mengambil nilai BITMAP dengan menggunakan "substring"
        val strBitmap = message.substring(4,36)
        println("Bitmap Hex : $strBitmap")

        // Mengambil nilai BINARY dengan menggunakan nilai HEXA
        val binary = BigInteger(strBitmap, 16)
        println("Bitmap Bin : ${binary.toString(2)}")

        // Cara mengecek BIT
//        for (i in 0..127){
//            // testBit() berfungsi untuk mengetes bit
//            if (binary.testBit(128 - i)){
//                println("BIT $i Active")
//            }
//        }

        val logonResponse:MutableMap<Int,String> = LinkedHashMap()
        logonResponse[7] = Iso8583Helper().formatDate()
        logonResponse[11] = "834624"
        logonResponse[39] = "00"
        logonResponse[70] = "001"

        val strBitResponse = Iso8583Helper().setMessageData("0810",logonResponse)
        println("Response Data : $strBitResponse")

        // Kirim response
        val out = DataOutputStream(koneksi.getOutputStream())
        out.writeShort(strBitResponse.length + 2)
        out.writeBytes(strBitResponse)
        out.flush()
        println("----------Response Terkirim----------")
        Thread.sleep(3 * 1000)
        println("Connection Disconnect")
        koneksi.close()
    }
}