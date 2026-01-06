package Vue;  // ou Controler; ou Vue; selon où tu veux le mettre

import DAO.GestionnaireDAO;

public class LoginService {

    // ===== Méthode qui vérifie login + mot de passe =====
    public static boolean authenticate(String username, String password) {
        GestionnaireDAO dao = new GestionnaireDAO();
        return dao.authenticate(username, password);
    }
}
