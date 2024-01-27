package pkunk.htmx.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import pkunk.htmx.model.Status
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ThreadLocalRandom


@Controller
class FeedController {

    companion object {
        const val PAGE_SIZE = 5
    }

    val statuses by lazy {
        val statuses = mutableListOf<Status>()
        val r = ThreadLocalRandom.current()
        repeat(PAGE_SIZE * 100) {
            statuses.add(
                Status(
                    it,
                    "@" + funnyString(r.nextInt()).lowercase(),
                    funnyString(r.nextInt()),
                    funnyString(r.nextInt()) + " " + funnyString(r.nextInt()) + " " + funnyString(r.nextInt()),
                )
            )
        }
        statuses
    }

    private fun funnyString(int: Int): String =
        Base64.getEncoder()
            .encodeToString(ByteBuffer.allocate(4).putInt(int).array())
            .replace("+", "")
            .replace("/", "")
            .replace("=", "")

    @GetMapping("/feed")
    fun getStatuses(
        model: Model,
        @RequestParam("page") page: Int
    ): String {
        if (page > 99) {
            model.addAttribute("statuses", emptyList<Status>())
            model.addAttribute("link", "/feed?page=" + 100)
            return "feed"
        }
        val from = page * PAGE_SIZE
        val to = from + PAGE_SIZE
        model.addAttribute("statuses", statuses.subList(from, to))
        model.addAttribute("link", "/feed?page=" + (page + 1))
        return "feed"
    }
}
