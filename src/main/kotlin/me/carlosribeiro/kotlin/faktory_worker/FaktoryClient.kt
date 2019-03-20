package me.carlosribeiro.kotlin.faktory_worker

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.Socket
import java.net.URI
import java.util.regex.Pattern
import java.util.*


/**
 * This class is used to be the client between the application and Faktory.
 * It connects into Faktory using a Socket.
 *
 * @property uri the URI from the Faktory Server. If empty it will use the FAKTORY_URL environment variable and if it
 * is also empty, it will use the localhost.
 */
class FaktoryClient(uri: String = System.getenv("FAKTORY_URL") ?: "tcp://localhost:7419") {
    internal var inFromServer: BufferedReader? = null
    internal var outToServer: DataOutputStream? = null
    internal var socket: Socket? = null
    internal val uri = URI(uri)


    private val HI_RECEIVED = Pattern.compile("\\+HI\\s\\{\\\"v\\\"\\:\\d\\}")
    private val OK_RECEIVED = Pattern.compile("\\+OK")
    private val WID = createRandomWID()

    internal constructor(socket: Socket) : this() {
        this.socket = socket
    }

    fun pushJob() {
        connect()
        //TODO: Push the job through the socket
        close()
    }

    /**
     * Method used to connect to Faktory using a Socket.
     */
    internal fun connect() {
        val socket = openSocket()
        inFromServer = BufferedReader(InputStreamReader(socket.getInputStream()))
        outToServer = DataOutputStream(socket.getOutputStream())
        if (!HI_RECEIVED.matcher(readFromSocket()).matches()) {
            throw FaktoryConnectionError()
        }

        writeToSocket(command("HELLO", object : HashMap<String, Any>() {
            init {
                put("wid", WID)
            }
        }))
        if (!OK_RECEIVED.matcher(readFromSocket()).matches()) {
            throw FaktoryConnectionError()
        }
    }

    private fun close() {
        socket?.close()
    }

    private fun openSocket(): Socket {
        return socket ?: Socket(uri.host, uri.port)
    }

    private fun command(command: String, arguments: HashMap<String, Any>): String {
        var argsInJson: String? = null
        try {
            argsInJson = ObjectMapper().writeValueAsString(arguments)
            return "$command $argsInJson"
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            return command
        }

    }

    private fun readFromSocket() = inFromServer?.readLine()
    private fun writeToSocket(content: String) = outToServer?.writeBytes(content + "\n")
    private fun createRandomWID(): String {
        val wid_length = 8
        val random = Random()
        val sb = StringBuilder()
        while (sb.length < wid_length) {
            sb.append(Integer.toHexString(random.nextInt()))
        }
        return sb.toString()
    }
}
