package ch.globaz.pegasus.business.domaine.parametre;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.parametre.forfaitPrime.ForfaitsPrimeAssuranceMaladie;
import ch.globaz.pegasus.business.domaine.parametre.home.TypesChambrePrix;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaiesEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierType;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;

public class Parameters {
    private final VariablesMetier variablesMetier;
    private final MonnaiesEtrangere monnaiesEtrangere;
    private final ForfaitsPrimeAssuranceMaladie forfaitsPrimesAssuranceMaladie;
    private final TypesChambrePrix typesChambrePrix;

    public Parameters() {
        variablesMetier = null;
        monnaiesEtrangere = null;
        forfaitsPrimesAssuranceMaladie = null;
        typesChambrePrix = null;
    }

    public Parameters(VariablesMetier variablesMetier, MonnaiesEtrangere monnaiesEtrangere,
            ForfaitsPrimeAssuranceMaladie forfaitsPrimesAssuranceMaladie, TypesChambrePrix typesChambrePrix) {
        this.variablesMetier = variablesMetier;
        this.monnaiesEtrangere = monnaiesEtrangere;
        this.forfaitsPrimesAssuranceMaladie = forfaitsPrimesAssuranceMaladie;
        this.typesChambrePrix = typesChambrePrix;
    }

    public Parameters filtreByPeriode(Date dateDebut) {
        VariablesMetier variablesMetier = this.variablesMetier.filtreByPeriode(dateDebut);
        MonnaiesEtrangere monnaiesEtrangere = this.monnaiesEtrangere.filtreByPeriode(dateDebut);
        ForfaitsPrimeAssuranceMaladie pimesLaml = forfaitsPrimesAssuranceMaladie.filtreforPeriode(dateDebut);
        TypesChambrePrix typesChambrePrix = this.typesChambrePrix.filtreByPeriode(dateDebut);
        return new Parameters(variablesMetier, monnaiesEtrangere, pimesLaml, typesChambrePrix);
    }

    public VariablesMetier getVariablesMetier() {
        return variablesMetier;
    }

    public MonnaiesEtrangere getMonnaiesEtrangere() {
        return monnaiesEtrangere;
    }

    public ForfaitsPrimeAssuranceMaladie getForfaitsPrimesAssuranceMaladie() {
        return forfaitsPrimesAssuranceMaladie;
    }

    public TypesChambrePrix getTypesChambrePrix() {
        return typesChambrePrix;
    }

    public VariablesMetier getParameters(VariableMetierType variableMetierType) {
        return variablesMetier.getParameters(variableMetierType);
    }

    public MonnaiesEtrangere getParameters(MonnaieEtrangereType parametreType) {
        return monnaiesEtrangere.getParameters(parametreType);
    }
}
