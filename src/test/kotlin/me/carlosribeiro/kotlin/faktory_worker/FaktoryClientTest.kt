package me.carlosribeiro.kotlin.faktory_worker

import io.mockk.*
import org.junit.Assert
import org.junit.Test
import org.junit.contrib.java.lang.system.EnvironmentVariables
import org.junit.Rule

class FaktoryClientTest {
    private val client = FaktoryClient()

    @Rule @JvmField
    val environmentVariables = EnvironmentVariables()

    @Test
    fun initializeClientWithoutUriAndWithoutEnvVars() {
        Assert.assertEquals("tcp://localhost:7419", client.uri.toString())
    }

    @Test
    fun initializeClientWithoutUriButWithEnvVars() {
        environmentVariables.set("FAKTORY_URL", "tcp://192.168.0.2:7419")
        val anotherClient = FaktoryClient()

        Assert.assertEquals("tcp://192.168.0.2:7419", anotherClient.uri.toString())
    }

    @Test
    fun initializeClientWithACustomUri() {
        val client = FaktoryClient("tcp://192.168.0.1:7419")

        Assert.assertEquals("tcp://192.168.0.1:7419", client.uri.toString())
    }

    @Test
    fun willSendAJobWithTheRighParamsToFaktory() {
        val connection = mockk<FaktoryConnection>()
        val client = FaktoryClient(connection = connection)
        val job = FaktoryJob("FindTheRebels", 1, "luke", "leia")

        every { connection.connect() } just Runs
        every { connection.send("PUSH {\"jid\":\"${job.jid}\",\"jobtype\":\"FindTheRebels\",\"args\":[1,\"luke\",\"leia\"]}")} just Runs
        every { connection.close() } just Runs

        client.pushJob(job)
    }
}