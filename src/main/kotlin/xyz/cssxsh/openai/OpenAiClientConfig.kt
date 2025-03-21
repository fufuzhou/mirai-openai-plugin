package xyz.cssxsh.openai


public interface OpenAiClientConfig {
    public val proxy: String
    public val doh: String
    public val ipv6: Boolean
    public val timeout: Long
    public val token: String
    public val imagetoken: String
    public val baseurl: String
}