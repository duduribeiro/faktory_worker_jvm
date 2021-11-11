package me.carlosribeiro.faktory_worker

import io.mockk.every
import io.mockk.mockkClass
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.Socket

class FaktoryConnectionTest {
    private val connection = FaktoryConnection("tcp://localhost:7419")
    private val mockedSocket = mockkClass(Socket::class)

    @Test
    fun whenFaktorySendsHiClientShouldSendHello() {
        val fromServer = ByteArrayInputStream("+HI {\"v\":2}\n+OK".toByteArray())
        val toServer = ByteArrayOutputStream()
        every { mockedSocket.getInputStream() } returns fromServer
        every { mockedSocket.getOutputStream() } returns toServer
        connection.socket = mockedSocket

        connection.connect()

        Assert.assertThat(toServer.toString(), CoreMatchers.containsString("HELLO"))
    }

    @Test(expected = FaktoryConnectionError::class)
    fun raisesErrorWhenFaktoryDoesNotSendHi() {
        val fromServer = ByteArrayInputStream("NOT HI".toByteArray())
        val toServer = ByteArrayOutputStream()
        every { mockedSocket.getInputStream() } returns fromServer
        every { mockedSocket.getOutputStream() } returns toServer
        connection.socket = mockedSocket

        connection.connect()
    }
}