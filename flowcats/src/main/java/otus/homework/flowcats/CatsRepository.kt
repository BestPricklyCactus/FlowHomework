package otus.homework.flowcats

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class CatsRepository(
    private val catsService: CatsService,
    private val refreshIntervalMs: Long = 5000
) {

    fun listenForCatFacts() = flow {
        while (true) {
            try {
                val latestNews = catsService.getCatFact()
                emit(Result.Success(latestNews))
                delay(refreshIntervalMs)
            } catch (e: Exception) {
                emit(Result.Error(e.message))
            }
        }
    }
}