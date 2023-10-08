package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        val presenterScope = CoroutineScope(Dispatchers.IO + CoroutineName("CatsCoroutine"))
        presenterScope.launch {
            val fact = catsService.getCatFact()
            val newFact = Fact(createdAt = "", deleted = false, id = "myCustomId", text = "myCustom", source = "", used = false, type = "", user = "", updatedAt = "")
            withContext(Dispatchers.Main) {
                _catsView?.populate(newFact)
            }

        }
//        .enqueue(object : Callback<Fact> {
//
//            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
//                if (response.isSuccessful && response.body() != null) {
//                    _catsView?.populate(response.body()!!)
//                }
//            }
//
//            override fun onFailure(call: Call<Fact>, t: Throwable) {
//                CrashMonitor.trackWarning()
//            }
//        })
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}