package ru.frozenpriest.environment

object Environment {

    const val SUWAYOMI_LIBRARY_PATH = "/suwayomi"
    const val KOMGA_LIBRARY_PATH = "/komga"

    val SUWAYOMI_URL by lazy {
        requireEnv("SUWAYOMI_URL")
    }

    val KOMGA_URL by lazy {
        requireEnv("KOMGA_URL")
    }

    val KOMGA_API_KEY by lazy {
        requireEnv("KOMGA_API_KEY")
    }

    val KOMGA_LIBRARY_NAME by lazy {
        requireEnv("KOMGA_LIBRARY_NAME")
    }
}

private fun requireEnv(name: String) = requireNotNull(System.getenv(name)) {
    "$name is null"
}
