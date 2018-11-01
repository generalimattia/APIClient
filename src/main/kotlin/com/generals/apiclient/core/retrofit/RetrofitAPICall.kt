package com.generals.apiclient.core.retrofit

import arrow.core.Either
import arrow.core.Try
import com.generals.apiclient.core.abstractions.APICall
import com.generals.apiclient.core.abstractions.APIError
import com.generals.apiclient.core.abstractions.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitAPICall<T>(
        private val call: Call<T>
) : APICall<T> {

    override fun execute(): Either<APIError, APIResponse<T>> {

        val response: Response<T> = call.execute()

        return if (response.isSuccessful) {
            Either.right(RetrofitAPIResponse(response))
        } else {

            Try {
                Either.left(
                        RetrofitAPIError(response.code(),
                                response.errorBody()?.string() ?: "")
                )
            }.fold({
                Either.left(
                        RetrofitAPIError(response.code(), "")
                )
            }, {
                it
            })
        }

    }

    override fun enqueue(callback: (Either<APIError, APIResponse<T>>) -> Unit) {

        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                callback(Either.left(RetrofitAPIError(500, t.message.orEmpty())))
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback(Either.right(RetrofitAPIResponse(response)))
                } else {

                    Try {
                        Either.left(
                                RetrofitAPIError(response.code(),
                                        response.errorBody()?.string() ?: "")
                        )
                    }.fold({
                        callback(Either.left(
                                RetrofitAPIError(response.code(), "")
                        ))
                    }, {
                        callback(it)
                    })
                }
            }
        })
    }
}