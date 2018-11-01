package com.generals.apiclient.core.retrofit

import com.generals.apiclient.core.abstractions.APIError

class RetrofitAPIError(
        override val code: Int,
        override val errorBody: String
) : APIError