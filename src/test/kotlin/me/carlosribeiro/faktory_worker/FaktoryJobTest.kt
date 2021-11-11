package me.carlosribeiro.faktory_worker

import org.junit.Assert
import org.junit.Test

class FaktoryJobTest {
    @Test
    fun initializesARandomJID() {
        val job = FaktoryJob("DestroyDeathStarJob", 1, "2")

        assert(job.jid.isNotEmpty())
    }

    @Test
    fun acceptsManyArguments() {
        var job = FaktoryJob("UseTheForce, Luke!", 1)


        Assert.assertTrue(job.args.contains(1))

        job = FaktoryJob("UseTheForce, Luke!", 1, 3, 6)

        Assert.assertTrue(job.args.contains(1))
        Assert.assertTrue(job.args.contains(3))
        Assert.assertTrue(job.args.contains(6))
    }
}