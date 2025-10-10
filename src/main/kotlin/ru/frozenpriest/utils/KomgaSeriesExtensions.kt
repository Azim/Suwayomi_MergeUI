package ru.frozenpriest.utils

import java.io.File
import ru.frozenpriest.data.KomgaSeries

fun List<KomgaSeries>.getSeriesIdByPath(path: String): String? = firstOrNull { series ->
    series.url.split(File.separator).last() == path
}?.id