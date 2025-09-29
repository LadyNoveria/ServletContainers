package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import org.springframework.stereotype.Controller;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        try {
            final var data = service.getById(id);
            response.getWriter().print(gson.toJson(data));
        } catch (NullPointerException e) {
            response.getWriter().print(gson.toJson("Пост с указанным id не найден!"));
            throw new NotFoundException();
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        try {
            final var post = gson.fromJson(body, Post.class);
            final var data = service.save(post);
            response.getWriter().print(gson.toJson(data));
        } catch (NotFoundException ex) {
            response.getWriter().print(gson.toJson("Невозможно отредактировать пост с несуществующим id!"));
            throw new NotFoundException();
        }
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        try {
            service.removeById(id);
        } catch (NotFoundException ex) {
            response.getWriter().print(gson.toJson("Несуществующий id!"));
            throw new NotFoundException();
        }
    }
}