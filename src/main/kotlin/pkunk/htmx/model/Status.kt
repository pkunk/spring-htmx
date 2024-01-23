package pkunk.htmx.model

@JvmRecord
data class Status(
 val id: Int,
 val userName: String,
 val fullName: String,
 val text: String,
)