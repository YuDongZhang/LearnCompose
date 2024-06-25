package com.example.learncompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learncompose.ui.theme.LearnComposeTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnComposeTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()){ innerPadding->
//                    Test()
//                    modifier = Modifier.padding(innerPadding)
//                }
            }

        }
    }
}

//Compose的UI刷新就是重新调用一次可组合函数，而且调用的次数和时机是不确定的
@Composable
fun Test1() {
    val a = "测试"
    Text(text = a)
}


/*函数幂等 ， 假设你的函数改变了外部的变量或者访问了外部的变量，那它就不是幂等的，
var a:Int=1

fun nonIdempotent(){
    a++
}

每次调用结果不一样 ， 不幂等
fun idempotent(a:Int):Int{
    return a+1
}

fun idempotentOrNonIdempotent(a:Int):Int{
    print("${a+1}")
    return a+1
}
也许你会觉得这是幂等的，因为结果是一致的，但是这个函数却是不幂等的，因为print会对控制台输出日志，
这属于对函数外部产生了影响，而对外部产生影响属于附带效应，因此也是不幂等的。

Compose的UI刷新就是重新调用一次可组合函数，而且调用的次数和时机是不确定的
 */
@Preview
@Composable
fun testPreview() {
    Test1()
}

/*
二、重组与智能重组
@Composable
fun ClickCounter(clicks: Int, onClick: () -> Unit) {
    Button(onClick = onClick) {
    Text("I've been clicked $clicks times")
 }
}

就是传入参数没有变化就不刷新，就是提高效率
 */

/*
三、Compose的生命周期
组合中可组合项的生命周期。 进入组合，执行 0 次或多次重组，然后退出组合。

每一次composable（重组）就是调用一次可组合函数
 */


/*
四、remember与状态
由于 Compose 是声明式工具集，因此更新它的唯一方法是通过新参数调用同一可组合项。
这些参数是界面状态的表现形式。每当状态更新时，都会发生重组。

有时候，我们希望某个remember变量在恰当的时候发生变化，例如int类型的变量num变化的时候，自动生成对应的字符串，我们可以使用remember的key，当key发生变化的时候，remember的变量会重新生成。
kotlin复制代码

var num by remember { mutableStateOf(0) }
val numString = remember(key1=num) {"我是数字$num"}

上述案例中，numString是受num影响的，如果num不变的情况下，numString取的值永远都是上一次生成的值，一旦num发生了变化，即remember中的key值变化，那么remember的lambda会重新执行来获取新值。

 */

@Composable
fun Test2() {
    var a = remember {
        "每次都是我"
    }
    Text(text = a)
}

@Preview
@Composable
fun testPreview2() {
    Test2()
}


/*
2.MutableState

mutableStateOf 会创建可观察的 MutableState，后者是与 Compose 运行时集成的可观察类型

interface MutableState<T> : State<T> {
     override var value: T
}


如果 value 有任何变化，系统就会为用于读取 value 的所有可组合函数安排重组。
在可组合项中声明 MutableState 对象的方法有三种：

val mutableState = remember { mutableStateOf(default) }
var value by remember { mutableStateOf(default) }（实际中最多使用）
val (value, setValue) = remember { mutableStateOf(default) }

这些声明是等效的，以语法糖的形式针对状态的不同用法提供。您选择的声明应该能够在您编写的可组合项中生成
可读性最高的代码。

简单来说，MutableState对象的作用就是一种可以被Compose观察其变化的对象，当一个MutableState
变化的时候，这个对象所在的所有重组作用域都会进入重组。
*关于重组作用域的概念此处不展开，你可以大致理解为MutableState所在的那个可组合函数
通常MutableState是和remember一起出现的，下面演示一个组件：
kotlin复制代码

@Composable
fun MyButton(){

    var num:Int by remember{ mutableStateOf(0) }

    Column{
        Button(onClick = { num++ }) {
            Text("点我加一")
        }
        Text("当前点击次数：$num")
    }
}

很容易看出来，这是一个竖向的布局，上面是一个按钮，点击之后，会让num变量+1，然后触发重组，
导致其下面的Text的显示内容也+1。
可能很多初学者看到num的类型是Int很奇怪，会奇怪为什么Int的类型变化会导致重组，不是说只有
MutableState变化才会触发重组吗，这是由于使用了by这个操作符对MutableState进行了委托，
num的get和set方法本质上是修改了MutableState的内部的value。
我们可以去除掉by操作符，代码会变成这样，本质是一样的：
kotlin复制代码

@Composable
fun MyButton(){

    val num: MutableState<Int> = remember{ mutableStateOf(0) }

    Column{
        Button(onClick = { num.value++ }) {
            Text("点我加一")
        }
        Text("当前点击次数：${num.value}")
    }


}

 */

