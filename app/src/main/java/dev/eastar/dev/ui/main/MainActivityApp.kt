package dev.eastar.dev.ui.main

import android.log.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import dev.eastar.data.provider.CheatEntity
import dev.eastar.dev.R
import dev.eastar.dev.ui.theme.EastarDevTheme
import dev.eastar.dev.ui.topbar.InsetAwareTopAppBar

@Composable
fun MainActivityApp() {
    EastarDevTheme {
        // val coroutineScope = rememberCoroutineScope()
        Scaffold(
            scaffoldState = rememberScaffoldState(),
            topBar = {
                val title = stringResource(id = R.string.app_name)
                InsetAwareTopAppBar(
                    title = { Text(text = title) },
                    navigationIcon = {
                        Icon(painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = stringResource(R.string.app_name)
                        )
                    }
                )
            }
        ) {
            PostList(
                posts = listOf(CheatEntity(0, "1", "2"),
                    CheatEntity(3, "4", "5")
                ),
                navigateToArticle = { id ->
                    Log.e(id)
                },
            )
        }
    }
}

@Composable
private fun PostList(
    posts: List<CheatEntity>,
    navigateToArticle: (postId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false
        )
    ) {
        item { PostListHistorySection(posts, navigateToArticle) }
    }
}

@Composable
private fun PostListHistorySection(
    posts: List<CheatEntity>,
    navigateToArticle: (Long) -> Unit
) {
    Column {
        repeat(200) {
            posts.forEach { post ->
                PostCardHistory(post, navigateToArticle)
                PostListDivider()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // MainActivityApp()
    PostList(
        posts = listOf(CheatEntity(0, "1", "2"),
            CheatEntity(3, "4", "5")
        ),
        navigateToArticle = { id ->
            Log.e(id)
        },
    )
}