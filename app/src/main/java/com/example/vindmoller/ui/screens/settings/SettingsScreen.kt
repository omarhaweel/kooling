package com.example.vindmoller.ui.screens.settings


import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.ContentAlpha
import com.example.vindmoller.R
import com.example.vindmoller.WindInitializer
import com.example.vindmoller.ui.AppViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToHome: () -> Unit
) {
    val settingsUiState by viewModel.settingsUiState.collectAsState()

    Row {
        Spacer(modifier = Modifier.weight(0.04f))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = R.string.instillinger),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.weight(0.04f))

            SettingCard(
                settingsUiState.overrideSystemTheme,
                viewModel::toggleOverride
            ) {
                Text(stringResource(id = R.string.overstyr_systemet), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(0.04f))

            SettingCard(
                if(settingsUiState.overrideSystemTheme) settingsUiState.darkMode else isSystemInDarkTheme(),
                viewModel::toggleDarkMode,
                settingsUiState.overrideSystemTheme
            ) {
                Text(
                    text = stringResource(id = R.string.mork_modus),
                    fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(0.04f))

            // restart button with alertdialog

            val confirmRestartDialog = remember { mutableStateOf(false) }
            SettingsDialog(confirmRestartDialog, R.string.tilbakestilling_tittel, R.string.tilbakestilling_beskrivelse) {
                CoroutineScope(Dispatchers.IO).launch {
                    WindInitializer.db.resetDatabase()
                }
                navigateToHome()
            }

            Button(onClick = {
                confirmRestartDialog.value = true
            }) {
                Text(stringResource(id = R.string.reset))
            }

            val confirmDummyState = remember { mutableStateOf(false) }
            SettingsDialog(confirmDummyState, R.string.dummystate_tittel, R.string.dummystate_beskrivelse) {
                CoroutineScope(Dispatchers.IO).launch {
                    WindInitializer.db.launchDummyState()
                }
                navigateToHome()
            }

            Button(onClick = {
                confirmDummyState.value = true
            }) {
                Text(stringResource(id = R.string.dummy))
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.weight(0.04f))
    }
}

@Composable
fun SettingCard(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(enabled = enabled) { onCheckedChange() }
        .then(if (enabled) Modifier.alpha(1F) else Modifier.alpha(ContentAlpha.disabled))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(0.04f))
            content()
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange() },
                enabled = enabled
            )
            Spacer(modifier = Modifier.weight(0.04f))
        }
    }
}

@Composable
fun SettingsDialog(opened: MutableState<Boolean>, a: Int, b: Int, onConfirm: () -> Unit) {
    if(opened.value) {
        if (opened.value) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    opened.value = false
                },
                title = {
                    Text(text = stringResource(id = a))
                },
                textContentColor = AlertDialogDefaults.titleContentColor,
                text = {
                    Text(text = stringResource(id = b))
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirm()
                            opened.value = false
                        }
                    ) {
                        Text(stringResource(id = R.string.ja))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            opened.value = false
                        }
                    ) {
                        Text(stringResource(id = R.string.nei))
                    }
                }
            )
        }
    }
}



