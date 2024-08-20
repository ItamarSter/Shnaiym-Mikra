package com.itamarstern.shnaim_mikra.ui.glance

import android.content.Context
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import com.itamarstern.shnaim_mikra.R
import com.itamarstern.shnaim_mikra.data.aliyas
import com.itamarstern.shnaim_mikra.data.humashs
import com.itamarstern.shnaim_mikra.data.parashas
import com.itamarstern.shnaim_mikra.di.dataStore
import com.itamarstern.shnaim_mikra.local.DataStoreRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent


class AppWidget : GlanceAppWidget() {

    // a way to get hilt inject what you need in non-suported class
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface PreferencesProviderEntryPoint {
        fun preferencesRepository(): DataStoreRepository
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {


            //get preferences repository from hilt
            val appContext = context.applicationContext ?: throw IllegalStateException()
            val preferencesEntryPoint =
                EntryPointAccessors.fromApplication(appContext, PreferencesProviderEntryPoint::class.java)
            val userPreferences = preferencesEntryPoint.preferencesRepository()


        provideContent {
            var isError = false
            var ex = ""
            val state by userPreferences.getAliyaDetailsFlow().collectAsState("")
            val details: List<String>
            var bookIndex = 0
            var parashaIndex = 0
            var aliyaIndex = 0
            var isConnectedParashas = false
            try {
                details = state.split(',')
                bookIndex = if (state.isNotEmpty()) details[0].toInt() else 0
                parashaIndex = if (state.isNotEmpty()) details[1].toInt() else 0
                aliyaIndex = if (state.isNotEmpty()) details[2].toInt() else 0
                isConnectedParashas = if (state.isNotEmpty()) details[3].toInt() == 1 else false
            } catch (e: Exception) {
                isError = true
                ex = e.message.toString()
            }
            if (isError) {
                Text(text = ex)
            } else {
                Column(modifier = GlanceModifier.fillMaxSize()) {
                    GlanceSelectArea(
                        GlanceModifier.defaultWeight(),
                        backgroundResource = R.color.brown2,
                        "ספר: ",
                        humashs[bookIndex],
                        onLeftArrowClick = {},
                        onRightArrowClick = {}
                    )
                    GlanceSelectArea(
                        GlanceModifier.defaultWeight(),
                        backgroundResource = R.color.brown3,
                        "פרשה: ",
                        if (isConnectedParashas && parashas[bookIndex][parashaIndex].canBeConnected) parashas[bookIndex][parashaIndex].nameIfConnected else parashas[bookIndex][parashaIndex].name,
                        onLeftArrowClick = {},
                        onRightArrowClick = {}
                    )
                    GlanceSelectArea(
                        GlanceModifier.defaultWeight(),
                        backgroundResource = R.color.brown4,
                        "עלייה: ",
                        aliyas[aliyaIndex],
                        onLeftArrowClick = {},
                        onRightArrowClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun GlanceSelectArea(
    modifier: GlanceModifier,
    @ColorRes backgroundResource: Int,
    text: String,
    text2: String,
    onLeftArrowClick: () -> Unit,
    onRightArrowClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(12.dp, 0.dp).background(backgroundResource),
    ) {
        Image(
            provider = ImageProvider(R.drawable.right_arrow),
            contentDescription = "",
            modifier = GlanceModifier.defaultWeight().size(24.dp).clickable(onRightArrowClick)
        )
        Text(text = text, modifier = GlanceModifier.defaultWeight())
        Text(text = text2, modifier = GlanceModifier.defaultWeight())
        Image(
            provider = ImageProvider(R.drawable.left_arrow),
            contentDescription = "",
            modifier = GlanceModifier.defaultWeight().size(24.dp).clickable(onLeftArrowClick)
        )
    }
}