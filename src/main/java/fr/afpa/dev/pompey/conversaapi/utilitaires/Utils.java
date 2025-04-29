package fr.afpa.dev.pompey.conversaapi.utilitaires;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletResponse;

public class Utils {
    public static String getNameClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Recherche de la première classe hors de Utils
        for (int i = 2; i < stackTrace.length; i++) {
            String nomClasse = stackTrace[i].getClassName();
            if (!nomClasse.equals(Utils.class.getName())) {
                String nomMethode = stackTrace[i].getMethodName();
                int numLigne = stackTrace[i].getLineNumber();
                return "Appelé depuis : " + nomClasse + "." + nomMethode + " (ligne " + numLigne + ")";
            }
        }

        return "Emplacement appelant non trouvé";
    }

    /**
     * Convertit un message en JSON et l'envoie à la réponse HTTP.
     * {"status": "error", "message": "Votre Message"}
     *
     * @param response La réponse HTTP.
     * @param message  Le message à envoyer.
     * @throws Exception
     */
//    public static String MsgErrorToJSON(HttpServletResponse response, String message) throws Exception {
//        try {
//            JsonObject jsonResponse = Json.createObjectBuilder()
//                    .add("status", "error")
//                    .add("message", message)
//                    .build();
//
//            response.getWriter().write(jsonResponse.toString());
//            response.getWriter().flush();
//            response.getWriter().close();
//            return jsonResponse.toString();
//        } catch (Exception e) {
//            throw new Exception("Erreur dans la méthode MsgErrorToJSON : " + e.getMessage());
//        }
//    }

}
