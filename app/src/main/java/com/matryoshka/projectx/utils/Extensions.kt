package com.matryoshka.projectx.utils

fun <T> Iterable<T>.exists(element: T) = this.indexOf(element) != -1