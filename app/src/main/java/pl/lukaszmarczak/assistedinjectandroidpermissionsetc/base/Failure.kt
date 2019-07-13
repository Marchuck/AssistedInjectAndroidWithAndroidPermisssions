package pl.lukaszmarczak.assistedinjectandroidpermissionsetc.base

sealed class Failure : Throwable() {
    object NetworkConnection : Failure()
    object ServerError : Failure()

    abstract class FeatureFailure : Failure()
}
