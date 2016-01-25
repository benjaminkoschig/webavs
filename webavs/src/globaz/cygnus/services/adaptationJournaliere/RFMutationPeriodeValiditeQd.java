package globaz.cygnus.services.adaptationJournaliere;

public class RFMutationPeriodeValiditeQd {

    String dateDebutQd1 = "";
    String dateDebutQd2 = "";
    String dateFinQd1 = "";
    String dateFinQd2 = "";
    String idFamille = "";
    String idPeriode = "";
    String idQd = "";
    String typeDeMutation = "";

    public RFMutationPeriodeValiditeQd(String dateDebutQd1, String dateDebutQd2, String dateFinQd1, String dateFinQd2,
            String idFamille, String idPeriode, String idQd, String typeDeMutation) {
        super();
        this.dateDebutQd1 = dateDebutQd1;
        this.dateDebutQd2 = dateDebutQd2;
        this.dateFinQd1 = dateFinQd1;
        this.dateFinQd2 = dateFinQd2;
        this.idFamille = idFamille;
        this.idPeriode = idPeriode;
        this.idQd = idQd;
        this.typeDeMutation = typeDeMutation;
    }

    public String getDateDebutQd1() {
        return dateDebutQd1;
    }

    public String getDateDebutQd2() {
        return dateDebutQd2;
    }

    public String getDateFinQd1() {
        return dateFinQd1;
    }

    public String getDateFinQd2() {
        return dateFinQd2;
    }

    public String getIdFamille() {
        return idFamille;
    }

    public String getIdPeriode() {
        return idPeriode;
    }

    public String getIdQd() {
        return idQd;
    }

    public String getTypeDeMutation() {
        return typeDeMutation;
    }

    public void setDateDebutQd1(String dateDebutQd1) {
        this.dateDebutQd1 = dateDebutQd1;
    }

    public void setDateDebutQd2(String dateDebutQd2) {
        this.dateDebutQd2 = dateDebutQd2;
    }

    public void setDateFinQd1(String dateFinQd1) {
        this.dateFinQd1 = dateFinQd1;
    }

    public void setDateFinQd2(String dateFinQd2) {
        this.dateFinQd2 = dateFinQd2;
    }

    public void setIdFamille(String idFamille) {
        this.idFamille = idFamille;
    }

    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    public void setIdQd(String idQd) {
        this.idQd = idQd;
    }

    public void setTypeDeMutation(String typeDeMutation) {
        this.typeDeMutation = typeDeMutation;
    }

}
