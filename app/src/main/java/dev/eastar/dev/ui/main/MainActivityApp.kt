package dev.eastar.dev.ui.main

import android.log.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import dev.eastar.data.provider.CheatEntity
import dev.eastar.dev.R
import dev.eastar.dev.ui.MainActivityViewModel
import dev.eastar.dev.ui.theme.EastarDevTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainActivityApp(viewModel: MainActivityViewModel = viewModel()) {
    EastarDevTheme {
        // val coroutineScope = rememberCoroutineScope()
        viewModel.getCheats()
        Log.e("title0")

        Scaffold(
            scaffoldState = rememberScaffoldState(),
            topBar = {
                Log.e("title")
                val title = stringResource(id = R.string.app_name)
                TopAppBar(
                    title = {
                        Log.w("title")
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