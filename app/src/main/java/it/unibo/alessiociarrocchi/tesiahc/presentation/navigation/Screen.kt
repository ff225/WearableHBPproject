package it.unibo.alessiociarrocchi.tesiahc.presentation.navigation

import it.unibo.alessiociarrocchi.tesiahc.R

const val UID_NAV_ARGUMENT = "uid"

/**
 * Represent all Screens in the app.
 *
 * @param route The route string used for Compose navigation
 * @param titleId The ID of the string resource to display as a title
 * @param hasMenuItem Whether this Screen should be shown as a menu item in the left-hand menu (not
 *     all screens in the navigation graph are intended to be directly reached from the menu).
 */
enum class Screen(val route: String, val titleId: Int, val hasMenuItem: Boolean = true) {
  WelcomeScreen("welcome_screen", R.string.welcome_screen, false),
  ReadBP("read_blood_pressure", R.string.read_blood_pressure),
  ReadSleep("read_sleep", R.string.read_sleep),
  ReadLocations("readlocations", R.string.read_locations),
  BloodPressureDetail("blood_pressure_detail", R.string.blood_pressure_detail, false),
  //PrivacyPolicy("privacy_policy", R.string.privacy_policy)
}
