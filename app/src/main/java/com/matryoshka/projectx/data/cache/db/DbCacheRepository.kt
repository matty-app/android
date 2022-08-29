package com.matryoshka.projectx.data.cache.db

interface DbCacheRepository<T> {
    fun get(): Collection<T>
    fun set(items: Collection<T>)
}