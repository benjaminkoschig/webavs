package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;

public class DecompteMensuelLine {

    private String idCotisation;
    private BigDecimal masse;
    private String typeCotisation;
    private String libelleFr;
    private String libelleDe;
    private String libelleIt;

    public DecompteMensuelLine() {
        super();
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    public void setIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
    }

    public BigDecimal getMasse() {
        return masse;
    }

    public void setMasse(BigDecimal masse) {
        this.masse = masse;
    }

    public String getTypeCotisation() {
        return typeCotisation;
    }

    public void setTypeCotisation(String typeCotisation) {
        this.typeCotisation = typeCotisation;
    }

    public String getLibelleFr() {
        return libelleFr;
    }

    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    public String getLibelleDe() {
        return libelleDe;
    }

    public void setLibelleDe(String libelleDe) {
        this.libelleDe = libelleDe;
    }

    public String getLibelleIt() {
        return libelleIt;
    }

    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

}
