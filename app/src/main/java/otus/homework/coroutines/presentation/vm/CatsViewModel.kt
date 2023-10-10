package otus.homework.coroutines.presentation.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.CatFactsService
import otus.homework.coroutines.data.CatImagesService
import otus.homework.coroutines.domain.CrashMonitor
import otus.homework.coroutines.models.Cat

class CatsViewModel(
    private val CatFactsService: CatFactsService,
    private val CatImagesService: CatImagesService
) : ViewModel() {

    private val _cat = MutableLiveData<Cat>()

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning("${e.message}")
    }
    val cat: LiveData<Cat>
        get() = _cat

    fun getCat() {

        viewModelScope.launch(exceptionHandler) {
                val factJob = async(Dispatchers.IO) {
                    CatFactsService.getCatFact()
                }
                val imageJob = async(Dispatchers.IO) {
                    CatImagesService.getCatImage()[0]  // TODO wrapper
                }
                val catResult = Cat(factJob.await().fact, imageJob.await().url)
                Log.d("TAG", "Fact is ${catResult.fact}, image is ${catResult.imageUrl}")
                _cat.value = catResult
        }
    }

    fun cancelCoroutine() {
        viewModelScope.cancel()   // cancel job + all children
    }
}

class CatsViewModelFactory(
    private val CatFactsService: CatFactsService,
    private val CatImagesService: CatImagesService
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(CatFactsService, CatImagesService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
