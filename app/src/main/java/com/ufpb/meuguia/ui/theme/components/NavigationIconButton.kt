package com.ufpb.meuguia.ui.theme.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun NavigationIconButton(
    navController: NavController,
    icon: ImageVector,
    contentDescription: String,
    navigateUp: Boolean = false
) {
    IconButton(
        onClick = {
            if (navigateUp) {
                navController.navigateUp()
            } else {
                navController.popBackStack()
            }
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
