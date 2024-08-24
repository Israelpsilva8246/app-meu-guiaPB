package com.ufpb.meuguia.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ufpb.meuguia.R
import com.ufpb.meuguia.ui.theme.components.Destinations
import com.ufpb.meuguia.viewmodel.AttractionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAttractiveView(navController: NavController, viewModel: AttractionViewModel = viewModel()) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navController, viewModel)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.menu),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                )
            },
            content = {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    ListAttraction(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        )
    }
}


@Composable
fun ListAttraction(
    modifier: Modifier,
    viewModel: AttractionViewModel,
    navController: NavController,
) {
    val isLoading = viewModel.isLoading.collectAsState().value
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    LaunchedEffect(viewModel) {
        viewModel.getAttractionList()
    }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEAEEED)),
        verticalArrangement = Arrangement.Top
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.listAttractiveView),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.getAttractionList()
            }
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                items(viewModel.attractionListResponse) { attractive ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                navController.navigate("attractiveView/${attractive.id}")
                            },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Surface {
                            Column(
                                Modifier
                                    .padding(8.dp) // Aumentar o padding para mais espaço
                                    .fillMaxSize()
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(attractive.image_link)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = stringResource(R.string.app_name),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp) // Aumentar a altura para dar mais espaço ao texto abaixo
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Column {
                                    Text(
                                        text = attractive.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier
                                            .padding(top = 12.dp) // Mais espaço acima do texto
                                            .align(Alignment.CenterHorizontally)
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


@Composable
fun DrawerContent(
    navController: NavController,
    viewModel: AttractionViewModel,
) {
    Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(16.dp)
    )

    HorizontalDivider()

    Text(
        text = stringResource(R.string.find_options_view),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                navController.navigate(Destinations.FINDOPTIONS.name)
            }
    )

    Text(
        text = stringResource(R.string.sobre),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                navController.navigate(Destinations.ABOUT.name)
            }
    )
}


