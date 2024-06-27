package ua.testtask.currencyexchanger.util.date

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat

object DateUtils {

    /**
     * 2024-06-26
     */
    private val SIMPLE_DATE_FORMATTER: DateTimeFormat<LocalDate> by lazy {
        LocalDate.Format { date(LocalDate.Formats.ISO) }
    }

    fun parseDate(stringDate: String?): LocalDate? =
        stringDate?.let { LocalDate.parse(it, SIMPLE_DATE_FORMATTER) }

    fun formatDate(dateTime: LocalDate?): String? =
        dateTime?.format(SIMPLE_DATE_FORMATTER)
}
