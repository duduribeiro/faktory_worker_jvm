package me.carlosribeiro.kotlin.faktory_worker

import java.util.UUID

class FaktoryJob(jobType: String, vararg args: Any) {
    val jid: String = UUID.randomUUID().toString();

    internal val jobtype = jobType
    internal val args = args
}