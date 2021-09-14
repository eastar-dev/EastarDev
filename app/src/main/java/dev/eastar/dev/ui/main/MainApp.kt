package dev.eastar.dev.ui.main

import android.log.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.eastar.data.provider.CheatEntity
import dev.eastar.dev.R
import dev.eastar.dev.ui.MainActivityViewModel
import dev.eastar.dev.ui.theme.AppTheme

@Composable
fun MainActivityApp( viewModel : MainActivityViewModel = viewModel()) {
    // val viewModel : MainActivityViewModel by viewModels()
    //  val viewModel : MainActivityViewModel by viewModel()
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color(0x55c20029))
    }
    AppTheme {
        // val coroutineScope = rememberCoroutineScope()
        Log.e("title0")
        viewModel.getCheats()

        Scaffold(
            scaffoldState = rememberScaffoldState(),
            topBar = {
                Log.e("title")
                val title = stringResource(id = R.string.app_name)
                TopAppBar(
                    title = {
                        Text(text = title)
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = stringResource(R.string.app_name)
                        )
                    },
                    actions = {
                        Log.e(this)
                    },
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding(bottom = false)
                )
            }
        ) {
            Log.e("cheats")
            val cheats = listOf(
                CheatEntity(0, "1", "2"),
                CheatEntity(3, "4", "5")
            )

            val navigateToArticle = { id: Long ->
                Log.e(id)
            }

            LazyColumn {

                item { PostListHistorySection(cheats, navigateToArticle) }

            }
        }
    }
}

@Composable
private fun PostListHistorySection(
    cheats: List<CheatEntity>,
    navigateToArticle: (Long) -> Unit
) {
    Column {
        repeat(10) {
            cheats.forEachIndexed { index, cheatEntity ->
                PostCardHistory(cheatEntity.copy(id = ((index * 10 + it).toLong())), navigateToArticle)
                PostListDivider()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // MainActivityApp()
    val cheats = listOf(
        CheatEntity(0, "1", "2"),
        CheatEntity(3, "4", "5")
    )
    val navigateToArticle = { id: Long ->
        Log.e(id)
    }

    LazyColumn {
        item { PostListHistorySection(cheats, navigateToArticle) }
    }
}