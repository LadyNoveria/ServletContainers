package servlet;

import com.google.gson.Gson;
import controller.PostController;
import exception.NotFoundException;
import repository.PostRepository;
import service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.lang.String.format;

public class MainServlet extends HttpServlet {

    public static final String API_POSTS_D = "/api/posts/\\d+";
    public static final String PATH = "/api/posts";
    private PostController controller;

    //    private final String GET_METHOD = "GET";
//    private final String POST_METHOD = "POST";
//    private final String DELETE_METHOD = "DELETE";

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
//            switch (method) {
//                case "GET":
//                    getPost(path, resp);
//                    return;
//                case "POST":
//                    updatePost(path, req, resp);
//                    return;
//                case "DELETE":
//                    removePost(path, resp);
//                    return;
//            }

            if (method.equals("GET") && path.equals(PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals("GET") && path.matches(API_POSTS_D)) {
                final var id = getaLong(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals(PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches(API_POSTS_D)) {
                final var id = getaLong(path);
                controller.removeById(id, resp);
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().print(new Gson().toJson("Не найден путь"));
        }
        catch (NotFoundException ex){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static long getaLong(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }

//    private void getPost(String path, HttpServletResponse resp) throws IOException {
//        if (path.equals(DEFAULT_PATH)) {
//            controller.all(resp);
//            return;
//        }
//        if (path.matches(REGEX_PATH)) {
//            final var id = getId(path);
//            controller.getById(id, resp);
//        }
//    }
//
//    private void updatePost(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        if (path.equals(DEFAULT_PATH)) {
//            controller.save(req.getReader(), resp);
//        }
//    }
//
//    private void removePost(String path, HttpServletResponse resp) {
//        if (path.matches(REGEX_PATH)) {
//            final var id = getId(path);
//            controller.removeById(id, resp);
//        }
//    }
}