package com.example.inc42reader.utils

data class MyResponse<out T>
    (val status:Status,
     val data:T?=null,
     val message:String?=null) {

        enum class Status{
            LODING,SUCCESS,ERROR
        }
    companion object{
        fun <T> loading():MyResponse<T>{
            return MyResponse(Status.LODING)
        }

        fun <T> success(data:T):MyResponse<T>{
            return MyResponse(Status.SUCCESS,data)
        }

        fun <T> error(errMsg:String):MyResponse<T>{
            return MyResponse(Status.ERROR, message = errMsg)
        }

    }
}