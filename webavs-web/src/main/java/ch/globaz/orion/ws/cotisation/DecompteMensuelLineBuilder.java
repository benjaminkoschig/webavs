package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;

public final class DecompteMensuelLineBuilder {

    private String idCotisation;
    private BigDecimal masse;
    private String typeCotisation;
    private String libelleFr;
    private String libelleDe;
    private String libelleIt;
    private int idRubrique;

    public DecompteMensuelLineBuilder() {
        super();
    }

    public DecompteMensuelLineBuilder withIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
        return this;
    }

    public DecompteMensuelLineBuilder withMasse(BigDecimal masse) {
        this.masse = masse;
        return this;
    }

    public DecompteMensuelLineBuilder withTypeCotisation(String typeCotisation) {
        this.typeCotisation = typeCotisation;
        return this;
    }

    public DecompteMensuelLineBuilder withLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
        return this;
    }

    public DecompteMensuelLineBuilder withLibelleDe(String libelleDe) {
        this.libelleDe = libelleDe;
        return this;
    }

    public DecompteMensuelLineBuilder withLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
        return this;
    }

    public DecompteMensuelLineBuilder withIdRubrique(int idRubrique) {
        this.idRubrique = idRubrique;
        return this;
    }

    public DecompteMensuelLine build() {
        return new DecompteMensuelLine(this);
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    public BigDecimal getMasse() {
        return masse;
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
}
