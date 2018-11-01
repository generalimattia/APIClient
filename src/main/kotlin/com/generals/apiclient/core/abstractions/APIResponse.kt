package com.generals.apiclient.core.abstractions

interface APIResponse<T> {

    val code: Int

    val body: T?
}