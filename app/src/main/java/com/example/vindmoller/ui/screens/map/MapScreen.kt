/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vindmoller.ui.screens.map

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.ContentAlpha
import com.example.vindmoller.R
import com.example.vindmoller.charts.calculateAverageWindSpeedFirstDay
import com.example.vindmoller.charts.ChartCard
import com.example.vindmoller.data.fylke.Fylke
import com.example.vindmoller.data.point.PointWithColor
import com.example.vindmoller.data.source.Source
import com.example.vindmoller.ui.AppViewModelProvider
import com.example.vindmoller.ui.theme.darkPrimaryColor
import com.example.vindmoller.ui.views.ModalBottomSheetLayout
import com.example.vindmoller.ui.views.ModalBottomSheetValue
import com.example.vindmoller.ui.views.rememberModalBottomSheetState
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navigateToSourceDetails: (String) -> Unit,
    viewModel: MapViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val mapUiState by viewModel.mapUiState.collectAsState()
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val context = LocalContext.current


    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            Column {

                Spacer(modifier = Modifier.height(8.dp))
                if (mapUiState.selectedSource != null) {


                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "${mapUiState.selectedCounty?.fylkeName} ${mapUiState.selectedSource!!.topografi} ",
                            color = darkPrimaryColor)
                    }

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "${mapUiState.selectedSourceWindTurbines.size}",
                            color = darkPrimaryColor)
                    }
                    // calling here to show buttons in bottom sheet
                    WindTurbineButtons(
                        onAddWindTurbine = { viewModel.addWindTurbine(mapUiState.selectedSource!!) },
                        onDeleteWindTurbine = {
                            if (mapUiState.selectedSourceWindTurbines.isNotEmpty()) viewModel.deleteWindTurbine(
                                mapUiState.selectedSourceWindTurbines.last(),
                                mapUiState.selectedSource!!
                            )
                        }
                    ) {
                        if (mapUiState.selectedSourceWindTurbines.isNotEmpty())
                            navigateToSourceDetails(mapUiState.selectedSource!!.sourceId)
                        else
                            Toast.makeText(
                                context,
                                "Område er tom \nLegg til vindmølle",
                                Toast.LENGTH_SHORT
                            ) // in case of no windmills
                                .show()

                    }
                    // update the number of windturbines

                }

            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                // gridmap card
                Box(
                    modifier = Modifier.weight(1f)
                ) {// padding of MAP from screen edges, ADJUST HERE
                    GridMapCard(
                        points = mapUiState.mapPoints,
                        selectedFylke = mapUiState.selectedCounty,
                        selectedSourceForecast = mapUiState.selectedSourceForecast,
                        isopenSheet = bottomSheetState.isVisible,
                        viewModel = viewModel
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))

                // SelectItemDropdown to select a county
                SelectItemDropdown(
                    stringResource(id = R.string.fylke),
                    mapUiState.availableCounties,
                    Fylke::fylkeName,
                    viewModel::setSelectedCounty,
                    selectedItem = mapUiState.selectedCounty // Pass selectedFylke
                )


                Spacer(modifier = Modifier.height(8.dp))

                // SelectItemDropdown to select a source for the selected county
                SelectItemDropdown(
                    label = stringResource(id = R.string.landskap),
                    list = mapUiState.availableSources,
                    listProperty = Source::topografi,
                    onSelect = {
                        viewModel.setSelectedSource(it)
                        if (!bottomSheetState.isVisible) scope.launch { bottomSheetState.show() }
                    },
                    enabled = mapUiState.availableSources.isNotEmpty()
                )


            }


        },
        sheetBackgroundColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.25f)
    )
    if (mapUiState.selectedSource != null) {
        LaunchedEffect(Unit) {
            scope.launch { bottomSheetState.show() }
        }

    }

}

// this is a dropdown menu from which you can select items in a list
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectItemDropdown(
    label: String,
    list: List<T>,
    listProperty: KProperty1<T, String>,
    onSelect: (T) -> Unit = {},
    enabled: Boolean = true,
    selectedItem: T? = null, // Add this line
)  {
    var expanded by remember { mutableStateOf(false) } // whether the dropdown menu is opened or not
    var selected by remember { mutableStateOf("") } // the string representation of what is selected

    LaunchedEffect(list, selectedItem) {
        selected = selectedItem?.let { listProperty.get(it) } ?: ""
    }

    key(enabled) { // workaround
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
            if (enabled) expanded = !expanded
        }, // this did not realize whether enabled changed without the key workaround above
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(enabled = enabled) {}
                .then(if (enabled) Modifier.alpha(1F) else Modifier.alpha(ContentAlpha.disabled))) {
            TextField(
                readOnly = true,
                value = selected,
                onValueChange = {},
                label = { Text(text = label, color = Color.Black) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedTrailingIconColor = Color.Black,
                    unfocusedTrailingIconColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize(true)
            ) {
                list.forEach {
                    DropdownMenuItem(modifier = Modifier.fillMaxWidth(),
                        text = { Text(listProperty.get(it)) },
                        onClick = {
                            onSelect(it)
                            selected = listProperty.get(it)
                            expanded = false
                        })
                }
            }
        }
    }
}

