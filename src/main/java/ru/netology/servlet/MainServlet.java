package ru.netology.servlet;

import com.google.gson.Gson;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.netology.model.Method.*;

public class MainServlet extends HttpServlet {

    public static final String API_REGEX = "/api/posts/\\d+";
    public static final String PATH = "/api/posts";
    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("ru.netology");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (method.equals(GET.name()) && path.equals(PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET.name()) && path.matches(API_REGEX)) {
                final var id = getaLong(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST.name()) && path.equals(PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE.name()) && path.matches(API_REGEX)) {
                final var id = getaLong(path);
                controller.removeById(id, resp);
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().print(new Gson().toJson("Не найден путь"));
        } catch (NotFoundException ex) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static long getaLong(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}