@Composable
fun MyButton() {

    var num: Int by remember { mutableStateOf(0) }

    Column {
        Button(onClick = { num++ }) {
            Text("点我加一")
        }
        Text("当前点击次数：$num")
    }
}

@Preview
@Composable
fun testBt() {
    MyButton()
}

@Composable
fun MyButton2() {

    val num: MutableState<Int> = remember { mutableStateOf(0) }

    Column {
        Button(onClick = { num.value++ }) {
            Text("点我加一")
        }
        Text("当前点击次数：${num.value}")
    }

}

@Preview
@Composable
fun testBt2() {
    MyButton2()
}

/*
1.LaunchedEffect：在某个可组合项的作用域内运行挂起函数
使用场景：希望在一个组合内进行异步操作
LaunchedEffect会提供一个协程作用域，这个作用域不会随着重组消失，它只会在该组合销毁的时候停止。
LaunchedEffect和remember一样，使用key作为是否重启的标志，当key发生变化的时候，会重新启动运行
挂起函数
下图展示了一个组件显示3秒之后会弹出一个"我显示了"的文字的可组合函数

作者：晴天小庭
链接：https://juejin.cn/post/7244420350753144891
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

图中的key1传入了Unit，也就是LaunchedEffect不会重启，你可以通过改变key的方式让它重启，
具体得看业务需要。
 */
@Composable
fun CoroutineTest() {
    var showText by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf("")
    }
    androidx.compose.runtime.LaunchedEffect(key1 = Unit) {
        kotlinx.coroutines.delay(3000L)
        showText = "我显示了"
    }
    Text(text = showText)
}


/*
2.rememberCoroutineScope：获取组合感知作用域，以便在可组合项外启动协程
使用场景：希望在非重组作用域启动协程任务
Compose中并不都是重组作用域，有一些诸如点击回调的地方，我们也希望启动协程任务，
这样LaunchedEffect就无法满足我们的需求了，因为LaunchedEffect是一个可组合函数，
他无法在重组作用域以外的地方调用。
下面看看案例，我们在重组作用域使用rememberCoroutineScope()方法生成一个scope，
这个scope的生命周期和组合的生命周期也是一致的，从组合出现到销毁，中间的重组并不会影响它，
同时我们根据名字也可以知道，这个scope内部是被remember处理过，我们不用担心重组之后又生成一个Scope。
接着我们就可以在非重组作用域（图中是onClick回调）中使用协程来完成异步操作。

你看懂了吗，点击按钮的3秒后，Text就会显示一段文字。
 */

@Composable
fun CoroutineTestBt() {
    val scope = rememberCoroutineScope()
    var showText by remember {
        mutableStateOf("")
    }
    Button(onClick = {
        scope.launch {
            delay(3000L)
            showText = "我显示了"
        }
    }) {
        Text("点击开始携程")
    }
    Text(text = showText)
}

/*
3.rememberUpdatedState：在效应中引用某个值，该效应在值改变时不应重启
使用场景：LaunchedEffect中执行了一段异步操作之后，希望取到最新的方法参数的值
假设我们拥有这样一个可组合函数，他的逻辑希望是：3秒后显示传入的num。

 */

@Composable
fun ShowText(num: Int) {
    var showText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit) {
        delay(3000L)
        showText = "显示结果：$num"
    }
    Text(text = showText)
}

/*
实际上，当你在3秒内传入了不同的num，在3秒后显示的结果是第一次传入的num。
这是什么情况呢，还记得LaunchedEffect的设计吗，它的设计就是避免异步逻辑遭受重组的干扰，
因此只有第一次传入的num会真正被LaunchedEffect的lambda拿走，其余的num都被LaunchedEffect自身的设计忽视了。
这个时候会有人想起，LaunchedEffect的key是可以让它重启的，于是会改造成这样：

 */
@Composable
fun ShowText2(num: Int) {
    var showText by remember {
        mutableStateOf("")
    }
    LaunchedEffect(num) {
        delay(3000L)
        showText = "显示结果：${num}"
    }
    Text(text = showText)
}

/*
每次num发生变化的时候，都重启LaunchedEffect，这样不就可以在3秒倒计时之后，取到的都是最新的num吗，最终结果来说这是没问题的，
显示的也是最新的值，但是问题是：倒计时也重启了。
在这种场景下，就需要使用rememberUpdatedState()了，它本质上非常简单，让我们看看源码：
kotlin复制代码@Composable
fun <T> rememberUpdatedState(newValue: T): State<T> = remember {
    mutableStateOf(newValue)
}.apply { value = newValue }

实际上就是把一个值缓存在一个MutableState里面而已，这样有什么用呢，我们看看改造后的代码：


作者：晴天小庭
链接：https://juejin.cn/post/7244420350753144891
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */

