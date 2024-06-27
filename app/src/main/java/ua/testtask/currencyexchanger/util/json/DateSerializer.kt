package ua.testtask.currencyexchanger.util.json

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ua.testtask.currencyexchanger.util.date.DateUtils

class DateSerializer : KSerializer<LocalDate?> {

    override val descriptor =
        PrimitiveSerialDescriptor(LocalDate::class.java.name, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate?) {
        DateUtils.formatDate(value)?.let(encoder::encodeString)
    }

    override fun deserialize(decoder: Decoder) =
        DateUtils.parseDate(decoder.decodeString())
}