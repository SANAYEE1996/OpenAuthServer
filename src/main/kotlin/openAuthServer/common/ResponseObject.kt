package openAuthServer.common

import kotlinx.serialization.Serializable
import org.slf4j.MDC
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class SingleResponseDto<T>(val responseCd: String, val responseMsg: String, val requestTs: String, val traceId: String, val data: T?)

@Serializable
data class FailResponseDto(val responseCd: String, val responseMsg: String, val requestTs: String, val traceId: String)

private val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

fun <T> getSuccessResponse(data: T): SingleResponseDto<T> {
    val traceId = MDC.get("traceId") ?: ""
    val nowTime = LocalDateTime.now().format(timestampFormatter)
    return SingleResponseDto(
        responseCd = CommonCode.SUCCESS.code,
        responseMsg = CommonCode.SUCCESS.message,
        requestTs = nowTime,
        traceId = traceId,
        data = data
    )
}

fun getFailResponse(
    message: String,
    code: String = CommonCode.FAIL.code
): FailResponseDto {
    val traceId = MDC.get("traceId") ?: ""
    val nowTime = LocalDateTime.now().format(timestampFormatter)
    return FailResponseDto(
        responseCd = code,
        responseMsg = message,
        requestTs = nowTime,
        traceId = traceId
    )
}
