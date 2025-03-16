package xyz.cssxsh.mirai.openai.config

import kotlinx.serialization.modules.*
import net.mamoe.mirai.console.data.*
import xyz.cssxsh.openai.image.*

@PublishedApi
internal object ImageConfig : ReadOnlyPluginConfig(saveName = "image") {

    override val serializersModule: SerializersModule = SerializersModule {
        contextual(ImageSize.Serializer)
    }

    @ValueName("n")
    @ValueDescription("generations number")
    val number: Int by value(1)

    @ValueName("size")
    @ValueDescription("1024x1024 512x512 256x256")
    val size: ImageSize by value(ImageSize.LARGE)

    @ValueName("format")
    @ValueDescription("url / b64_json")
    val format: ImageResponseFormat by value(ImageResponseFormat.URL)

    @ValueName("model")
    @ValueDescription("dall-e-2 | dall-e-3 | black-forest-labs/FLUX.1-schnell | Kwai-Kolors/Kolors")
    val model: String by value("dall-e-2")

    @ValueName("num_inference_steps")
    @ValueDescription("Required range: 1 <= x <= 50")
    val num_inference_steps : Int by value(50)

    fun push(builder: ImageRequest.Builder) {
        builder.number = number
        builder.size = size
        builder.format = format
        builder.model = model
        builder.num_inference_steps = num_inference_steps
    }
}