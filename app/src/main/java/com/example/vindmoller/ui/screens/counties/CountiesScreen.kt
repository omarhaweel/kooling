package com.example.vindmoller.ui.screens.counties

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vindmoller.R
import com.example.vindmoller.data.fylke.FylkeWithWindTurbines
import com.example.vindmoller.ui.AppViewModelProvider
import com.example.vindmoller.ui.theme.gradientColor4
// counties screen
@Composable
fun CountiesScreen(
    navigateToFylke: (Int) -> Unit,
    viewModel: CountiesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.countiesUiState.collectAsState()
    CountyCards(countyWithWindTurbines = uiState.countySources, navigateToFylke = navigateToFylke)
}
// cards that contain all counties
@Composable
fun CountyCards(countyWithWindTurbines: List<FylkeWithWindTurbines>, navigateToFylke: (Int) -> Unit) {
    var valgtFylke by remember { mutableStateOf(-1) }
    var clicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(1f)) {

        Spacer(modifier = Modifier.padding(4.dp)
        )
        Text(
            "Fylker",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
            )
        Spacer(modifier = Modifier.padding(4.dp))


        LazyColumn(modifier = Modifier.fillMaxWidth(1f)) {
            items(countyWithWindTurbines, key = { it.fylke.fylkeId }) { county ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            if (valgtFylke == county.fylke.fylkeId) {
                                clicked = !clicked
                            } else {
                                valgtFylke = county.fylke.fylkeId
                                clicked = true
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(Modifier.padding(vertical = 12.dp)) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(county.fylke.fylkeName)
                            Icon(
                                imageVector = if (valgtFylke == county.fylke.fylkeId && clicked) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                                modifier = Modifier
                                    .size(20.dp),
                                contentDescription = "drawable icons",
                            )
                        }

                        AnimatedVisibility(valgtFylke == county.fylke.fylkeId && clicked) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(1f)

                            ) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(gradientColor4)
                                        .height(100.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = stringResource(id = R.string.vindmoller),
                                            style = TextStyle(
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 14.sp,
                                                color = Color.White
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "${county.windTurbines.size}",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp, color = Color.White
                                            )
                                        )
                                    }


                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = { navigateToFylke(valgtFylke) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowRight,
                                            modifier = Modifier
                                                .size(20.dp)
                                                .align(Alignment.CenterEnd),
                                            contentDescription = "drawable icons",
                                            tint = Color.White
                                        )
                                        Text(
                                            text = "Landskap",
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





