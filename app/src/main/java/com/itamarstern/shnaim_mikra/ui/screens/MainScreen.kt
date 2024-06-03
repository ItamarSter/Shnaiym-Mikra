package com.itamarstern.shnaim_mikra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itamarstern.shnaim_mikra.MainViewModel
import com.itamarstern.shnaim_mikra.R
import com.itamarstern.shnaim_mikra.ui.components.FailureText
import com.itamarstern.shnaim_mikra.ui.components.MultiStylesText
import com.itamarstern.shnaim_mikra.ui.components.ScreenProgressBar
import com.itamarstern.shnaim_mikra.ui.components.SelectArea
import com.itamarstern.shnaim_mikra.ui.components.ToggleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState: MainViewModel.UiState by viewModel.uiState.collectAsState()
    var fullScreen by rememberSaveable { mutableStateOf(true) }
    var isFirstTime by rememberSaveable { mutableStateOf(true) }
    val scrollState = rememberScrollState(uiState.scrollOffset)
    val coroutineScope = rememberCoroutineScope()
    val resetScroll = {
        coroutineScope.launch {
            scrollState.scrollTo(0)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.scrollOffset >= 0 && isFirstTime) {
            scrollState.scrollTo(uiState.scrollOffset)
            isFirstTime = false
        }
    }

    LaunchedEffect(scrollState.value) {
        withContext(Dispatchers.IO) {
            viewModel.saveScrollOffset(scrollState.value)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.brown))

        ) {
            IconButton(onClick = { navController.navigate("settingsScreen") }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "הגדרות")
            }
            IconButton(onClick = { fullScreen = !fullScreen }) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = if (fullScreen) painterResource(id = R.drawable.full_screen) else painterResource(
                        id = R.drawable.exit_full_screen
                    ), contentDescription = "מסך מלא"
                )
            }
        }
        if (fullScreen) {
            SelectArea(
                modifier = Modifier.background(color = colorResource(id = R.color.brown2)),
                text1 = uiState.bookName,
                text2 = "ספר: ",
                onForwardClick = {
                    resetScroll()
                    viewModel.onBookForwardClick()
                },
                onBackClick = {
                    resetScroll()
                    viewModel.onBookBackClick()
                })
            SelectArea(
                modifier = Modifier.background(color = colorResource(id = R.color.brown3)),
                text1 = uiState.parashaName,
                text2 = "פרשה: ",
                onForwardClick = {
                    resetScroll()
                    viewModel.onParashaForwardClick()
                },
                onBackClick = {
                    resetScroll()
                    viewModel.onParashaBackClick()
                })
            uiState.parasha?.let {
                if (it.canBeConnected) {
                    ToggleView(
                        isConnected = uiState.isConnected,
                        onConnectParashasToggled = {
                            viewModel.connectedParashasClicked()
                        }
                    )
                }
            }
            SelectArea(
                modifier = Modifier.background(color = colorResource(id = R.color.brown4)),
                text1 = uiState.aliyaName,
                onForwardClick = {
                    resetScroll()
                    viewModel.onAliyaForwardClick()
                },
                onBackClick = {
                    resetScroll()
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
                        .verticalScroll(scrollState),
                    textData = uiState.aliyaText,
                    textSize = uiState.fontSize
                )
            }
        }
    }
}