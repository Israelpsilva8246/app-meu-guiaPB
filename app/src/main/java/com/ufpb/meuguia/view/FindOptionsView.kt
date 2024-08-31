package com.ufpb.meuguia.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ufpb.meuguia.R
import com.ufpb.meuguia.ui.theme.components.MainButton
import com.ufpb.meuguia.ui.theme.components.NavigationIconButton
import com.ufpb.meuguia.utils.getMainItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindOptionsView(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.find_options_view)) },
                navigationIcon = {
                    NavigationIconButton(
                        navController = navController,
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        navigateUp = true
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        content = {
            FindGrid(
                modifier = Modifier.padding(it),
                navController = navController
            )
        }
    )
}


@Composable
fun FindGrid(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val itemsList = getMainItems()

    if (itemsList.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = modifier.padding(20.dp),
            columns = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(itemsList) { item ->
                MainButton(
                    text = item.text,
                    onClick = {
                        if (item.nav.isNotEmpty()) {
                            navController.navigate(item.nav)
                        }
                    }
                )
            }
        }
    } else {
        // Exibe uma mensagem ou visualização alternativa quando a lista estiver vazia
        Text(
            text = stringResource(id = R.string.atrativo_nao_encontrado),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(16.dp)
        )
    }
}


