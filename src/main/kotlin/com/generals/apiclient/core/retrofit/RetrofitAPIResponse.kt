package com.generals.apiclient.core.retrofit

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
