package openAuthServer.common

class CustomException(
    val error: CommonCode,
    override val message: String = error.message
) : RuntimeException(message)