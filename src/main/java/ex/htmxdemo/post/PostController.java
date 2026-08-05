package ex.htmxdemo.post;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public String list(
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
        Model model
    ) {
        model.addAttribute("page", postService.list(pageable));
        return "post/list";
    }

    @HxRequest
    @GetMapping("/posts")
    public String listFragment(
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
        Model model
    ) {
        model.addAttribute("page", postService.list(pageable));
        return "post/list :: list";
    }

    @GetMapping("/posts/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.get(id));
        return "post/detail";
    }
}
