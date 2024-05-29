package it.unibo.alessiociarrocchi.tesiahc.presentation.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.presentation.theme.HealthConnectTheme

/**
 * An item in the side navigation drawer.
 */
@Composable
fun DrawerItem(
    item: Screen,
    selected: Boolean,
    onItemClick: (Screen) -> Unit,
) {
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = { onItemClick(item) })
        .height(60.dp)
        .padding(start = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = stringResource(item.titleId),
      style = MaterialTheme.typography.h5,
      color = if (selected) {
        MaterialTheme.colors.primary
      } else {
        MaterialTheme.colors.onBackground
      }
    )
  }
    Divider (
        color = Color.Gray,
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun DrawerItemPreview() {
  HealthConnectTheme {
    DrawerItem(
      item = Screen.ReadBP,
      selected = true,
      onItemClick = {}
    )
  }
}
