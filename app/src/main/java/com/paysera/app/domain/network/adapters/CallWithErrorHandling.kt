package com.paysera.app.domain.network.adapters

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallWithErrorHandling(
    private val delegate: Call<Any>
) : Call<Any> by delegate {

    override fun enqueue(callback: Callback<Any>) {
        delegate.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    callback.onResponse(call, response)
                } else {
                    callback.onFailure(call, Exception(response.message()))
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                callback.onFailure(call, t)
            }
        })
    }

    override fun clone() = CallWithErrorHandling(delegate.clone())
}