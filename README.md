# LiveData事件总线

- 项目build.gradle

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

- App build.gradle

```
dependencies {
    implementation 'com.github.LetMeOff:LiveDataBus:v1.0.0'
}
```

## 发送

- 直接使用字符串方式发送

```kotlin
LiveDataBus.instance.event("stringEvent").post("发送字符串事件")
```

- 自定义事件发送 (推荐使用，不直接传入字符串，对事件进行约束，假如A传入`eventOne`发送事件，而B以`event0ne`来订阅事件，那么此事件就永远接收不到，并且会增加排查难度。
  而且当上游删除发送事件相关代码后，订阅方也无法感知到。)

```kotlin
//发送
LiveDataBus.instance.event(LiveEvents::class.java).stringEvent().post("这是一个字符串")
LiveDataBus.instance.event(LiveEvents::class.java).integerEvent().post(123)
LiveDataBus.instance.event(LiveEvents::class.java).userEvent().post(User("小明", 18))

//LiveEvents
interface LiveEvents {
    fun stringEvent(): LiveDataWrapper<String>
    fun integerEvent(): LiveDataWrapper<Int>
    fun userEvent(): LiveDataWrapper<User>
}
```

必须使用```interface```

## 接收

- 字符串方式

```kotlin
LiveDataBus.instance.event("stringEvent").observer(this, object : ObserverWrapper<Any>() {
    override fun onChanged(value: Any?) {
        logD("接收到字符串事件 : $value")
    }
})

或

LiveDataBus.instance.event("stringEvent").observer(this) {
    logD("接收到字符串事件 : $it")
}
```

- 自定义事件

```kotlin
LiveDataBus.instance.event(LiveEvents::class.java).stringEvent().observer(this) {
    binding.stringText.text = it
}
LiveDataBus.instance.event(LiveEvents::class.java).integerEvent().observer(this) {
    binding.integerText.text = it.toString()
}
LiveDataBus.instance.event(LiveEvents::class.java).userEvent().observer(this) {
    binding.userText.text = "userName : ${it.name} , userAge : ${it.age}"
}
```

## 粘性事件

- ```observer```仅观察注册后发送的事件

- ```observerSticky```观察者注册前发送的数据也能收到

ActivityOne 发送:

```kotlin
LiveDataBus.instance.event(LiveEvents::class.java).stickyEvent().post("这是一个粘性事件")
```

ActivityTwo 接收：

```kotlin
LiveDataBus.instance.event(LiveEvents::class.java).stickyEvent().observerSticky(this) {
    binding.stickyText.text = it
}
```
