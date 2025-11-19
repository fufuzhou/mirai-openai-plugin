package xyz.cssxsh.openai.image

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable(ImageSize.Serializer::class)
public enum class ImageSize(public val text: String) {
    LARGE_WIDTH("1792x1024"),
    LARGE_HEIGHT("1024x1792"),
    ExLARGE("2048x2048"),
    LARGE("1024x1024"),
    MIDDLE("512x512"),
    SMALL("256x256"),
    I2K("2K"),
    I4K("4K");

    internal companion object Serializer : KSerializer<ImageSize> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor(ImageSize::class.qualifiedName!!, PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): ImageSize {
            val text = decoder.decodeString()
            return values().find { it.text == text }
                ?: throw NoSuchElementException("image size: $text")
        }

        override fun serialize(encoder: Encoder, value: ImageSize) {
            encoder.encodeString(value.text)
        }
    }
}