package ex.htmxdemo.post;

import ex.htmxdemo.auth.CustomUserDetails;
import ex.htmxdemo.post.dto.PostCreateRequest;
import ex.htmxdemo.post.entity.Post;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/posts/new")
    public String newForm(Model model) {
        model.addAttribute("form", new PostCreateRequest("", ""));
        return "post/form";
    }

    @PostMapping("/posts")
    public String create(
        BindingResult bindingResult,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @ModelAttribute("form") PostCreateRequest request
    ) {
        if (bindingResult.hasErrors()) {
            return "post/form";
        }

        Post post = postService.create(request, userDetails.user());
        return "redirect:/posts/" + post.getId();
    }
}
