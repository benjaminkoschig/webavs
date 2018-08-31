package globaz.vulpecula.vb.comptabilite;

import globaz.vulpecula.vb.listes.PTListeProcessViewBean;
import java.util.List;

public class PTListeRecapParRubriqueViewBean extends PTListeProcessViewBean {

    private String forIdRole;
    private String forIdGenreCompte;
    private String forIdCategorie;
    private String fromDateDebut;
    private String toDateFin;
    private String fromIdExterneRole;
    private String toIdExterneRole;
    private List<String> fromIdExternes;

    public String getForIdRole() {
        return forIdRole;
    }

    public void setForIdRole(String forIdRole) {
        this.forIdRole = forIdRole;
    }

    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    public void setForIdGenreCompte(String forIdGenreCompte) {
        this.forIdGenreCompte = forIdGenreCompte;
    }

    public String getForIdCategorie() {
        return forIdCategorie;
    }

    public void setForIdCategorie(String forIdCategorie) {
        this.forIdCategorie = forIdCategorie;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public String getToDateFin() {
        return toDateFin;
    }

    public void setToDateFin(String toDateFin) {
        this.toDateFin = toDateFin;
    }

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    public String getToIdExterneRole() {
        return toIdExterneRole;
    }

    public void setToIdExterneRole(String toIdExterneRole) {
        this.toIdExterneRole = toIdExterneRole;
    }

    public List<String> getFromIdExternes() {
        return fromIdExternes;
    }

    public void setFromIdExternes(List<String> fromIdExternes) {
        this.fromIdExternes = fromIdExternes;
    }

}
