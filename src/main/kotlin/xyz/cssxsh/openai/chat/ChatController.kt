package xyz.cssxsh.openai.chat

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
//import xyz.cssxsh.mirai.openai.config.MiraiOpenAiConfig
import xyz.cssxsh.openai.*
import java.io.File

/**
 * [Chat](https://platform.openai.com/docs/api-reference/chat)
 * @since 1.2.0
 */
public class ChatController(private val client: OpenAiClient) {

    /**
     * [Create chat completion](https://platform.openai.com/docs/api-reference/chat/create)
     */
    public suspend fun create(request: ChatRequest): ChatInfo {
        //val response = client.http.post("https://api.deepseek.com/v1/chat/completions") {
        val response = client.http.post("https://api.openai.com/v1/chat/completions") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return response.body()
    }

    /**
     * [Create completion](https://platform.openai.com/docs/api-reference/chat/create)
     */
    public suspend fun create(model: String, block: ChatRequest.Builder.() -> Unit): ChatInfo {
        return create(request = ChatRequest.Builder(model = model).apply(block).build())
    }

    public suspend fun create(
        request: ChatRequest,
        apiBaseUrl: String = "https://api.openai.com/v1"
    ): ChatInfo {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true  // 开启宽松模式
            prettyPrint = true
        }
        val url = "$apiBaseUrl/chat/completions"
        val response = try {
            client.http.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        } catch (e: Exception) {
            // 出错时将保存的 JSON 写入文件，便于调试
            val jsonString = json.encodeToString(request)
            File("/root/dice/data/request_debug.txt").writeText(jsonString)
            throw e  // 或者根据实际情况处理异常
        }
        //return response.body()
        // 先获取响应的原始文本内容
        val rawResponse = response.bodyAsText()

        // 预处理响应文本：去掉空行（也可以根据需要去掉注释行）
        val cleanedResponse = rawResponse.lineSequence()
            .filter { it.trim().isNotEmpty() } // 保留非空行
            .joinToString("\n")

        return try {
            // 尝试将响应文本反序列化为 ChatInfo 对象
            json.decodeFromString<ChatInfo>(cleanedResponse)
        } catch (e: Exception) {
            // 如果反序列化失败，将响应文本写入调试文件

            // 构造完整响应日志
            val fullResponse = buildString {
                appendLine("HTTP Version: ${response.version}")
                appendLine("Status: ${response.status}")
                appendLine("Headers:")
                response.headers.forEach { key, values ->
                    appendLine("  $key: ${values.joinToString(", ")}")
                }
                appendLine("Body:")
                appendLine(rawResponse)
            }

            // 将完整响应写入调试文件
            File("/root/dice/data/response_debug.txt").writeText(fullResponse)
            // 可选：记录日志、处理异常，最后抛出异常让上层知晓
            throw e
        }
    }

    public suspend fun create(
        model: String,
        apiBaseUrl: String,
        block: ChatRequest.Builder.() -> Unit
    ): ChatInfo {
        val request = ChatRequest.Builder(model = model).apply(block).build()
        return create(request = request, apiBaseUrl = apiBaseUrl)
    }

}