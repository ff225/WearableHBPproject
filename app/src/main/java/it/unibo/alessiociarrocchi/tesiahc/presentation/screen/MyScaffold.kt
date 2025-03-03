package it.unibo.alessiociarrocchi.tesiahc.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure.BloodPressureScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(
    title: String = stringResource(id = R.string.app_name),
    navController: NavController? = null,
    showBackButton: Boolean = false,
    navigateTo: () -> Unit = {},
    actions: @Composable() (RowScope.() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "WearableHBP",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(label = { Text("Blood pressure list") },
                        selected = false,
                        onClick = {
                            navController?.navigate(BloodPressureScreen.route)
                        })
                    NavigationDrawerItem(label = { Text("Location list") },
                        selected = false,
                        onClick = {
                            navController?.navigate(LocationScreen.route)
                        })
                }
            }
        }, drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = title) }, navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = navigateTo) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }

                }, actions = actions
                )

            }, content = content
        )
    }
}

@Preview
@Composable
fun MyScaffoldPreview() {
    MyScaffold(showBackButton = true) {
        Text("Content", modifier = Modifier.padding(it))
    }
}