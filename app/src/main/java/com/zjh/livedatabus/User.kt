package com.zjh.livedatabus

/**
 *  desc :
 *  @author zjh
 *  on 2021/9/16
 */
data class User(
    var name: String,
    var age: Int
){
    override fun toString(): String {
        return "User(name='$name', age=$age)"
    }
}