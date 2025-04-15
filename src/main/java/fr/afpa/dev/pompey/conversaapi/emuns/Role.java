package fr.afpa.dev.pompey.conversaapi.emuns;

public enum Role {
    ADMIN,
    UTILISATEUR,
    MODERATEUR;

    public static Role getRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role non valide : " + role);
        }
    }

}
