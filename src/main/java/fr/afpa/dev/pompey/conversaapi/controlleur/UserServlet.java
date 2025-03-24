package fr.afpa.dev.pompey.conversaapi.controlleur;

import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.dao.UserDAO;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.*;

@WebServlet("/api/users")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<User> users = userService.getAllUsers();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(usersToJson(users));
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

//    private String userToJson(User user) {
//        return String.format("{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"role\":\"%s\",\"date\":\"%s\"}",
//                user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getDate());
//
//    }

    private String usersToJson(List<User> users) {
        try{
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (User user : users) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("id", user.getId())
                        .add("name", user.getName())
                        .add("password", user.getPassword())
                        .add("email", user.getEmail())
                        .add("role", user.getRole())
                        .add("date", user.getDate().toString()));
            }
            return arrayBuilder.build().toString();
        }catch (JsonException e){
            throw new JsonException("Erreur lors de la lecture du serveur");
        }
    }


}
