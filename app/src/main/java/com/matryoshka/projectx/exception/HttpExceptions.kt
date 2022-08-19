package com.matryoshka.projectx.exception

import com.google.type.DateTime

open class HttpException(message: String) : AppException(message)

//client errors
class UnauthorizedException(message: String) : HttpException(message)

class BadRequestException(message: String) : HttpException(message)

class ForbiddenException(message: String) : HttpException(message)

class NotFoundException(message: String) : HttpException(message)

//server errors
class InternalServerErrorException(message: String) : HttpException(message)

class NotImplementedException(message: String) : HttpException(message)

class ServiceUnavailableException(message: String) : HttpException(message)

data class HttpError(
    val timestamp: DateTime,
    val status: Int,
    val error: String,
    val path: String
)