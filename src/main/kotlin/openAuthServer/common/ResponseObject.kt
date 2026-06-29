package openAuthServer.common

import kotlinx.serialization.Serializable
import org.slf4j.MDC

@Serializable
data class SingleResponseDto<T>(val responseCd: String, val responseMsg: String, val requestTs: String, val traceId: String, val data: T?)

@Serializable
data class FailResponseDto(val responseCd: String, val responseMsg: String, val requestTs: String, val traceId: String)

fun <T> getSuccessResponse(data: T): SingleResponseDto<T> {
    val traceId = MDC.get("traceId") ?: ""
    return SingleResponseDto(
        responseCd = CommonCode.SUCCESS.code,
        responseMsg = CommonCode.SUCCESS.message,
        requestTs = "",
        traceId = traceId,
        data = data
    )
}

fun getFailResponse(message: String): FailResponseDto {
    val traceId = MDC.get("traceId") ?: ""
    return FailResponseDto(
        responseCd = CommonCode.FAIL.code,
        responseMsg = message,
        requestTs = "",
        traceId = traceId
    )
}
