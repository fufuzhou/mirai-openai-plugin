package xyz.cssxsh.openai.chat

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import xyz.cssxsh.mirai.openai.config.MiraiOpenAiConfig
import xyz.cssxsh.openai.*

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
}