package com.example.vindmoller.ui.screens.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vindmoller.R
import com.example.vindmoller.data.windturbines.WindTurbine
import com.example.vindmoller.ui.AppViewModelProvider
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*
// details screen that lists all wind turbines in the selected source user navigated from
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsScreen(
    navigateBack: () -> Unit,
    viewModel: DetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val detailsWindTurbineUiState by viewModel.detailsWindTurbineUiState.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(120.dp)
            ) {
                Button(
                    onClick = { navigateBack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.tilbake_til_kart))
                }
            }
        }
        Spacer(Modifier.height(16.dp)) // Add space between buttons and LazyColumn
        Box(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = Color.DarkGray,
                    //shape = RoundedCornerShape(16.dp)
                )
        ) {
            LazyColumn {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.plasseringer),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
                // list all windturbines in this source
                items(detailsWindTurbineUiState.windTurbines) {
                    WindTurbineRow(it)
                }
            }
        }
    }
}

@Composable
fun WindTurbineRow(windTurbine: WindTurbine) {
    Column {
        Spacer(Modifier.height(1.dp))
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                RowText(firstText = stringResource(id = R.string.turbin_id)+" ", lastText = windTurbine.id.toString())
                RowText(firstText = stringResource(id = R.string.plasseringsTid)+" ", lastText = windTurbine.timestamp.toLocalDateTime(TimeZone.UTC).toString())
            }
        }
    }
}

@Composable
fun RowText(firstText: String, lastText: String) {
    Row {
        Text(
            text = firstText,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = lastText,
            color = Color.Black
        )
    }
    Spacer(Modifier.height(8.dp))
}
