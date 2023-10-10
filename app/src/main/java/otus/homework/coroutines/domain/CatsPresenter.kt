package otus.homework.coroutines.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.Cat
import otus.homework.coroutines.data.CatFactsService
import otus.homework.coroutines.data.CatImagesService
import otus.homework.coroutines.presentation.ICatsView

