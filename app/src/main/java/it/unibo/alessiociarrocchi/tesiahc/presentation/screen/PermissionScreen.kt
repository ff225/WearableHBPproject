package it.unibo.alessiociarrocchi.tesiahc.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.navigation.NavController
import androidx.work.WorkManager
import it.unibo.alessiociarrocchi.tesiahc.presentation.RouteDestination
import it.unibo.alessiociarrocchi.tesiahc.setupPeriodicWork

object PermissionScreen : RouteDestination {
    override val route: String = "permission_screen"
    override val title: String = "Grant Permission"
}

@SuppressLint("RestrictedApi")
@Composable
fun PermissionScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            //Manifest.permission.ACCESS_BACKGROUND_LOCATION

        )
    } else {
        arrayOf(
            //Manifest.permission.ACTIVITY_RECOGNITION,
            //Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

    val locationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

    val locationBackgroundPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    } else {
        arrayOf()
    }


    var missingPermissions by remember { mutableStateOf(permissions.toList()) }
    var missingLocationPermission by remember { mutableStateOf(locationPermission.toList()) }
    var missingLocationBackgroundPermission by remember {
        mutableStateOf(
            locationBackgroundPermission.toList()
        )
    }
    var areHealthPermissionsGranted by remember { mutableStateOf(false) }


    val launcherStandardPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            missingPermissions = results.filter { !it.value }.map { it.key }
        }

    val launcherLocationPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            missingLocationPermission = results.filter { !it.value }.map { it.key }
        }

    val launcherLocationBackgroundPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            missingLocationBackgroundPermission = results.filter { !it.value }.map { it.key }
        }


    val requestPermissionActivityContract =
        PermissionController.createRequestPermissionResultContract()

    val launcherHealthPermissions =
        rememberLauncherForActivityResult(requestPermissionActivityContract) { isGranted ->

            areHealthPermissionsGranted = isGranted.containsAll(
                setOf(
                    HealthPermission.getReadPermission(HeartRateRecord::class),
                    HealthPermission.getReadPermission(BloodPressureRecord::class),
                    HealthPermission.Companion.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
                )
            )
            Log.d(
                "PermissionScreen",
                "Health permissions granted: $areHealthPermissionsGranted"
            )
        }

    LaunchedEffect(Unit) {

        if (missingPermissions.isNotEmpty()) {
            launcherStandardPermissions.launch(missingPermissions.toTypedArray())
        }
        if (missingLocationPermission.isNotEmpty()) {
            launcherLocationPermissions.launch(missingLocationPermission.toTypedArray())
        }
    }


    LaunchedEffect(missingPermissions, areHealthPermissionsGranted, missingLocationPermission) {
        if (missingPermissions.isEmpty() && areHealthPermissionsGranted && missingLocationPermission.isEmpty()) {
            setupPeriodicWork(workManager = WorkManager.getInstance(context))
            navController?.navigate(HomeScreen.route) {
                popUpTo(PermissionScreen.route) { inclusive = true }
            }
        }
    }

    MyScaffold(navController = navController) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                text = "Grant permission to allow the app to work properly",
                modifier = Modifier.padding(8.dp)
            )

            Log.d("PermissionScreen", "Missing permissions: $missingPermissions")
            Log.d("PermissionScreen", "Missing location permissions: $missingLocationPermission")
            Log.d(
                "PermissionScreen",
                "Missing location background permissions: $missingLocationBackgroundPermission"
            )

            Button(onClick = {

                if (missingPermissions.isNotEmpty()) {
                    launcherStandardPermissions.launch(missingPermissions.toTypedArray())
                } else if (missingLocationPermission.isNotEmpty()) {
                    launcherLocationPermissions.launch(missingLocationPermission.toTypedArray())
                } else if (missingLocationBackgroundPermission.isNotEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        launcherLocationBackgroundPermissions.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                    }
                } else
                    launcherHealthPermissions.launch(
                        setOf(
                            HealthPermission.getReadPermission(HeartRateRecord::class),
                            HealthPermission.getReadPermission(BloodPressureRecord::class),
                            HealthPermission.Companion.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
                        )
                    )
            }) {
                Text(text = "Grant Permission")
            }


        }
    }
}


@Preview
@Composable
fun PermissionScreenPreview() {
    PermissionScreen(navController = null)
}