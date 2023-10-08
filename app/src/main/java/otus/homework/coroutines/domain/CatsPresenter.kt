package otus.homework.coroutines.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.Cat
import otus.homework.coroutines.data.CatFactsService
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.Fact
import otus.homework.coroutines.Image
import otus.homework.coroutines.data.CatImagesService
import otus.homework.coroutines.presentation.ICatsView

class CatsPresenter(
    private val CatFactsService: CatFactsService,
    private val CatImagesService: CatImagesService,
    private val presenterScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
//                    throw java.net.SocketTimeoutException()
                val fact: Fact = withContext(Dispatchers.IO) {
                    CatFactsService.getCatFact()
                }
                val image: Image = withContext(Dispatchers.IO) {
                    CatImagesService.getCatImage()[0]  // TODO wrapper
                }
                Log.d("TAG", "Fact is ${fact.fact}, image is ${image.url}")
                _catsView?.populate(Cat(fact.fact, image.url))
            } catch (sockExcept: java.net.SocketTimeoutException) {
                _catsView?.showErrorToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                _catsView?.showErrorToast("${e.message}")
                Log.d("TAG", "${e.message}")
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelCoroutine() {
        presenterScope.coroutineContext.job.cancel()
    }
}