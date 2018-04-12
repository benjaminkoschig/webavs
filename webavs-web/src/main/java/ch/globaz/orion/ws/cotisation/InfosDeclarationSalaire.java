package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;

public class InfosDeclarationSalaire {
    private BigDecimal masseSalarialeDeclaree;
    private Integer nbSalaires;
    private Integer annee;

    public BigDecimal getMasseSalarialeDeclaree() {
        return masseSalarialeDeclaree;
    }

    public void setMasseSalarialeDeclaree(BigDecimal masseSalarialeDeclaree) {
        this.masseSalarialeDeclaree = masseSalarialeDeclaree;
    }

    public Integer getNbSalaires() {
        return nbSalaires;
    }

    public void setNbSalaires(Integer nbSalaires) {
        this.nbSalaires = nbSalaires;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }
}
