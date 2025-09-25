package repository;

import exception.NotFoundException;
import model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class PostRepository {

    private static final AtomicLong id = new AtomicLong(0);
    private final ConcurrentHashMap<Long, String> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return posts.entrySet()
                .stream()
                .map(entry -> new Post(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        return posts.get(id).isEmpty()
                ? Optional.empty()
                : Optional.of(new Post(id, posts.get(id)));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            Post tmp = new Post(id.getAndIncrement(), post.getContent());
            posts.put(tmp.getId(), tmp.getContent());
            return tmp;
        }

        if (posts.containsKey(post.getId())) {
            posts.put(post.getId(), post.getContent());
            return post;
        } else {
            throw new NotFoundException();
        }
    }

    public void removeById(long id) {
        if (posts.containsKey(id)) {
            posts.remove(id);
        } else {
            throw new NotFoundException();
        }
    }
}