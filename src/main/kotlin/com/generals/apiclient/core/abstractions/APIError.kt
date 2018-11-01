package com.generals.apiclient.core.abstractions

interface APIError {

    val code: Int

    val errorBody: String
}