@Composable
fun showText3(num: Int) {
    var showText by remember {
        mutableStateOf("")
    }
    val rememberUpdatedNum by rememberUpdatedState(newValue = num)
    LaunchedEffect(key1 = Unit) {
        delay(3000L)
        showText = "显示结果：$rememberUpdatedNum"
    }
    Text(text = showText)
}

/*
我们继续看代码，使用rememberUpdatedState()把num缓存在一个MutableState中，当LaunchedEffect内部的delay结束时，
通过MutableState访问到了最新的值。
等等，为什么这个时候获取到的是最新的值呢，不是说LaunchedEffect不是不会受到重组影响吗，当然不会，还记得MutableState的by使用方式吗，
我们访问rememberUpDatedNum实际上是访问了MutableState内部的value变量，MutableState自始至终都没发生过变化，而是它内部的value发生了变化，
因此我们可以取到最新的值。

作者：晴天小庭
链接：https://juejin.cn/post/7244420350753144891
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */

/*
4.DisposableEffect：需要清理的效应
使用场景：当前可组合函数去订阅某些信息，而且可组合函数销毁的时候取消订阅

假设我们有一个这样的天气服务，可以通知所有的订阅者当前的天气。
 */
interface WeatherListener {

    fun onUpdate(weather: String)

}

object WeatherService {

    private val observerList = mutableListOf<WeatherListener>()

    fun addObserver(observer: WeatherListener) = observerList.add(observer)

    fun remove(observer: WeatherListener) = observerList.remove(observer)

    fun update() {
        observerList.forEach {
            it.onUpdate("下雨了")
        }
    }

}

/*
我们希望在一个组合中订阅实时的天气，可以这样做：


 */

@Composable
fun Weather() {

    var weatherString by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        val listener = object : WeatherListener {
            override fun onUpdate(weather: String) {
                weatherString = weather
            }
        }
        WeatherService.addObserver(listener)
        onDispose {
            WeatherService.remove(listener)
        }
    }

    Text("当前的天气:${weatherString}")

}

/*
DisposableEffect和LaunchedEffect很类似，都有key作为重启的标识，只是必须调用onDispose方法结尾，在onDispose中进行解绑操作。
 */


/*
5.derivedStateOf：将一个或多个状态对象转换为其他状态
使用场景：订阅可观察的列表变化、观察多个状态的变化等
有时候我们希望某个状态发生改变的时候，会改变另外一个状态的值，通常可以使用remember的key来完成这个业务，例如下图，showText的值会在num改变的时候重新生成。

作者：晴天小庭
链接：https://juejin.cn/post/7244420350753144891
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */

@Composable
fun ShowText4() {
    var num by remember {
        mutableStateOf(0)
    }
    val showText = remember(num) {
        "当前数值；$num"
    }
    Column {
        Button(onClick = { num++ }) {
            Text("点击我加 1")
        }
        Text(text = showText)
    }
}

/*
但是有些可观察的状态我们是无法使用为remember的key的，因为变化并不是发生在它自身的值的变化，而是其内部的值发生了变化，
例如常见的mutableListOf()生成的列表。
 */
@Composable
fun ShowText5() {
    var list = remember {
        mutableStateListOf<String>()
    }
    val showText = remember(list) {//没有用 以为list 没有改变
        "当前数值；${list.size}"
    }
    Column {
        Button(onClick = { list.add("") }) {
            Text("点击我加 1")
        }
        Text(text = showText)
    }
}

/*
为什么会没用呢，因为remember比较的额是对象自身，而不是对象内部的内容，对于list来说，它从来没有改变为其他引用，我们只改变它内部的元素，
因此remember是无法感知到list的变化的，这时候我们就需要使用derivedStateOf来感知。
 */
@Composable
fun ShowText6() {
    var list = remember {
        mutableStateListOf<String>()
    }
    val showText by remember {
        derivedStateOf { list.size.toString() }
    }
    Column {
        Button(onClick = { list.add("") }) {
            Text("点击我加 1")
        }
        Text(text = showText)
    }
}

/*
derivedStateOf传入的lambda里面的任意一个MutableState发生变化的时候，就会重新生成一个新值。
其他类内部包含了其他State的也可以通过这种方式来观察其变化。
总结
笔者大致讲了一下Compose的基础概念和入门难点，希望大家在入门Compose的过程中少走弯路，多理解不同api在合适的场景下的作用，
后续会出更多Compose和其他安卓开发的文章，请关注订阅点赞。

作者：晴天小庭
链接：https://juejin.cn/post/7244420350753144891
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */