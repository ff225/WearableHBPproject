package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.presentation.AppViewModelProvider
import it.unibo.alessiociarrocchi.tesiahc.presentation.RouteDestination
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail_GPS
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail_HeartRate
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.MyScaffold


object BloodPressureDetailScreen : RouteDestination {
    override val route: String
        get() = "blood_pressure_detail_screen"
    override val title: String
        get() = "Blood Pressure Detail"

    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun BloodPressureDetailScreen(
    navController: NavController? = null,
) {

    val viewModel: BloodPressureDetailViewModel = viewModel(
        factory =
        AppViewModelProvider.provideViewModel(LocalContext.current)
    )

    val bloodDetail = viewModel.bpDetail.collectAsState()
    val heardDetail = viewModel.hrAggregate.collectAsState()
    MyScaffold(
        navController = navController,
        title = BloodPressureDetailScreen.title,
        showBackButton = true,
        navigateTo = {
            navController?.navigateUp()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                bloodDetail.value.let { bloodDetail ->
                    bloodDetail?.let {
                        BloodPressureDetail(
                            bloodDetail,
                        )

                        heardDetail.value.let { heartDetail ->
                            heartDetail?.let {
                                Text(
                                    text = stringResource(id = R.string.bp_detail_title),
                                )
                                Text(
                                    text = stringResource(id = R.string.bp_detail_hr_title),
                                )
                                BloodPressureDetail_HeartRate(
                                    heartDetail
                                )
                            }
                        }

                        Text(
                            text = "Localizzazione GPS",
                        )
                        BloodPressureDetail_GPS(
                            bloodDetail.latitude,
                            bloodDetail.longitude
                        )

                        var textDesc by remember { mutableStateOf(bloodDetail.description) }
                        val focusManager = LocalFocusManager.current
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Descrizione misurazione",
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 5,
                            value = textDesc ?: "",
                            onValueChange = { newText ->
                                textDesc = newText
                            },
                            placeholder = { Text(text = "Descrivi brevemente dove è stata svolta la misurazione e cosa si stava facendo poco prima") },
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(padding),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    viewModel.updateDescription(textDesc!!)
                                }
                            ) {
                                Text(text = "Salva info")
                            }
                        }
                    }
                }
            }
        }
    }
}