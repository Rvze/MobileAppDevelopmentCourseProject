package recognitioncommons.util.extractor

import recognitioncommons.exception.InvalidAirPodsIdException
import recognitioncommons.models.apple.AppleColor
import recognitioncommons.models.apple.airpods.AirPodsFullModel
import recognitioncommons.models.apple.airpods.AirPodsModel
import recognitioncommons.models.country.Country

fun airPodsFullModelFromId(id: String, country: Country): AirPodsFullModel {
    val parts = id.split("/")
    if (parts.size !in setOf(1, 2))
        throw InvalidAirPodsIdException(id)

    val color = if (parts.size == 2) AppleColor.valueOf(parts[1]) else AppleColor.DEFAULT

    return try {
        AirPodsFullModel(
            model = AirPodsModel.valueOf(parts[0]),
            color = color,
            country = country
        )
    } catch (e: Exception) {
        throw InvalidAirPodsIdException(id)
    }
}
