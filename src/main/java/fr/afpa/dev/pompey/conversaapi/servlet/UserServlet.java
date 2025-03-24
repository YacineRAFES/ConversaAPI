package fr.afpa.dev.pompey.conversaapi.servlet;

import fr.afpa.dev.pompey.conversaapi.exception.JsonException;
import fr.afpa.dev.pompey.conversaapi.modele.User;
import fr.afpa.dev.pompey.conversaapi.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import jakarta.json.*;

/**
 * Servlet pour les utilisateurs.
 */
@WebServlet("/api/users")
public class UserServlet extends HttpServlet {

    private UserService userService;

    /**
     * Initialise la servlet.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }

    /**
     * Récupère tous les utilisateurs et les renvoie en JSON.
     *
     * @param request  La requête HTTP.
     * @param response La réponse HTTP.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(usersToJson(users));
    }

    //Add doPost method for creating a new user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    /**
     * Convertit une liste d'utilisateurs en JSON.
     *
     * @param users La liste d'utilisateurs.
     * @return La liste d'utilisateurs en JSON.
     */
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
