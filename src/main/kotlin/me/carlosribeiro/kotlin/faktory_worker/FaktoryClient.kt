package me.carlosribeiro.kotlin.faktory_worker

import com.google.gson.Gson

/**
 * This class is the public client to integrate with Faktory.
 * It can send a job to Faktory.
 *
 * @property uri the URI from the Faktory Server. If empty it will use the FAKTORY_URL environment variable and if it
 * is also empty, it will use the localhost.
 */
class FaktoryClient(uri: String = System.getenv("FAKTORY_URL") ?: "tcp://localhost:7419", internal var connection: FaktoryConnection = FaktoryConnection(uri)) {
    internal val uri = uri

    fun pushJob(job: FaktoryJob) {
        connection.connect()
        var payload = Gson().toJson(job)
        connection.send("PUSH $payload")
        connection.close()
    }
}
