package com.itamarstern.shnaim_mikra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itamarstern.shnaim_mikra.MainViewModel
import com.itamarstern.shnaim_mikra.R

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState: MainViewModel.UiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Right,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.brown))
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "חזרה")
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { viewModel.onIncreaseFontClick() }) {
                Icon(modifier = Modifier.size(20.dp), painter = painterResource(id = R.drawable.plus), contentDescription = "הגדלה")
            }
            Text(
                text = "גודל טקסט",
                style = TextStyle(fontSize = uiState.fontSize.sp)
            )
            IconButton(onClick = { viewModel.onDecreaseFontClick() }) {
                Icon(modifier = Modifier.size(20.dp), painter = painterResource(id = R.drawable.minus), contentDescription = "הקטנה")
            }
        }
    }
}