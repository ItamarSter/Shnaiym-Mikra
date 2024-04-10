package com.itamarstern.shnaim_mikra.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itamarstern.shnaim_mikra.MainViewModel
import com.itamarstern.shnaim_mikra.ui.components.FailureText
import com.itamarstern.shnaim_mikra.ui.components.MultiStylesText
import com.itamarstern.shnaim_mikra.ui.components.ScreenProgressBar
import com.itamarstern.shnaim_mikra.ui.components.SelectArea

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState: MainViewModel.UiState by viewModel.uiState.collectAsState()
    var fullScreen by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { navController.navigate("settingsScreen") }) {
                Text(text = "הגדרות")
            }
            Button(onClick = { fullScreen = !fullScreen }) {
                Text(text = if (fullScreen) "מסך מלא" else "הצג אפשרויות")
            }
        }
        if (fullScreen) {
            SelectArea(
                text1 = uiState.bookName,
                text2 = "ספר: ",
                onForwardClick = {
                    viewModel.onBookForwardClick()
                },
                onBackClick = {
                    viewModel.onBookBackClick()
                })
            SelectArea(
                text1 = uiState.parashaName,
                text2 = "פרשה: ",
                onForwardClick = {
                    viewModel.onParashaForwardClick()
                },
                onBackClick = {
                    viewModel.onParashaBackClick()
                })
            SelectArea(
                text1 = uiState.aliyaName,
                onForwardClick = {
                    viewModel.onAliyaForwardClick()
                },
                onBackClick = {
                    viewModel.onAliyaBackClick()
                })
        }

        when (uiState.state) {
            MainViewModel.UiState.State.Loading -> {
                ScreenProgressBar()
            }

            MainViewModel.UiState.State.Failure -> {
                FailureText()
            }

            MainViewModel.UiState.State.Fetched -> {
                MultiStylesText(
                    modifier = Modifier
                        .padding(12.dp, 0.dp)
                        .fillMaxSize()
                        .verticalScroll(ScrollState(0)),
                    textData = uiState.aliyaText,
                    textSize = uiState.fontSize
                )
            }
        }
    }
}