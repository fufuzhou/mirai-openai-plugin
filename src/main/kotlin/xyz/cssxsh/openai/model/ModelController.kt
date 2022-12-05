package xyz.cssxsh.openai.model

import io.ktor.client.call.*
import io.ktor.client.request.*
import xyz.cssxsh.openai.*

/**
 * [Models](https://beta.openai.com/docs/api-reference/models)
 */
public class ModelController(private val client: OpenAiClient) {

    /**
     * [List models](https://beta.openai.com/docs/api-reference/models/list)
     */
    public suspend fun list(): List<ModelInfo> {
        val response = client.http.get( "https://api.openai.com/v1/models")
        val body = response.body<ListWrapper<ModelInfo>>()

        return body.data
    }

    /**
     * [Retrieve model](https://beta.openai.com/docs/api-reference/models/retrieve)
     */
    public suspend fun retrieve(model: String): ModelInfo {
        val response = client.http.get( "https://api.openai.com/v1/models/$model")

        return response.body()
    }
}