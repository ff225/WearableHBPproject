package it.unibo.alessiociarrocchi.tesiahc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.HomeScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.MyScaffold
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.PermissionScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure.BloodPressureScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail.BloodPressureDetailScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.theme.HealthConnectTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HealthConnectTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                // Check permission and move from splash screen to home screen
                LaunchedEffect(Unit) {
                    val availabilityStatus =
                        HealthConnectClient.getSdkStatus(context, context.packageName)
                    if (availabilityStatus == HealthConnectClient.SDK_AVAILABLE) {
                        navController.navigate("splash_screen")
                        {
                            popUpTo("splash_screen") {
                                inclusive = true
                            }
                        }
                    }
                    if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
                        navController.navigate("health_connect_unavailability_screen")
                        {
                            popUpTo("splash_screen") {
                                inclusive = true
                            }
                        }
                    }
                    if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
                        // Optionally redirect to package installer to find a provider, for example:
                        val uriString =
                            "market://details?id=${context.packageName}&url=healthconnect%3A%2F%2Fonboarding"
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW).apply {
                                setPackage("com.android.vending")
                                data = Uri.parse(uriString)
                                putExtra("overlay", true)
                                putExtra("callerId", context.packageName)
                            }
                        )
                    }
                }
                NavHost(
                    navController = navController,
                    startDestination = "splash_screen"
                ) {
                    composable("splash_screen") {
                        SplashScreen(navController)
                    }

                    composable("health_connect_unavailability_screen") {
                        HealthConnectUnavailabilityScreen()
                    }
                    composable(HomeScreen.route) {
                        HomeScreen(navController)
                    }
                    composable(PermissionScreen.route) {
                        PermissionScreen(navController)
                    }

                    composable(BloodPressureScreen.route) {
                        BloodPressureScreen(navController)
                    }
                    composable(BloodPressureDetailScreen.route) {
                        BloodPressureDetailScreen(navController)
                    }
                    composable(LocationScreen.route) {
                        LocationScreen(navController)
                    }
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
suspend fun checkPermissionsAndRun(
    healthConnectClient: HealthConnectClient,
    navController: NavController?,
    context: Context
) {


    val granted = healthConnectClient.permissionController.getGrantedPermissions()
    if (granted.containsAll(
            setOf(
                HealthPermission.getReadPermission(HeartRateRecord::class),
                HealthPermission.getReadPermission(BloodPressureRecord::class),
                HealthPermission.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
            )
        ) &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
        &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {

        navController?.navigate(HomeScreen.route)
        {
            popUpTo("splash_screen") {
                inclusive = true
            }
        }
    } else {
        navController?.navigate(PermissionScreen.route)
        {
            popUpTo("splash_screen") {
                inclusive = true
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController? = null) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        checkPermissionsAndRun(HealthConnectClient.getOrCreate(context), navController, context)
    }

    MyScaffold(showBackButton = false) {
        Row(
            Modifier
                .padding(it)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }

    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}

@Composable
fun HealthConnectUnavailabilityScreen() {
    MyScaffold(showBackButton = false) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(it),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "HealthConnect SDK is not available on this device",
            )
        }

    }
}

@Preview
@Composable
fun HealthConnectUnavailabilityScreenPreview() {
    HealthConnectUnavailabilityScreen()
}
