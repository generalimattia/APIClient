package com.generals.apiclient.core.retrofit

import arrow.core.Either
import arrow.core.Try
import com.generals.apiclient.core.abstractions.APICall
import com.generals.apiclient.core.abstractions.APIError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitAPICall<T>(
        private val call: Call<T>
) : APICall<T> {

    companion object {
        private const val GENERIC_ERROR_CODE = 500
    }

    override fun executeSync(): Either<APIError, T?> {
        val response: Response<T> = call.execute()
        return response.fold(
                isSuccess = { body: T? -> Either.right(body) },
                isFailure = { code: Int, errorBody: String ->
                    Either.left(
                            GenericAPIError(code, errorBody)
                    )
                })
    }

    override fun executeAsync(callback: (Either<APIError, T?>) -> Unit) {

        call.enqueue(object : Callback<T> {

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback(Either.left(GenericAPIError(GENERIC_ERROR_CODE, t.message.orEmpty())))
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {

                response.fold(
                        isSuccess = { body: T? -> callback(Either.right(body)) },
                        isFailure = { code: Int, errorBody: String ->
                            callback(Either.left(
                                    GenericAPIError(code, errorBody)
                            ))
                        })
            }
        })
    }
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