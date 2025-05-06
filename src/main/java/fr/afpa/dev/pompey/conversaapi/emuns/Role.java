package fr.afpa.dev.pompey.conversaapi.emuns;

public enum Role {
    SUPERADMIN,
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
