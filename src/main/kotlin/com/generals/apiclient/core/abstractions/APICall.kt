package com.generals.apiclient.core.abstractions

import arrow.core.Either

interface APICall<T> {

    fun executeSync(): Either<APIError, T?>

    fun executeAsync(callback: (Either<APIError, T?>) -> Unit)

}