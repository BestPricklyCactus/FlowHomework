package otus.homework.flowcats

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    //private val _catsLiveData = MutableStateFlow(Fact("", false, "0", "", "", false, "", "", ""))
    private val _catsLiveData = MutableStateFlow<Result>(value = Result.Success<Fact>(Fact("", 0)))
    val catsFlowData: StateFlow<Result> = _catsLiveData.asStateFlow()

    init {
        viewModelScope.launch {
            catsRepository.listenForCatFacts()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _catsLiveData.emit(Result.Error(e.message.toString()))
                }
                .collect {
                    _catsLiveData.emit(it)
                }
        }
    }
}

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatsViewModel(catsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class Result {
    data class Success<T>(val data: T) : Result()
    data class Error(val msg: String?) : Result()

}
