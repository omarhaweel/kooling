package com.example.vindmoller.ui.screens.stations


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vindmoller.R
import com.example.vindmoller.data.source.Source
import com.example.vindmoller.data.windturbines.WindTurbine
import com.example.vindmoller.ui.AppViewModelProvider
import com.example.vindmoller.util.WindCalculations
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StationsScreen(
    navigateBack: () -> Unit,
    viewModel: StationsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.stationsUiState.collectAsState()
    val sourceList = uiState.value.sources // all the sources in the county here
    var valgtSourceID by remember { mutableStateOf("") }
    val listeAvWindTurbines = uiState.value.windTurbines // liste av vindTurbiner i en stasjon
    val removedTurbineSources = remember { mutableStateListOf<String>() } // hold kontroll over alle sources id hvor vi fjernet turbiner fra.
    var showRemoveDialog by remember { mutableStateOf(false) }
    var selectedSourceForRemoval by remember { mutableStateOf<Source?>(null) }
    val totalProduction = viewModel.getTotalProduction(uiState.value.sourceHistoryData) // total produksjon for alle vindmøller på en stasjon
    val avoidedEmission = viewModel.getAvoidedEmission(totalProduction,0.9 )
    val savedOilBarrels = viewModel.getSavedOilBarrelsCounty(totalProduction)

    RemoveTurbinesDialog(
        showDialog = showRemoveDialog,
        onConfirm = {
            viewModel.removeALLTurbinesInCounty(selectedSourceForRemoval!!)
            removedTurbineSources.add(selectedSourceForRemoval!!.sourceId)
            showRemoveDialog = false
            selectedSourceForRemoval = null
        },
        onDismiss = {
            showRemoveDialog = false
            selectedSourceForRemoval = null
        }
    )

    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize(1f)) {
        Button(
            onClick = { navigateBack() },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(stringResource(id = R.string.tilbake_til_fylker))
        }

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = "${stringResource(R.string.vindmoller_i)} ${uiState.value.fylke?.fylkeName}",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start =16.dp),
        )

        Spacer(modifier = Modifier.padding(4.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth(1f)) {
            items(sourceList) { source ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            valgtSourceID = if (valgtSourceID == source.sourceId) ""
                            else source.sourceId
                            viewModel.getSourceWindTurbines(valgtSourceID)
                            viewModel.getSourceHistoryData(valgtSourceID)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(vertical = 12.dp)) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(source.topografi)
                            Icon(
                                imageVector = if (valgtSourceID == source.sourceId) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                modifier = Modifier
                                    .size(20.dp),
                                contentDescription = "drawable icons",
                            )
                        }
                        AnimatedVisibility(valgtSourceID == source.sourceId) {
                            Column(modifier = Modifier.weight(1f)) {
                                val sourceRemoved = removedTurbineSources.contains(source.sourceId)
                                if (listeAvWindTurbines.isNotEmpty() && !sourceRemoved) {
                                    SourceHistoryDataResults(
                                        uiState.value.windTurbines,
                                        totalProduction,
                                        avoidedEmission,
                                        savedOilBarrels
                                    )
                                    Button(
                                        onClick = {
                                            selectedSourceForRemoval = source
                                            showRemoveDialog = true
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(1f)  // Set the button width to fill the entire width of the screen
                                            .padding(8.dp)
                                    ) {
                                        Text("Fjern alle") //text = stringResource(id = R.string.fjern_alle), don't like stringResource
                                    }
                                } else if (sourceRemoved) {
                                    Text(text = stringResource(id = R.string.ingen_plasserte_vindmoller), modifier = Modifier.padding(horizontal = 12.dp))
                                } else {
                                    Text(text = stringResource(id = R.string.ingen_plasserte_vindmoller), modifier = Modifier.padding(horizontal = 12.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun RemoveTurbinesDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            title = { Text(stringResource(id = R.string.fjern_turbiner)) },
            text = { Text(
                text =stringResource(id = R.string.vil_du_fjerne_alle),
                color = Color.Black
            ) },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(stringResource(id = R.string.ja))
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(id = R.string.nei))
                }
            },
            onDismissRequest = onDismiss
        )
    }
}

// cards to show data generated by windmills
@Composable
fun SourceHistoryDataResults(
    listOfTurbines: List<WindTurbine>,
    totalProduction: Double,
    avoidedEmission: Double,
    savedOilBarrels: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        StatCards(
            information = stringResource(id = R.string.turbiner_ved_stasjon),
            numbers = listOfTurbines.size.toString(),
            typeNum = "",
            color = 0xFF003645
        )
        StatCards(
            information = stringResource(id = R.string.energi),
            numbers = "%.2f".format(totalProduction),
            typeNum = stringResource(id = R.string.kwh),
            color = 0xFF135B6E
        )
        StatCards(
            information = stringResource(id = R.string.ungatt_co2),
            numbers = "%.2f".format(avoidedEmission),
            typeNum = stringResource(id = R.string.kg),
            color = 0xFF545A5B
        )
        StatCards(
            information = stringResource(id = R.string.spart_olje),
            numbers = "%.2f".format(savedOilBarrels),
            typeNum = stringResource(id = R.string.fat),
            color = 0xFF003645
        )
        StatCards(
            information = stringResource(id = R.string.opptjent_sum),
            numbers = "%.2f".format(WindCalculations.getNOKFromWind(totalProduction)),
            typeNum = stringResource(id = R.string.kr),
            color = 0xFF135B6E
        )
    }
}

//the card design to chow each data
@Composable
fun StatCards(information: String, numbers: String, typeNum: String, color: Long) {
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth(1f),
        colors = CardDefaults.cardColors(Color(color))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = information,
                color = Color.White
            )
            Text(
                text = "$numbers $typeNum",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}