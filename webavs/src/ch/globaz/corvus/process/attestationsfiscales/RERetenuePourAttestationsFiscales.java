package ch.globaz.corvus.process.attestationsfiscales;

import java.io.Serializable;

public class RERetenuePourAttestationsFiscales implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csType;
    private String dateDebut;
    private String dateFin;
    private String idRetenue;

    public RERetenuePourAttestationsFiscales() {
        super();

        csType = "";
        dateDebut = "";
        dateFin = "";
        idRetenue = "";
    }

    public String getCsType() {
        return csType;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdRetenue() {
        return idRetenue;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdRetenue(String idRetenue) {
        this.idRetenue = idRetenue;
    }
}
