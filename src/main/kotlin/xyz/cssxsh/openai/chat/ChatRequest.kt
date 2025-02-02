package xyz.cssxsh.openai.chat

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import xyz.cssxsh.openai.*

/**
 * @param messages [chat-create-messages](https://platform.openai.com/docs/api-reference/chat/create#chat-create-messages)
 * @param model [chat-create-model](https://platform.openai.com/docs/api-reference/chat/create#chat-create-model)
 * @param frequencyPenalty [chat-create-frequency_penalty](https://platform.openai.com/docs/api-reference/chat/create#chat-create-frequency_penalty)
 * @param logitBias [chat-create-logit_bias](https://platform.openai.com/docs/api-reference/chat/create#chat-create-logit_bias)
 * @param maxTokens [chat-create-max_tokens](https://platform.openai.com/docs/api-reference/chat/create#chat-create-max_tokens)
 * @param number [chat-create-n](https://platform.openai.com/docs/api-reference/chat/create#chat-create-n)
 * @param presencePenalty [chat-create-presence_penalty](https://platform.openai.com/docs/api-reference/chat/create#chat-create-presence_penalty)
 * @param seed [chat-create-seed](https://platform.openai.com/docs/api-reference/chat/create#chat-create-seed)
 * @param stop [chat-create-stop](https://platform.openai.com/docs/api-reference/chat/create#chat-create-stop)
 * @param stream [chat-create-stream](https://platform.openai.com/docs/api-reference/chat/create#chat-create-stream)
 * @param temperature [chat-create-temperature](https://platform.openai.com/docs/api-reference/chat/create#chat-create-temperature)
 * @param topP [chat-create-top_p](https://platform.openai.com/docs/api-reference/chat/create#chat-create-top_p)
 * @param tools [chat-create-tools](https://platform.openai.com/docs/api-reference/chat/create#chat-create-tools)
 * @param choice [chat-create-tool_choice](https://platform.openai.com/docs/api-reference/chat/create#chat-create-tool_choice)
 * @param user [chat-create-user](https://platform.openai.com/docs/api-reference/chat/create#chat-create-user)
 * @param call [chat-create-function_call](https://platform.openai.com/docs/api-reference/chat/create#chat-create-function_call)
 * @param functions [chat-create-functions](https://platform.openai.com/docs/api-reference/chat/create#chat-create-functions)
 */
