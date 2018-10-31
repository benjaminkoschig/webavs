package ch.globaz.amal.business.calcul;

import ch.globaz.common.domaine.Montant;

public class ParamsPCFamilleContainer {

    private String paramYear;
    private String typeSubside; // Related to IAMParametresAnnuels
    private Montant valeurNumerique;
    private String valeurString;
    private Montant montantFrom;
    private Montant montantTo;

    public ParamsPCFamilleContainer() {
        paramYear = "";
        typeSubside = "";
        valeurNumerique = Montant.ZERO;
        valeurString = "";
        montantFrom = Montant.ZERO;
        montantTo = Montant.ZERO;
    }

    public String getParamYear() {
        return paramYear;
    }

    public String getTypeSubside() {
        return typeSubside;
    }

    public Montant getValeurNumerique() {
        return valeurNumerique;
    }

    public String getValeurString() {
        return valeurString;
    }

    public Montant getMontantFrom() {
        return montantFrom;
    }

    public Montant getMontantTo() {
        return montantTo;
    }

    public void setParamYear(String paramYear) {
        this.paramYear = paramYear;
    }

    public void setTypeSubside(String typeSubside) {
        this.typeSubside = typeSubside;
    }

    public void setValeurNumerique(Montant valeurNumerique) {
        this.valeurNumerique = valeurNumerique;
    }

    public void setValeurString(String valeurString) {
        this.valeurString = valeurString;
    }

    public void setMontantFrom(Montant montantFrom) {
        this.montantFrom = montantFrom;
    }

    public void setMontantTo(Montant montantTo) {
        this.montantTo = montantTo;
    }
}
