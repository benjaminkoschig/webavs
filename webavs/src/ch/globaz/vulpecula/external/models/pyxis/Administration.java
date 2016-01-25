package ch.globaz.vulpecula.external.models.pyxis;

public class Administration extends Tiers {
    public static final String CS_TYPE_CAISSE_AF = "509030";

    private String genre;
    private String canton;
    private String codeAdministration;
    private String codeInstitution;

    public Administration() {
        super();
    }

    public Administration(Tiers tiers) {
        super(tiers);
    }

    public Administration(Administration administration) {
        super(administration);
        genre = administration.getGenre();
        canton = administration.getCanton();
        codeAdministration = administration.getCodeAdministration();
        codeInstitution = administration.getCodeInstitution();
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getCodeAdministration() {
        return codeAdministration;
    }

    public void setCodeAdministration(String codeAdministration) {
        this.codeAdministration = codeAdministration;
    }

    public String getCodeInstitution() {
        return codeInstitution;
    }

    public void setCodeInstitution(String codeInstitution) {
        this.codeInstitution = codeInstitution;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Administration) {
            Administration other = (Administration) obj;
            return getId().equals(other.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Administration [designation1=" + designation1 + " genre=" + genre + ", canton=" + canton
                + ", codeAdministration=" + codeAdministration + ", codeInstitution=" + codeInstitution + "]";
    }
}
