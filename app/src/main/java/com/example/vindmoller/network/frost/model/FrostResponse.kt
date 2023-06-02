package com.example.vindmoller.network.frost.model

import kotlinx.serialization.Serializable


/* TODO: we can add parameteres to the request such as `fields` in order to
    limit recieved data. This may result in a different serializer class.
 */
// data class for FROST response
@Serializable
data class FrostResponse(
    val data: List<FrostDatapoint>
) {
    @Serializable
    data class FrostDatapoint(val sourceId: String, val referenceTime: String, val observations: List<Observation>)

    @Serializable
    data class Observation(val elementId: String, val value: Float)
}
