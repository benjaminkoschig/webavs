package ch.globaz.vulpecula.domain.models.controleemployeur;

public class Reviseur {
    private final String id;
    private final String user;

    public Reviseur(String id, String user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }
}
