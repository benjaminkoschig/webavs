package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;

public class DecompteMensuelLine {

    private String idCotisation;
    private BigDecimal masse;
    private String typeCotisation;
    private String libelleFr;
    private String libelleDe;
    private String libelleIt;
    private int idRubrique;

    DecompteMensuelLine() {
        // vide pour génération jAXWS
    }

    public DecompteMensuelLine(DecompteMensuelLineBuilder builder) {
        idCotisation = builder.getIdCotisation();
        masse = builder.getMasse();
        typeCotisation = builder.getTypeCotisation();
        libelleFr = builder.getLibelleFr();
        libelleDe = builder.getLibelleDe();
        libelleIt = builder.getLibelleIt();
        idRubrique = builder.getIdRubrique();
    }

    public String getIdCotisation() {
        return idCotisation;
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

    public String getLibelleFr() {
        return libelleFr;
    }

    public String getLibelleDe() {
        return libelleDe;
    }

    public String getLibelleIt() {
        return libelleIt;
    }

    public int getIdRubrique() {
        return idRubrique;
    }

    public void setIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
    }

    public void setTypeCotisation(String typeCotisation) {
        this.typeCotisation = typeCotisation;
    }

    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    public void setLibelleDe(String libelleDe) {
        this.libelleDe = libelleDe;
    }

    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

    public void setIdRubrique(int idRubrique) {
        this.idRubrique = idRubrique;
    }
}
