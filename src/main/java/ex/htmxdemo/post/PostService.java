package ex.htmxdemo.post;

import ex.htmxdemo.post.entity.Post;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
