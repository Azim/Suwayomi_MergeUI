package ru.frozenpriest.environment


object Environment {

    val SUWAYOMI_URL by lazy {
        requireEnv("SUWAYOMI_URL")
    }
}

private fun requireEnv(name: String) = requireNotNull(System.getenv(name)) {
    "$name is null"
}