// GridMap canvas, clickabe map of Norway
@Composable
fun ClickableGridMapCanvas(
    points: List<PointWithColor>,
    selectedFylke: Fylke?,
    onBoxClick: (PointWithColor) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.weight(0.04f))

        Row(Modifier.weight(1f)) {
            Spacer(modifier = Modifier.weight(0.04f))

            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .background(color = Color.Transparent)
            ) {
                val tileAmountVertical = 34
                val tileAmountHorizontal = 27


                val availableWidth by remember { mutableStateOf(this.maxWidth) }
                val availableHeight by remember { mutableStateOf(this.maxHeight) }

                val tileSize = if (availableHeight < availableWidth) {
                    availableHeight / tileAmountVertical
                } else {
                    availableWidth / tileAmountHorizontal
                }

                points.forEach { point ->
                    val isSelected =
                        (selectedFylke != null) && (point.fylkeId == selectedFylke.fylkeId)
                    val color = when {
                        isSelected -> darkPrimaryColor // use the App´s blue color when selected
                        point.color == 1 -> Color.White
                        point.color == 2 -> Color.LightGray
                        point.color == 3 -> Color.Gray
                        else -> Color.Transparent
                    }

                    Box(
                        modifier = Modifier
                            .offset(
                                x = (tileSize * point.x.toFloat()),
                                y = (tileSize * point.y.toFloat())
                            )
                            .size(width = tileSize, height = tileSize)
                            .background(color = color)
                            .let {
                                if (isSelected) it.shadow(
                                    30.dp,
                                    shape = RoundedCornerShape(50)
                                ) else it
                            } // SHADOW WHEN SELECTED, CAN BE REMOVED HERE
                            .clickable { onBoxClick(point) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.04f))
        }

        Spacer(modifier = Modifier.weight(0.04f))
    }
}

// a card that contains Norway´s grid map card
@Composable
fun GridMapCard(
    points: List<PointWithColor>,
    selectedFylke: Fylke?,
    selectedSourceForecast: LocationForecastData,
    isopenSheet: Boolean,
    viewModel: MapViewModel
) {
    val padding = if (isopenSheet) 40 else 0

    Card(
        modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(16.dp)
    ) {
        Column {
                Box(
                    modifier = Modifier
                        //.padding(start = 25.dp, top = 30.dp)

                ) {
                    ClickableGridMapCanvas(points, selectedFylke, viewModel::setCountyFromPoint)

                    if (selectedSourceForecast is LocationForecastData.Success) {
                        val snittVind =
                            calculateAverageWindSpeedFirstDay(selectedSourceForecast.sourceForecasts)
                        Text(
                            text = "${snittVind}m/s",
                            modifier = Modifier.padding(24.dp),
                            color = darkPrimaryColor
                        ) // snitt vind
                    }

                    if (selectedSourceForecast is LocationForecastData.Success) {


                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = padding.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                            ) {
                                ChartCard(
                                    sourceForecasts = selectedSourceForecast.sourceForecasts
                                )
                            }

                        }
                    }
                }
        }
    }
}

// Row of two buttons, add Turbine + counter , Details
@Composable
fun WindTurbineButtons(
    onAddWindTurbine: () -> Unit,
    onDeleteWindTurbine: () -> Unit,
    onNavigateToDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = onAddWindTurbine)
        ) {
            Image(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "",
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.legg_til_turbin), style = TextStyle(color = Color.Black))

        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = onDeleteWindTurbine)
        ) {
            Image(
                painter = painterResource(id = R.drawable.minus),
                contentDescription = "",
                modifier = Modifier
                    .size(25.dp)
                    .scale(0.7f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.fjern_turbin), style = TextStyle(color = Color.Black))

        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = onNavigateToDetails)
        ) {
            Image(
                painter = painterResource(id = R.drawable.map_marker_radius),
                contentDescription = "",
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.vindmoller), style = TextStyle(color = Color.Black))

        }
    }
}
