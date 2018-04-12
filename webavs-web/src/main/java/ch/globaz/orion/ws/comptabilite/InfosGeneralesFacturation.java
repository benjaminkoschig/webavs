package ch.globaz.orion.ws.comptabilite;

import java.math.BigDecimal;

public class InfosGeneralesFacturation {
    private Integer nbFacturesOuvertes;
    private Integer nbFacturesEchues;
    private BigDecimal soldeOuvert;
    private BigDecimal soldeEchu;

    public InfosGeneralesFacturation() {
        super();
    }

    public InfosGeneralesFacturation(Integer nbFacturesOuvertes, Integer nbFacturesEchues, BigDecimal soldeOuvert,
            BigDecimal soldeEchu) {
        super();
        this.nbFacturesOuvertes = nbFacturesOuvertes;
        this.nbFacturesEchues = nbFacturesEchues;
        this.soldeOuvert = soldeOuvert;
        this.soldeEchu = soldeEchu;
    }

    public Integer getNbFacturesOuvertes() {
        return nbFacturesOuvertes;
    }

    public void setNbFacturesOuvertes(Integer nbFacturesOuvertes) {
        this.nbFacturesOuvertes = nbFacturesOuvertes;
    }

    public Integer getNbFacturesEchues() {
        return nbFacturesEchues;
    }

    public void setNbFacturesEchues(Integer nbFacturesEchues) {
        this.nbFacturesEchues = nbFacturesEchues;
    }

    public BigDecimal getSoldeOuvert() {
        return soldeOuvert;
    }

    public void setSoldeOuvert(BigDecimal soldeOuvert) {
        this.soldeOuvert = soldeOuvert;
    }

    public BigDecimal getSoldeEchu() {
        return soldeEchu;
    }

    public void setSoldeEchu(BigDecimal soldeEchu) {
        this.soldeEchu = soldeEchu;
    }

}
