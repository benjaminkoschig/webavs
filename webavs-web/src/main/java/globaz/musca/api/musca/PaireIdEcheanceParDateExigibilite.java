package globaz.musca.api.musca;

import java.util.Objects;

public class PaireIdEcheanceParDateExigibilite {

    private String idEcheance;
    private String dateExigibilite;

    public PaireIdEcheanceParDateExigibilite(String idEcheance, String dateExigibilite) {
        this.idEcheance = idEcheance;
        this.dateExigibilite = dateExigibilite;
    }

    public String getIdEcheance() {
        return idEcheance;
    }

    public void setIdEcheance(String idEcheance) {
        this.idEcheance = idEcheance;
    }

    public String getDateExigibilite() {
        return dateExigibilite;
    }

    public void setDateExigibilite(String dateExigibilite) {
        this.dateExigibilite = dateExigibilite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaireIdEcheanceParDateExigibilite that = (PaireIdEcheanceParDateExigibilite) o;
        return Objects.equals(idEcheance, that.idEcheance) && Objects.equals(dateExigibilite, that.dateExigibilite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEcheance, dateExigibilite);
    }
}