package ex.htmxdemo.post;

import ex.htmxdemo.post.dto.PostCreateRequest;
import ex.htmxdemo.post.entity.Post;
import ex.htmxdemo.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public Page<Post> list(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post get(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. id=" + id));
    }

    @Transactional
    public Post create(PostCreateRequest request, User author) {
        Post post = Post.builder()
            .title(request.title())
            .content(request.content())
            .author(author)
            .build();
        return postRepository.save(post);
    }

    @Transactional
    public void update(Long id, PostCreateRequest request, User editor) {
        Post post = get(id);
        if (!post.isAuthor(editor)) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        post.update(request.title(), request.content());
    }

    @Transactional
    public void delete(Long id, User requester) {
        Post post = get(id);
        if (!post.isAuthor(requester)) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }
}
