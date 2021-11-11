package me.carlosribeiro.faktory_worker

import java.util.UUID

data class FaktoryJob(
    val jid: String = UUID.randomUUID().toString(),
    val jobType: String,
    val args: List<Any>
) {
    constructor(jobType: String, vararg args: Any ) : this(
        jobType = jobType,
        args =  args.toList()
    )
}