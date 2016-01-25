package ch.globaz.pegasus.business.domaine.parametre;

import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaieEtrangereType;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaiesEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierType;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;

public class Parameters {
    private VariablesMetier variablesMetier = new VariablesMetier();
    private MonnaiesEtrangere monnaiesEtrangere = new MonnaiesEtrangere();

    public VariablesMetier getVariablesMetier() {
        return variablesMetier;
    }

    public void setVariablesMetier(VariablesMetier variablesMetier) {
        this.variablesMetier = variablesMetier;
    }

    public MonnaiesEtrangere getMonnaiesEtrangere() {
        return monnaiesEtrangere;
    }

    public void setMonnaiesEtrangere(MonnaiesEtrangere monnaiesEtrangere) {
        this.monnaiesEtrangere = monnaiesEtrangere;
    }

    public VariablesMetier getParameters(VariableMetierType variableMetierType) {
        return variablesMetier.getParameters(variableMetierType);
    }

    public MonnaiesEtrangere getParameters(MonnaieEtrangereType parametreType) {
        return monnaiesEtrangere.getParameters(parametreType);
    }

}
