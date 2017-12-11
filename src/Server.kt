import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.io.FileInputStream

object Server {

    @Throws(Throwable::class)
    @JvmStatic
    fun main(args: Array<String>)
    {
        val ss = ServerSocket(8080)
        while (true)
        {
            val s = ss.accept()
            System.out.println("Client accepted")
            Thread(SocketProcessor(s)).start()
        }
    }

    private class SocketProcessor @Throws(Throwable::class)
    public constructor(val s: Socket) : Runnable
    {
        val ins: InputStream
        val outs: OutputStream

        init
        {
            this.ins = s.getInputStream()
            this.outs = s.getOutputStream()
        }

        override fun run()
        {
            try
            {
                readInputHeaders()
                var text = getStringFromFile("C:/Users/Мария/Desktop/учеба/Лабы Java/WebServer/src/index.html");
                writeResponse(text)
            }
            catch (t: Throwable)
            {
                /*do nothing*/
            }
            finally
            {
                try
                {
                    s.close()
                } catch (t: Throwable) {
                    /*do nothing*/
                }

            }
            System.out.println("Client processing finished")
        }

        private fun getStringFromFile(path: String): String
        {
            val inFile = FileInputStream(path)
            val str = ByteArray(inFile.available())
            inFile.read(str)
            return String(str)
        }
        @Throws(Throwable::class)
        private fun writeResponse(s: String)
        {
            val response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length + "\r\n" +
                    "Connection: close\r\n\r\n"
            val result = response + s
            outs.write(result.toByteArray())
            outs.flush()
        }

        @Throws(Throwable::class)
        private fun readInputHeaders()
        {
            val br = BufferedReader(InputStreamReader(ins))
            while (true) {
                val s = br.readLine()
                if (s == null || s.trim { it <= ' ' }.length == 0) {
                    break
                }
            }
        }
    }
}