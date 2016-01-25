package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

public class TiersForOv {
    private String dateNaissance;
    private String designation1;
    private String designation2;
    private String idPays;
    private String nss;
    private String sexe;

    public TiersForOv(String dateNaissance, String designation1, String nss, String designation2, String sexe,
            String idPays) {
        super();
        this.dateNaissance = dateNaissance;
        this.designation1 = designation1;
        this.nss = nss;
        this.designation2 = designation2;
        this.sexe = sexe;
        this.idPays = idPays;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getIdPays() {
        return idPays;
    }

    public String getNss() {
        return nss;
    }

    public String getSexe() {
        return sexe;
    }
}
