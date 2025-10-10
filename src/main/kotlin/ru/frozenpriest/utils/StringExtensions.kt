package ru.frozenpriest.utils

fun String.toValidFilename(): String {
    val name = trim('.', ' ')
    if (name.isEmpty()) {
        return "(invalid)"
    }
    val validName = buildString(capacity = name.length) {
        name.forEach { c ->
            if (c.isValidForFatFilename()) {
                append(c)
            } else {
                append('_')
            }
        }
    }
    // Even though vfat allows 255 UCS-2 chars, we might eventually write to
    // ext4 through a FUSE layer, so use that limit minus 15 reserved characters.
    return validName.take(240)
}

fun Char.isValidForFatFilename(): Boolean {
    if (0x00.toChar() <= this && this <= 0x1f.toChar()) {
        return false
    }
    return when (this) {
        '"', '*', '/', ':', '<', '>', '?', '\\', '|', 0x7f.toChar() -> false
        else -> true
    }
}