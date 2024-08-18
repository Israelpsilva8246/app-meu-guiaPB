package com.ufpb.meuguia.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ufpb.meuguia.R
import com.ufpb.meuguia.ui.theme.components.Destinations
import com.ufpb.meuguia.viewmodel.AttractionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAttractiveView(navController: NavController, viewModel: AttractionViewModel = viewModel()) {

    val isLoading = viewModel.isLoading.collectAsState()
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

                    if (isLoading.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x99000000)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
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
            LazyColumn {
                items(viewModel.attractionListResponse) { attractive ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp, 4.dp)
                            .fillMaxWidth()
                            .height(150.dp)
                            .clickable {
                                navController.navigate("attractiveView/${attractive.id}")
                            },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Surface {
                            Row(
                                Modifier
                                    .padding(4.dp)
                                    .fillMaxSize()
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        ImageRequest.Builder(LocalContext.current)
                                            .data(attractive.image_link)
                                            .apply {
                                                scale(Scale.FILL)
                                                transformations(CircleCropTransformation())
                                            }
                                            .build()
                                    ),
                                    contentDescription = attractive.state,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(0.2f)
                                )
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxHeight()
                                        .weight(0.8f)
                                ) {
                                    Text(
                                        text = attractive.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = attractive.city,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier
                                            .background(Color.LightGray)
                                            .padding(4.dp)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(bottom = 4.dp)
                                            .clickable {
                                                val mapLink = attractive.map_link
                                                if (mapLink.isNotEmpty() && (mapLink.startsWith("https://maps.app.goo.gl/") || mapLink.startsWith("https://goo.gl/maps/"))) {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapLink))
                                                    try {
                                                        context.startActivity(intent)
                                                    } catch (e: ActivityNotFoundException) {
                                                        Toast.makeText(context, "Nenhum aplicativo pode lidar com essa solicitação. Instale um navegador da web ou aplicativo de mapas", Toast.LENGTH_LONG).show()
                                                    } catch (e: Exception) {
                                                        Toast.makeText(context, "Link do maps inválido.", Toast.LENGTH_SHORT).show()
                                                    }
                                                } else {
                                                    Toast.makeText(context, "Link do maps inválido.", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Location Icon",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
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
        text = stringResource(R.string.refresh),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                viewModel.getAttractionList()
            }
    )

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


