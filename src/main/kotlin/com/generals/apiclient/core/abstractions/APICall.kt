package com.generals.apiclient.core.abstractions

import arrow.core.Either

interface APICall<T> {

    fun executeSync(): Either<APIError, APIResponse<T>>

    fun executeAsync(callback: (Either<APIError, APIResponse<T>>) -> Unit)

}