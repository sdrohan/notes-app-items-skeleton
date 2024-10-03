package utils

import models.Item
import models.Note

fun formatListString(notesToFormat: List<Note>): String =
    notesToFormat
        .joinToString(separator = "\n") { note -> "$note" }

fun formatSetString(itemsToFormat: Set<Item>): String =
    itemsToFormat
        .joinToString(separator = "\n") { item -> "\t$item" }


