package com.ufpb.meuguia

import ApiService
import FindAttractionByCityView
import FindAttractionByNameView
import FindAttractionBySegmentationsView
import FindAttractionByTypeView
import FindAttractionViewModel
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ufpb.meuguia.model.Attraction
import com.ufpb.meuguia.ui.theme.MeuGuiaTheme
import com.ufpb.meuguia.ui.theme.components.Destinations
import com.ufpb.meuguia.view.AboutView
import com.ufpb.meuguia.view.AttractionView
import com.ufpb.meuguia.view.FindOptionsView
import com.ufpb.meuguia.view.ListAttractiveView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeuGuiaTheme {
                val navController = rememberNavController()
                val apiService = ApiService.getInstance()
                var attractives by remember { mutableStateOf(emptyList<Attraction>()) }
                val viewModel = FindAttractionViewModel()

                LaunchedEffect(key1 = Unit) {
                    attractives = apiService.getAttractions()
                }
                NavHost(navController = navController, startDestination = Destinations.LIST.name) {
                    composable(route = Destinations.LIST.name) {
                        ListAttractiveView(navController)
                    }
                    composable(route = Destinations.FIND.name) {
                        FindAttractionByNameView(navController, viewModel)
                    }
                    composable(route = Destinations.FINDBYCITY.name) {
                        FindAttractionByCityView(navController, viewModel)
                    }
                    composable(route = Destinations.FINDBYSEG.name) {
                        FindAttractionBySegmentationsView(navController, viewModel)
                    }
                    composable(route = Destinations.FINDBYTYPE.name) {
                        FindAttractionByTypeView(navController, viewModel)
                    }
                    composable(route = Destinations.FINDOPTIONS.name) {
                        FindOptionsView(navController)
                    }
                    composable(route = Destinations.ABOUT.name) {
                        AboutView(navController)
                    }
                    composable(route = "attractiveView/{attractiveId}") { backStackEntry ->
                        val attractiveId = backStackEntry.arguments?.getString("attractiveId")
                        val selectedAttractive = attractives.find { it.id == attractiveId }
                        if (selectedAttractive != null) {
                            AttractionView(navController, selectedAttractive)
                        } else {
                            Toast.makeText(
                                LocalContext.current,
                                "Atrativo não encontrado.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        }
    }
}
