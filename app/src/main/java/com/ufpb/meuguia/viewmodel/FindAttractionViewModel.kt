import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufpb.meuguia.model.Attraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FindAttractionViewModel : ViewModel() {
    private val apiService = ApiService.getInstance()


    var attractionListResponse: List<Attraction> by mutableStateOf(listOf())

    fun fetchAttractionByName(name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                attractionListResponse = apiService.getAttractionByName(name)
            } catch (e: Exception) {
                println("Erro ao filtrar ${e.message}")
            }
        }
    }

    fun fetchAttractionBySegmentations(segmentations: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                attractionListResponse = apiService.getAttractionBySegmentations(segmentations)
            } catch (e: Exception) {
                println("Erro ao filtrar ${e.message}")
            }
        }
    }

    fun fetchAttractionByCity(city: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                attractionListResponse = apiService.getAttractionByCity(city)
            } catch (e: Exception) {
                println("Erro ao filtrar ${e.message}")
            }
        }
    }

    fun fetchAttractionByType(attractionTypes: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                attractionListResponse = apiService.getAttractionByType(attractionTypes)
            } catch (e: Exception) {
                println("Erro ao filtrar ${e.message}")
            }
        }
    }

    suspend fun fetchAttractionTypeNames(): List<String> {
        val attractionTypes = apiService.getAttractionTypes()
        return attractionTypes.map { it.name }
    }

    suspend fun fetchAttractionNames(): List<String> {
        val attractionNames = apiService.getAttractionName()
        return attractionNames.map { it.name }
    }

    suspend fun fetchAttractionSegName(): List<String> {
        val attractionSeg = apiService.getAttractionSeg()
        return attractionSeg.map { it.name }
    }

    suspend fun fetchAttractionCity(): List<String> {
        val attractions = apiService.getAttractions()
        return attractions.map { it.city }.toSet().toList()
    }
}
