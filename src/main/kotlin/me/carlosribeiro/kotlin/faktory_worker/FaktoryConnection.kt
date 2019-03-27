package me.carlosribeiro.kotlin.faktory_worker

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.DataOutputStream
import java.net.Socket
import java.net.URI
import java.util.regex.Pattern

/**
 * This class is used to handle connections with Faktory.
 * It connects into Faktory using a Socket.
 *
 * @property uri the URI from the Faktory Server.
 */
class FaktoryConnection(uri: String) {
    internal var socket: Socket? = null
    internal val uri = URI(uri)

    private val HI_RECEIVED = Pattern.compile("\\+HI\\s\\{\\\"v\\\"\\:\\d\\}")
    private val OK_RECEIVED = Pattern.compile("\\+OK")

    /**
     * Method used to connect to Faktory using a Socket.
     */
    internal fun connect() {
        val socket = openSocket()
        val fromServer = socket.getInputStream().bufferedReader()
        val toServer = DataOutputStream(socket.getOutputStream())
        if (!HI_RECEIVED.matcher(readFromSocket(fromServer)).matches()) {
            throw FaktoryConnectionError()
        }

        writeToSocket(toServer, "HELLO {\"v\":2}")

        if (!OK_RECEIVED.matcher(readFromSocket(fromServer)).matches()) {
            throw FaktoryConnectionError()
        }
    }

    internal fun close() {
        socket?.close()
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

    private fun openSocket(): Socket {
        return socket ?: Socket(uri.host, uri.port)
    }

    private fun readFromSocket(fromServer: BufferedReader) = fromServer.readLine()

    private fun writeToSocket(toServer: DataOutputStream, content: String) = toServer.writeBytes(content + "\n")
}
