package com.itamarstern.shnaim_mikra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itamarstern.shnaim_mikra.ui.components.FailureText
import com.itamarstern.shnaim_mikra.ui.components.MultiStylesText
import com.itamarstern.shnaim_mikra.ui.components.ScreenProgressBar
import com.itamarstern.shnaim_mikra.ui.components.SelectArea
import com.itamarstern.shnaim_mikra.ui.theme.ShnaimMikraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShnaimMikraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel by viewModels()
                    val uiState: MainViewModel.UiState by viewModel.uiState.collectAsState()

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}