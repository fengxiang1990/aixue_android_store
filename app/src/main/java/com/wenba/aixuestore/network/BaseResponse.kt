package com.wenba.aixuestore.network

class BaseResponse<T> {


    constructor()

    constructor(code: Int, msg: String) {
        this.code = code
        this.message = msg
    }

    var code: Int = 0

    var message: String? = null

    var data: T? = null

    override fun toString(): String {
        return "BaseResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }
}