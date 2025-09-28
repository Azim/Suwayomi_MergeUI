package ru.frozenpriest

import kotlinx.coroutines.runBlocking
import ru.frozenpriest.tasks.fetchNewChapters
import ru.frozenpriest.tasks.updateMangaMetadata

fun main() {
    runBlocking {
        fetchNewChapters()
        updateMangaMetadata()
    }

    /**
     * TODO: order of action
     *      Our database --> contains links komga -- suwayomi one to many (only ids, maybe with priority)
     *      Once every day (maybe) -- ideally after every suwayomi sync (check so it doesnt overlap)
     *      0) add the ability to create manga in komga (maybe when komga id not set but suwa id set in db?)
     *      1) iterate over every suwayomi manga in our database
     *      2) update name/description/image/whatever inside komga
     *      3) find all new chapters in suwayomi for this manga id
     *      4) move them to komga adding source name and translator to metadata, mark chapter as read in suwa
     *      -- Do we need time for getting new chapters? -- I think not, read mark is enough
     *      -- Is it okey to use read as mark? -- maybe,
     *          alternatives:
     *          -- add tag to suwa?
     *          -- add table of read chapter ids?
     *
     */
}