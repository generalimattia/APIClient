package com.generals.apiclient.core.retrofit

import arrow.core.Try
import com.generals.apiclient.core.abstractions.APIResponse
import retrofit2.Response

class RetrofitAPIResponse<T>(
        private val response: Response<T>
) : APIResponse<T> {

    override val code: Int
        get() = response.code()

    override val body: T?
        get() = response.body()
}

fun <T, R> Response<T>.fold(
        isSuccess: (T?) -> R,
        isFailure: (Int, String) -> R
): R {
    return if (isSuccessful) {
        isSuccess(body())
    } else {
        Try {
            errorBody()?.string()
        }.fold({
            isFailure(code(), "")
        }, {
            isFailure(code(), it.orEmpty())
        })
    }
}