@Serializable
public data class ChatRequest(
    @SerialName("model")
    val model: String,
    @SerialName("messages")
    val messages: List<ChoiceMessage>,
    @SerialName("tools")
    val tools: List<ChoiceTool>? = null,
    @SerialName("tool_choice")
    val choice: JsonElement = JsonNull,
    @SerialName("temperature")
    val temperature: Double = 1.0,
    @SerialName("top_p")
    val topP: Double = 1.0,
    @SerialName("n")
    val number: Int = 1,
    @SerialName("seed")
    val seed: Int? = null,
    @SerialName("stream")
    val stream: Boolean = false,
    @SerialName("stop")
    val stop: List<String>? = null,
    @SerialName("max_tokens")
    val maxTokens: Int = 4096,
    @SerialName("max_completion_tokens")
    val max_completion_tokens: Int = 4096,
    @SerialName("presence_penalty")
    val presencePenalty: Double = 0.0,
    @SerialName("frequency_penalty")
    val frequencyPenalty: Double = 0.0,
    @SerialName("logit_bias")
    val logitBias: Map<String, Int>? = null,
    @SerialName("user")
    val user: String = "",
    @SerialName("functions")
    @Deprecated("Deprecated in favor of tools.")
    val functions: List<ChoiceFunction>? = null,
    @SerialName("function_call")
    @Deprecated("Deprecated in favor of tool_choice.")
    val call: JsonElement = JsonNull,
) {
    public class Builder(@property:OpenAiDsl public var model: String) {
        @OpenAiDsl
        public fun model(value: String): Builder = apply {
            model = value
        }

        @OpenAiDsl
        public var messages: List<ChoiceMessage> = emptyList()

        @OpenAiDsl
        public fun messages(values: List<ChoiceMessage>): Builder = apply {
            messages = values
        }

        @OpenAiDsl
        public fun messages(vararg values: ChoiceMessage): Builder = apply {
            messages = values.asList()
        }

        @OpenAiDsl
        public fun messages(block: MutableList<ChoiceMessage>.() -> Unit): Builder = apply {
            messages = buildList(block)
        }

        @OpenAiDsl
        public fun MutableList<ChoiceMessage>.system(content: String): Boolean {
            return add(ChoiceMessage(role = "system", content = content))
        }

        @OpenAiDsl
        public fun MutableList<ChoiceMessage>.user(content: String): Boolean {
            return add(ChoiceMessage(role = "user", content = content))
        }

        @OpenAiDsl
        public fun MutableList<ChoiceMessage>.assistant(content: String): Boolean {
            return add(ChoiceMessage(role = "system", content = content))
        }

        @OpenAiDsl
        @Deprecated("Deprecated in favor of tools.")
        public var functions: List<ChoiceFunction> = emptyList()

        @OpenAiDsl
        public fun functions(values: List<ChoiceFunction>): Builder = apply {
            tools {
                for (function in values) {
                    add(ChoiceTool(type = "function", function = function))
                }
            }
        }

        @OpenAiDsl
        public fun functions(vararg values: ChoiceFunction): Builder = functions(values = values.asList())

        @OpenAiDsl
        public fun functions(block: MutableList<ChoiceFunction>.() -> Unit): Builder = functions(values = buildList(block))

        @OpenAiDsl
        public fun MutableList<ChoiceFunction>.define(
            name: String,
            description: String = "",
            block: ChoiceFunction.Parameters.() -> Unit = {}
        ): Boolean {
            return add(
                ChoiceFunction(
                    name = name,
                    description = description.ifEmpty { null },
                    parameters = ChoiceFunction.Parameters().apply(block).build().ifEmpty { null }
                )
            )
        }

        @OpenAiDsl
        @Deprecated("Deprecated in favor of tool_choice.")
        public var call: JsonObject? = null

        @OpenAiDsl
        public fun call(name: String): Builder = call {
            put("name", name)
        }

        @OpenAiDsl
        public fun call(block: JsonObjectBuilder.() -> Unit): Builder = apply {
            choice = buildJsonObject {
                put("type", "function")
                putJsonObject("function", block)
            }
        }

        @OpenAiDsl
        public var temperature: Double = 1.0

        @OpenAiDsl
        public fun temperature(value: Double): Builder = apply {
            temperature = value
        }

        @OpenAiDsl
        public var topP: Double = 1.0

        @OpenAiDsl
        public fun topP(value: Double): Builder = apply {
            topP = value
        }

        @OpenAiDsl
        public var number: Int = 1

        @OpenAiDsl
        public fun number(value: Int): Builder = apply {
            number = value
        }

        @OpenAiDsl
        public var stream: Boolean = false

        @OpenAiDsl
        public fun stream(value: Boolean): Builder = apply {
            stream = value
        }

        @OpenAiDsl
        public var stop: List<String> = emptyList()

        @OpenAiDsl
        public fun stop(values: List<String>): Builder = apply {
            stop = values
        }

        @OpenAiDsl
        public fun stop(vararg values: String): Builder = apply {
            stop = values.asList()
        }


        @OpenAiDsl
        public var maxTokens: Int = 256

        @OpenAiDsl
        public fun maxTokens(value: Int): Builder = apply {
            maxTokens = value
        }

        @OpenAiDsl
        public var presencePenalty: Double = 0.0

        @OpenAiDsl
        public fun presencePenalty(value: Double): Builder = apply {
            presencePenalty = value
        }

        @OpenAiDsl
        public var frequencyPenalty: Double = 0.0

        @OpenAiDsl
        public fun frequencyPenalty(value: Double): Builder = apply {
            frequencyPenalty = value
        }

        @OpenAiDsl
        public var bestOf: Int = 1

        @OpenAiDsl
        public fun bestOf(value: Int): Builder = apply {
            bestOf = value
        }

        @OpenAiDsl
        public var logitBias: Map<String, Int> = emptyMap()

        @OpenAiDsl
        public fun logitBias(value: Map<String, Int>): Builder = apply {
            logitBias = value
        }

        @OpenAiDsl
        public fun logitBias(vararg pairs: Pair<String, Int>): Builder = apply {
            logitBias = mapOf(pairs = pairs)
        }

        @OpenAiDsl
        public var user: String = ""

        @OpenAiDsl
        public fun user(value: String): Builder = apply {
            user = value
        }

        @OpenAiDsl
        public var tools: List<ChoiceTool> = emptyList()

        @OpenAiDsl
        public fun tools(block: MutableList<ChoiceTool>.() -> Unit): Builder = apply {
            tools = buildList(block)
        }

        @OpenAiDsl
        public var choice: JsonElement = JsonNull

        @OpenAiDsl
        public fun choice(block: JsonObjectBuilder.() -> Unit): Builder = apply {
            choice = buildJsonObject(block)
        }

        public fun build(): ChatRequest {
            check(messages.isEmpty().not()) { "Required messages" }
            return ChatRequest(
                model = model,
                messages = messages,
                functions = functions.ifEmpty { null },
                call = call ?: JsonNull,
                temperature = temperature,
                topP = topP,
                number = number,
                stream = stream,
                stop = stop.ifEmpty { null },
                maxTokens = maxTokens,
                max_completion_tokens = maxTokens,
                presencePenalty = presencePenalty,
                frequencyPenalty = frequencyPenalty,
                logitBias = logitBias.ifEmpty { null },
                user = user,
                tools = tools.ifEmpty { null },
                choice = choice,
            )
        }
    }
}