package com.asp424.weather.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.asp424.weather.activity.MainActivity

import com.asp424.weather.data.view_states.InternetResponse
import com.asp424.weather.ui.cells.*
import com.asp424.weather.ui.viewmodel.MainViewModel
import com.asp424.weather.utilites.isOnline
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FeatureScreen(viewModel: MainViewModel) {
    BackgroundImage()
    var visible by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current as MainActivity
    BackgroundImage()
    val values by remember(viewModel) {
        viewModel.internetValues
    }.collectAsState()
    when (values) {
        is InternetResponse.OnSuccess -> {
            (values as InternetResponse.OnSuccess).dataValues.apply {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                ) {
                    Visibility(visible = visible) {
                        Card(backgroundColor = Color.Yellow) {
                            Header(
                                string = "Отсутствует интернет",
                                color = Color.Red,
                                paddingTop = 8.dp, paddingStart = 8.dp,
                                paddingBottom = 8.dp, paddingEnd = 8.dp
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 65.dp, top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        YandexF(
                            JSONObject()
                                .put("yan_temp", getString("yan_temp"))
                                .put("yan_rain", getString("yan_rain"))
                        )
                        GidroMetF(
                            JSONObject()
                                .put("hydro_today_temp", getString("hydro_today_temp"))
                                   .put("hydro_today_rain", getString("hydro_today_rain"))


                        )
                        GisMeteoF(
                            JSONObject()
                                .put("gis_temp", getString("gis_temp"))
                                .put("gis_wind", getString("gis_wind"))
                                .put("gis_pres", getString("gis_pres"))
                                .put("gis_hum", getString("gis_hum"))
                                .put("gis_fill", getString("gis_fill"))
                                .put("gis_rain", getString("gis_rain"))
                        )
                    }
                }
            }
        }
        is InternetResponse.Loading -> {
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isOnline(context)) {
                    Card(backgroundColor = Color.Black) {
                        Header(
                            string = "Загрузка...",
                            paddingTop = 8.dp, paddingStart = 8.dp,
                            paddingBottom = 8.dp, paddingEnd = 8.dp
                        )
                    }
                } else {
                    visible = true
                    Card(backgroundColor = Color.Yellow) {
                        Header(
                            string = "Отсутствует интернет",
                            color = Color.Red,
                            paddingTop = 8.dp, paddingStart = 8.dp,
                            paddingBottom = 8.dp, paddingEnd = 8.dp
                        )
                    }
                }

            }
            LocalLifecycleOwner.current.lifecycle.addObserver(viewModel)
        }
    }
}
