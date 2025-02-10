package xyz.cssxsh.openai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import xyz.cssxsh.openai.chat.*
import xyz.cssxsh.openai.completion.*
import xyz.cssxsh.openai.edit.*
import xyz.cssxsh.openai.embedding.*
import xyz.cssxsh.openai.file.*
import xyz.cssxsh.openai.finetune.*
import xyz.cssxsh.openai.image.*
import xyz.cssxsh.openai.model.*
import xyz.cssxsh.openai.moderation.*
import java.io.File

public open class OpenAiClient(internal val config: OpenAiClientConfig) {
    @OptIn(ExperimentalSerializationApi::class)
    public open val http: HttpClient = HttpClient(OkHttp) {
//        install(ContentNegotiation) {
//            json(json = Json)
//        }
        install(ContentNegotiation) {
            json(Json {
                // 设置 explicitNulls = false，使得值为 null 的字段不会被序列化
                explicitNulls = false
                ignoreUnknownKeys = true // 忽略未知字段
                isLenient = true // 宽松模式
            })
        }
        install(HttpTimeout) {
            socketTimeoutMillis = config.timeout
            connectTimeoutMillis = config.timeout
            requestTimeoutMillis = null
        }
        Auth {
            bearer {
                loadTokens {
                    BearerTokens(config.token, "")
                }
                refreshTokens {
                    BearerTokens(config.token, "")
                }
//                sendWithoutRequest { builder ->
//                    builder.url.host == "api.openai.com"
//                }
                sendWithoutRequest { builder ->
                    builder.url.host == "api.deepseek.com"
                }
            }
        }
        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value
                val originCall = response.call
                if (statusCode < 400) return@validateResponse

                val exceptionCall = originCall.save()
                val exceptionResponse = exceptionCall.response

                throw try {
                    val error = exceptionResponse.body<ErrorInfoWrapper>().error
                    OpenAiException(info = error)
                } catch (_: ContentConvertException) {
                    val exceptionResponseText = try {
                        exceptionResponse.bodyAsText()
                    } catch (_: MalformedInputException) {
                        "<body failed decoding>"
                    }
                    when (statusCode) {
                        in 400..499 -> {
                            ClientRequestException(response, exceptionResponseText)
                        }
                        in 500..599 -> {
                            ServerResponseException(response, exceptionResponseText)
                        }
                        else -> ResponseException(response, exceptionResponseText)
                    }
                }
            }
        }
        BrowserUserAgent()
        ContentEncoding()
        engine {
            config {
                apply(config = config)
            }
        }

    }
    @OptIn(ExperimentalSerializationApi::class)
    public open val http2: HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                // 设置 explicitNulls = false，使得值为 null 的字段不会被序列化
                explicitNulls = false
                ignoreUnknownKeys = true // 忽略未知字段
                isLenient = true // 宽松模式
            })
        }
        install(HttpTimeout) {
            socketTimeoutMillis = config.timeout
            connectTimeoutMillis = config.timeout
            requestTimeoutMillis = null
        }
        Auth {
            bearer {
                loadTokens {
                    BearerTokens(config.imagetoken, "")
                }
                refreshTokens {
                    BearerTokens(config.imagetoken, "")
                }
            }
        }
        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value
                val originCall = response.call
                if (statusCode < 400) return@validateResponse

                val exceptionCall = originCall.save()
                val exceptionResponse = exceptionCall.response

                throw try {
                    val error = exceptionResponse.body<ErrorInfoWrapper>().error
                    OpenAiException(info = error)
                } catch (_: ContentConvertException) {
                    val exceptionResponseText = try {
                        exceptionResponse.bodyAsText()
                    } catch (_: MalformedInputException) {
                        "<body failed decoding>"
                    }
                    when (statusCode) {
                        in 400..499 -> {
                            ClientRequestException(response, exceptionResponseText)
                        }
                        in 500..599 -> {
                            ServerResponseException(response, exceptionResponseText)
                        }
                        else -> ResponseException(response, exceptionResponseText)
                    }
                }
            }
        }
        BrowserUserAgent()
        ContentEncoding()
        engine {
            config {
                apply(config = config)
            }
        }

    }
    public open val model: ModelController by lazy { ModelController(this) }
    public open val completion: CompletionController by lazy { CompletionController(this) }
    public open val edit: EditController by lazy { EditController(this) }
    public open val image: ImageController by lazy { ImageController(this) }
    public open val embedding: EmbeddingController by lazy { EmbeddingController(this) }
    public open val file: FileController by lazy { FileController(this) }
    public open val finetune: FineTuneController by lazy { FineTuneController(this) }
    public open val moderation: ModerationController by lazy { ModerationController(this) }
    public open val chat: ChatController by lazy { ChatController(this) }

    /**
     * @since 1.0.7
     */
    public open fun clearToken() {
        for (provider in http.plugin(Auth).providers) {
            if (provider !is BearerAuthProvider) continue
            provider.clearToken()
            break
        }
        for (provider in http2.plugin(Auth).providers) {
            if (provider !is BearerAuthProvider) continue
            provider.clearToken()
            break
        }
    }
}