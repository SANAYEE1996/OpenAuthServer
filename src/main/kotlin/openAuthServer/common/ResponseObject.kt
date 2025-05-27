package openAuthServer.common

import kotlinx.serialization.Serializable

@Serializable
data class SingleResponseDto<T>(val responseCd: String, val responseMsg: String, val requestTs: String, val traceId: String, val data: T)

fun <T> getSuccessResponse(data: T): SingleResponseDto<T> {
    return SingleResponseDto(responseCd = "0000", responseMsg = "", requestTs = "", traceId = "", data = data)
}

fun <T> getFailResponse(data: T): SingleResponseDto<T> {
    return SingleResponseDto(responseCd = "9999", responseMsg = "", requestTs = "", traceId = "", data = data)
}
