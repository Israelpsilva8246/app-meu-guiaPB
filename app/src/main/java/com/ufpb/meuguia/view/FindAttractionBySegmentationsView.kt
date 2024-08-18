import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FmdGood
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.ufpb.meuguia.R
import com.ufpb.meuguia.ui.theme.components.NavigationIconButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindAttractionBySegmentationsView(
    navController: NavController,
    viewModel: FindAttractionViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
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
                            text = stringResource(R.string.find_attraction),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        NavigationIconButton(
                            navController = navController,
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button),
                            navigateUp = true
                        )
                    }
                )
            },
            content = {
                FindAttractionBySegmentations(
                    modifier = Modifier.padding(it),
                    navController,
                    viewModel
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindAttractionBySegmentations(
    modifier: Modifier,
    navController: NavController,
    viewModel: FindAttractionViewModel = viewModel()
) {
    var isFetching by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val segmentation: List<String> = runBlocking {
        viewModel.fetchAttractionSegName()
    }
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(segmentation[0]) }


    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose {
            isFetching = false
            showDetails = false
        }
    }

    Column(modifier = modifier.padding(start = 8.dp, end = 8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Selecione a segmentação turistica",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .background(Color(0xFFEAEEED)),
                    value = selectedText,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    segmentation.forEachIndexed { index, text ->
                        DropdownMenuItem(
                            text = { Text(text = text) },
                            onClick = {
                                selectedText = segmentation[index]
                                isExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                isFetching = true
                showDetails = true
                viewModel.fetchAttractionBySegmentations(selectedText)
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Buscar")
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (isFetching && !showDetails) {
                CircularProgressIndicator()
            }
        }

        if (showDetails) {
            LaunchedEffect(viewModel) {
                if (isFetching) {
                    viewModel.fetchAttractionBySegmentations(selectedText)
                    isFetching = false
                }
            }

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
                                    painter = rememberImagePainter(
                                        data = attractive.image_link,
                                        builder = {
                                            scale(Scale.FILL)
                                            transformations(CircleCropTransformation())
                                        }),
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
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FmdGood,
                                            contentDescription = "Attraction Location",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(bottom = 4.dp)
                                                .clickable {
                                                    val intent = Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse(attractive.map_link)
                                                    ).apply {
                                                        setPackage("com.google.android.apps.maps")
                                                    }
                                                    context.startActivity(intent)
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (!isFetching) {


            }
        }
    }
}

