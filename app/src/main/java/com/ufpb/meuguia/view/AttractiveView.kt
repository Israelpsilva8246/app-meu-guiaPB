package com.ufpb.meuguia.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.ufpb.meuguia.R
import com.ufpb.meuguia.model.Attraction
import com.ufpb.meuguia.ui.theme.components.NavigationIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionView(navController: NavController, attraction: Attraction) {
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
                            text = attraction.name,
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
                AttractionDetail(
                    modifier = Modifier.padding(it),
                    attraction = attraction,
                    navController = navController
                )
            }
        )
    }
}

@Composable
fun AttractionDetail(
    modifier: Modifier,
    attraction: Attraction,
    navController: NavController
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Spacer(modifier = Modifier.height(60.dp)) // Espaçamento entre a TopBar e a imagem

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Surface(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = attraction.image_link)
                                    .apply(block = fun ImageRequest.Builder.() {
                                        scale(Scale.FILL)
                                    }).build()
                            ),
                            contentDescription = attraction.description,
                            contentScale = ContentScale.Crop, // Adicionado para garantir que a imagem preencha todo o espaço horizontal
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )


                        Text(
                            text = "Foto: ${attraction.fonte}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = attraction.name,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            Text(
                                text = "Estado: ${attraction.state}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 4.dp),
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Cidade: ${attraction.city}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 4.dp),
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Segmentações: ${attraction.segmentations.joinToString { it.name }}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 4.dp),
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Tipo: ${attraction.attractionTypes.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 4.dp),
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .clickable {
                                        val mapLink = attraction.map_link
                                        if (mapLink.isNotEmpty() && (mapLink.startsWith("https://maps.app.goo.gl/") || mapLink.startsWith(
                                                "https://goo.gl/maps/"
                                            ))
                                        ) {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(mapLink)
                                            )
                                            try {
                                                context.startActivity(intent)
                                            } catch (e: ActivityNotFoundException) {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Nenhum aplicativo pode lidar com essa solicitação. Instale um navegador da web ou aplicativo de mapas",
                                                        Toast.LENGTH_LONG
                                                    )
                                                    .show()
                                            } catch (e: Exception) {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Link do maps inválido.",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            }
                                        } else {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Link do maps inválido.",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                            ) {
                                Text(
                                    text = "Google Maps: ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location Icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Descrição:",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Text(
                                text = attraction.description,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }


                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            if (attraction.moreInfoLinkList.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                ) {
                                    Text(
                                        text = "Para mais informações acesse: ",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 4.dp),
                                    )
                                }

                                attraction.moreInfoLinkList.forEach { moreInfoLink ->
                                    Text(
                                        text = "- ${moreInfoLink.description}",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.clickable {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(moreInfoLink.link)
                                            )
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
    }
}


