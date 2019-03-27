package me.carlosribeiro.kotlin.faktory_worker

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.DataOutputStream
import java.net.Socket
import java.net.URI
import java.util.Random
import java.util.regex.Pattern

/**
 * This class is the public client to integrate with Faktory.
 * It can send a job to Faktory.
 *
 * @property uri the URI from the Faktory Server. If empty it will use the FAKTORY_URL environment variable and if it
 * is also empty, it will use the localhost.
 */
class FaktoryClient(uri: String = System.getenv("FAKTORY_URL") ?: "tcp://localhost:7419") {
    private val connection = FaktoryConnection(uri)

    internal val uri = uri

    fun pushJob() {
        connection.connect()
        // TODO: Push the job through the socket
        connection.close()
    }
}
