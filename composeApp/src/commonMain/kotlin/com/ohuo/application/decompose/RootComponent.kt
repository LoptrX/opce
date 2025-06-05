package com.ohuo.application.decompose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.ohuo.application.decompose.RootComponent.Child.AppDetailChild
import com.ohuo.application.decompose.RootComponent.Child.AppListChild
import com.ohuo.application.decompose.RootComponent.Child.FileManagementChild
import com.ohuo.application.decompose.RootComponent.Child.HomeChild
import com.ohuo.application.decompose.RootComponent.Child.ManagementsChild
import com.ohuo.application.decompose.RootComponent.Child.MeChild
import com.ohuo.application.decompose.RootComponent.Child.ServerApiConfigChild
import com.ohuo.application.decompose.RootComponent.Child.SwitchServerChild
import com.ohuo.application.pages.home.components.HostBar
import com.ohuo.application.pages.manager.AppDetailMenus
import com.ohuo.application.services.dto.app.AppItem
import com.ohuo.application.util.scope
import kotlinx.serialization.Serializable

class RootComponent(
    context: ComponentContext,
) : ComponentContext by context {

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<Config, Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, context: ComponentContext): Child {
        return when (config) {
            is Config.Home -> HomeChild(
                HomeComponent(context, ::onNavigateToSwitchServer)
            )

            is Config.ServerApiConfig -> ServerApiConfigChild(
                ServerApiConfigComponent(context, config.id, ::onNavigateUp)
            )

            is Config.SwitchServer -> SwitchServerChild(
                SwitchServerComponent(context, ::onNavigateUp)
            )

            is Config.Managements -> ManagementsChild(
                ManagementsComponent(context, ::onNavigateToApps)
            )

            is Config.Me -> MeChild(
                MeComponent(context)
            )

            is Config.AppList -> AppListChild(
                AppListComponent(context, ::onNavigateUp, ::onNavigateToAppDetail)
            )

            is Config.AppDetail -> AppDetailChild(
                AppDetailComponent(context, config.params, ::onNavigateUp, ::onNavigateToFM)
            )

            is Config.FileManagement -> FileManagementChild(
                FileManageComponent(context, config.basePath, ::onNavigateUp)
            )
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun onNavigateToServerConfig(id: Long?) {
        navigation.push(Config.ServerApiConfig(id))
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun onNavigateToSwitchServer() {
        navigation.push(Config.SwitchServer)
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun onNavigateToApps() = navigation.push(Config.AppList)

    @OptIn(DelicateDecomposeApi::class)
    private fun onNavigateToAppDetail(app: AppItem, onRefresh: () -> Unit) =
        navigation.push(Config.AppDetail(app, onRefresh))

    @OptIn(DelicateDecomposeApi::class)
    private fun onNavigateToFM(basePath:String) =
        navigation.push(Config.FileManagement(basePath))


    fun onNavigateUp(): Boolean {
        var result = false
        navigation.pop {
            result = it
        }
        return result
    }

    fun navigateToTab(route: String) {
        // 获取当前活动的配置
        val currentConfig = stack.value.active.configuration

        // 检查当前是否已经在目标Tab，如果是则不执行导航操作
        val shouldNavigate = when (route) {
            HomeChild.ROUTE -> currentConfig != Config.Home
            ManagementsChild.ROUTE -> currentConfig != Config.Managements
            MeChild.ROUTE -> currentConfig != Config.Me
            else -> false
        }

        // 只有当需要导航时才执行导航操作
        if (shouldNavigate) {
            when (route) {
                HomeChild.ROUTE -> navigation.bringToFront(Config.Home)
                ManagementsChild.ROUTE -> navigation.bringToFront(Config.Managements)
                MeChild.ROUTE -> navigation.bringToFront(Config.Me)
                else -> {}
            }
        }
    }

    fun shouldShowTopBar(config: Config): Boolean {
        return when (config) {
            is Config.Home -> false
            is Config.ServerApiConfig -> true
            is Config.SwitchServer -> true
            is Config.Managements -> false
            is Config.Me -> false
            is Config.AppList -> true
            is Config.AppDetail -> true
            is Config.FileManagement -> true
        }
    }

    fun shouldShowBottomBar(config: Config): Boolean {
        return when (config) {
            is Config.Home -> true
            is Config.ServerApiConfig -> false
            is Config.SwitchServer -> false
            is Config.Managements -> true
            is Config.Me -> true
            is Config.AppList -> false
            is Config.AppDetail -> false
            is Config.FileManagement -> false
        }
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object Home : Config()

        @Serializable
        data class ServerApiConfig(val id: Long?) : Config()

        @Serializable
        data object SwitchServer : Config()

        @Serializable
        data object Managements : Config()

        @Serializable
        data object Me : Config()

        @Serializable
        data object AppList : Config()

        @Serializable
        data class AppDetail(val params: AppItem, val onRefresh: () -> Unit) : Config()

        @Serializable
        data class FileManagement(val basePath: String) : Config()
    }

    sealed class Child {
        abstract val title: String
        abstract val route: String

        // 添加TopBar自定义配置
        open val showTopBar: Boolean = true
        open val customTopBarTitle: String? = null

        // 添加自定义TopBar内容函数，允许页面提供完全自定义的TopBar实现
        open val customTopBarContent: (@Composable (canNavigateBack: Boolean, navigateUp: () -> Unit) -> Unit)? =
            null

        data class HomeChild(val component: HomeComponent) : Child() {
            override val title: String = "首页"
            override val route: String = ROUTE

            // 首页显示自定义的HostBar样式TopBar
            override val showTopBar: Boolean = true

            // 设置为false，因为我们使用自定义的HostBar而不是标准TopAppBar
            override val customTopBarTitle: String? = null

            // 使用新的customTopBarContent属性提供自定义TopBar实现
            override val customTopBarContent: (@Composable (canNavigateBack: Boolean, navigateUp: () -> Unit) -> Unit)? =
                { _, _ ->
                    val server = component.viewModel.stateFlow.collectAsState().value.server

                    HostBar(
                        server = server,
                        onNavigateToSwitchServer = component::navigateToSwitchServer
                    )
                }

            companion object {
                const val ROUTE = "home"
            }
        }

        data class ServerApiConfigChild(val component: ServerApiConfigComponent) : Child() {
            override val title: String = "配置页"
            override val route: String = "server_config/${component.id}"
            override val showTopBar: Boolean = true

            companion object {
                const val ROUTE_PREFIX = "server_config"
                fun createRoute(id: Long?) = "$ROUTE_PREFIX/$id"
            }
        }

        data class SwitchServerChild(val component: SwitchServerComponent) : Child() {
            override val title: String = ""
            override val route: String = ROUTE
            override val showTopBar: Boolean = true
            override val customTopBarTitle: String? = "服务器列表"

            companion object {
                const val ROUTE = "switch_server"
            }
        }

        data class ManagementsChild(val component: ManagementsComponent) : Child() {
            override val title: String = "管理"
            override val route: String = ROUTE
            override val showTopBar: Boolean = false

            companion object {
                const val ROUTE = "managements"
            }
        }

        data class MeChild(val component: MeComponent) : Child() {
            override val title: String = "我的"
            override val route: String = ROUTE
            override val showTopBar: Boolean = false

            companion object {
                const val ROUTE = "me"
            }
        }

        data class AppListChild(val component: AppListComponent) : Child() {
            override val title: String = "Apps"
            override val route: String = ROUTE
            override val showTopBar: Boolean = true

            companion object {
                const val ROUTE = "app_list"
            }
        }

        data class AppDetailChild(val component: AppDetailComponent) : Child() {
            override val title: String = ""
            override val route: String = ROUTE
            override val showTopBar: Boolean = true
            override val customTopBarTitle: String = "应用详情"

            @OptIn(ExperimentalMaterial3Api::class)
            override val customTopBarContent: @Composable ((Boolean, () -> Unit) -> Unit)? =
                { canNavigateBack, navigateUp ->
                    val state = component.viewModel.stateFlow.collectAsState()
                    val color = MaterialTheme.colorScheme.onPrimary
                    TopAppBar(
                        // 使用页面自定义标题，如果没有则使用默认标题
                        title = {
                            val title = state.value.app.name
                            Text(text = title, color = color)
                        },
                        colors = TopAppBarDefaults.topAppBarColors().copy(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        navigationIcon = {
                            if (canNavigateBack) {
                                IconButton(onClick = navigateUp) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "返回",
                                        tint = color
                                    )
                                }
                            }
                        },
                        actions = {
                            AppDetailMenus(
                                component.lifecycle.scope,
                                state.value.app,
                                component.viewModel::refresh
                            )
                        }
                    )
                }

            companion object {
                const val ROUTE = "app_detail"
            }
        }

        data class FileManagementChild(val component: FileManageComponent) : Child() {
            override val title: String = ""
            override val route: String = ROUTE
            override val showTopBar: Boolean = true
            override val customTopBarTitle: String = "文件管理"

            companion object {
                const val ROUTE = "file_management"
            }
        }

        companion object {
            // 直接路径映射表
            private val directRoutes by lazy {
                setOf(
                    HomeChild.ROUTE,
                    ManagementsChild.ROUTE,
                    MeChild.ROUTE,
                    SwitchServerChild.ROUTE,
                    AppListChild.ROUTE
                )
            }

            // 带参数的路径前缀映射表
            private val prefixRoutes by lazy {
                setOf(
                    ServerApiConfigChild.ROUTE_PREFIX,
                )
            }

            fun match(path: String?): String? {
                if (path == null) return null

                // 尝试匹配直接路径
                if (path in directRoutes) {
                    return path
                }

                // 匹配带有参数的路径
                prefixRoutes.forEach { prefix ->
                    if (path.startsWith("$prefix/")) {
                        return path
                    }
                }

                return null
            }
        }
    }
}