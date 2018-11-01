package com.generals.apiclient.core.retrofit

import com.generals.apiclient.core.abstractions.APIError

class GenericAPIError(
        override val code: Int,
        override val errorBody: String
) : APIError