package com.zjh.livedatabus

/**
 *  desc :
 *  @author zjh
 *  on 2021/9/16
 */
interface LiveEvents {
    fun stringEvent(): LiveDataWrapper<String>
    fun integerEvent(): LiveDataWrapper<Int>
    fun userEvent(): LiveDataWrapper<User>
    fun stickyEvent(): LiveDataWrapper<String>
}