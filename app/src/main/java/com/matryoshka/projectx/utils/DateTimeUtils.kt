package com.matryoshka.projectx.utils

import java.time.Duration

//standard functions Duration.to..Part() require API 31
val Duration.hoursPart: Int
    get() = (this.toHours() % 24).toInt()

val Duration.minutesPart: Int
    get() = (this.toMinutes() % 60).toInt()