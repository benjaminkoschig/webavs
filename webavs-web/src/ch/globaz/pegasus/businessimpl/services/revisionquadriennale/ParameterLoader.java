package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaiesEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;

public class ParameterLoader {
    public Parameters load() {

        VariableMetierLoader variableMetierLoader = new VariableMetierLoader();
        VariablesMetier variablesMetier = variableMetierLoader.load();

        MonnaieEtrangereLoader etrangereLoader = new MonnaieEtrangereLoader();
        MonnaiesEtrangere monnaiesEtrangere = etrangereLoader.load();

        Parameters parameters = new Parameters();
        parameters.setMonnaiesEtrangere(monnaiesEtrangere);
        parameters.setVariablesMetier(variablesMetier);

        return parameters;
    }
}
