package xyz.cssxsh.openai

import kotlinx.serialization.*

@Serializable
public data class Usage(
    @SerialName("completion_tokens")
    val completionTokens: Int = 0,
    @SerialName("prompt_tokens")
    val promptTokens: Int = 0,
    @SerialName("prompt_cache_hit_tokens")
    val prompt_cache_hit_tokens :Int = 0,
    @SerialName("prompt_cache_miss_tokens")
    val prompt_cache_miss_tokens:Int = 0,
    @SerialName("total_tokens")
    val totalTokens: Int = 0,
    @SerialName("completion_tokens_details")
    val completion_tokens_details: CompletionTokensDetails? = null,
    @SerialName("prompt_tokens_details")
    val prompt_tokens_details: PromptTokensDetails? = null
)

@Serializable
public data class CompletionTokensDetails(
    @SerialName("reasoning_tokens")
    val reasoning_tokens: Int = 0,
    @SerialName("audio_tokens")
    val audio_tokens: Int = 0,
    @SerialName("accepted_prediction_tokens")
    val acceptedPredictionTokens: Int = 0,
    @SerialName("rejected_prediction_tokens")
    val rejectedPredictionTokens: Int = 0
)

@Serializable
public data class PromptTokensDetails(
    @SerialName("cached_tokens")
    val cached_tokens: Int = 0,
    @SerialName("audio_tokens")
    val audio_tokens: Int = 0
)
