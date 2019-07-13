package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class NoParamsUseCase<out Type> where Type : Any {

    abstract suspend fun run(): Either<Failure, Type>

    operator fun invoke(onResult: (Either<Failure, Type>) -> Unit = {}) {

        val job = GlobalScope.async(Dispatchers.Default) { run() }
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }
}
