package com.generals.apiclient.core.retrofit

import arrow.core.Either
import com.generals.apiclient.core.abstractions.APICall
import com.generals.apiclient.core.abstractions.APIError
import com.generals.apiclient.core.abstractions.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitAPICall<T>(
        private val call: Call<T>
) : APICall<T> {

    companion object {
        private const val GENERIC_ERROR_CODE = 500
    }

    override fun executeSync(): Either<APIError, APIResponse<T>> =
            call.execute().fold(
                    isSuccess = { Either.right(RetrofitAPIResponse(call.execute())) },
                    isFailure = { code: Int, errorBody: String ->
                        Either.left(
                                RetrofitAPIError(code, errorBody)
                        )
                    })

    override fun executeAsync(callback: (Either<APIError, APIResponse<T>>) -> Unit) {

        call.enqueue(object : Callback<T> {

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback(Either.left(RetrofitAPIError(GENERIC_ERROR_CODE, t.message.orEmpty())))
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {

                response.fold(
                        isSuccess = { callback(Either.right(RetrofitAPIResponse(call.execute()))) },
                        isFailure = { code: Int, errorBody: String ->
                            callback(Either.left(
                                    RetrofitAPIError(code, errorBody)
                            ))
                        })
            }
        })
    }
}