package com.generals.apiclient.core.abstractions

import arrow.core.Either

interface APICall<T> {

    fun execute(): Either<APIError, APIResponse<T>>

    fun enqueue(callback: (Either<APIError, APIResponse<T>>) -> Unit)

}