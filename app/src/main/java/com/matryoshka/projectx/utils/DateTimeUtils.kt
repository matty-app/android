package com.matryoshka.projectx.utils

import java.time.Duration

//standard functions Duration.toHoursPart() and Duration.toMinutesPart() require API 31
val Duration.hoursPart: Long
    get() = this.toHours() % 24

val Duration.minutesPart: Long
    get() = this.toMinutes() % 60