package com.ohuo.application

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.ohuo.application.rootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 设置沉浸式状态栏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        
        // 添加返回事件处理
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 检查当前是否在TabBar页面（底部导航栏可见的页面）
                val isInTabBarScreen = try {
                    // 尝试访问rootComponent，如果已初始化则检查是否应显示底部导航栏
                    rootComponent.shouldShowBottomBar(rootComponent.stack.value.active.configuration)
                } catch (e: UninitializedPropertyAccessException) {
                    // 如果rootComponent未初始化，则不在TabBar页面
                    false
                }
                
                if (isInTabBarScreen) {
                    // 如果在TabBar页面，直接返回到桌面
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                } else if (!this@MainActivity.onNavigateUp()) {
                    // 如果不在TabBar页面且根组件无法处理返回事件，则调用默认行为
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })
        
        setContent {
            App()
        }
    }
    
    // 处理返回事件，委托给根组件处理
    override fun onNavigateUp(): Boolean {
        // 尝试通过根组件处理返回事件，使用安全调用避免空指针异常
        return try {
            // 如果rootComponent已初始化，调用其onNavigateUp方法
            rootComponent.onNavigateUp()
        } catch (e: UninitializedPropertyAccessException) {
            // 如果rootComponent未初始化，返回false
            false
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}