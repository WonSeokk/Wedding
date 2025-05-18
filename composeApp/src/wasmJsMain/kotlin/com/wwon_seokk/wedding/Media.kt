data class Media(
    val key: Int,
    val type: MediaType,
    val fileName: String,
    val thumb: String? = null
) {
    enum class MediaType {
        IMAGE, VIDEO
    }
}
