package com.example.vindmoller.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vindmoller.R
import com.example.vindmoller.ui.AppViewModelProvider
import com.example.vindmoller.util.WindCalculations

// home screen to show up first when application is opened
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeWindTurbineUiState by viewModel.windTurbines.collectAsState()
    val homeSourceHistoryUiState by viewModel.sourceHistory.collectAsState()
    val totalProduction = homeSourceHistoryUiState.sourceHistoryProduction
    val savedOilBarrels = viewModel.getSavedOilBarrels(totalProduction)
    val totalWindTurbines = homeWindTurbineUiState.windTurbines.size
    val totalPrice = WindCalculations.getNOKFromWind(totalProduction)

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(1f),
    ) {
        Text(
            text = stringResource(R.string.app_name).uppercase(),
            modifier = Modifier.padding(top = 46.dp, bottom = 34.dp).fillMaxWidth(1f),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.statistikk_for_norge),
            modifier = Modifier.padding(8.dp).fillMaxWidth(1f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
        LazyColumn(modifier = Modifier.fillMaxWidth(1f)) {
            item {
                InfoCard(
                    info = stringResource(id = R.string.plasserte_turbiner).uppercase(),
                    numbers = totalWindTurbines.toString())
            }
            item {
                InfoCard(
                    info = stringResource(id = R.string.energi).uppercase(),
                    numbers = "${"%.2f".format(totalProduction)} ${stringResource(id = R.string.kwh).uppercase()}")
            }
            item {
                InfoCard(
                    info = stringResource(id = R.string.spart_olje).uppercase(),
                    numbers = "${"%.2f".format(savedOilBarrels)} ${stringResource(id = R.string.fat).uppercase()}")
            }
            item {
                InfoCard(
                    info = stringResource(id = R.string.opptjent_sum).uppercase(),
                    numbers = "${"%.2f".format(totalPrice)} ${stringResource(id = R.string.kr).uppercase()}")
            }
        }

    }
}

// card to show different data like energy, count of windmills, and other things
@Composable
fun InfoCard(info: String, numbers: String) {
    Card (
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(horizontal = 16.dp, vertical = 8.dp)

    ) {
        Text(
            text = "$info: $numbers",
            modifier = Modifier.padding(12.dp)
        )
    }
}

