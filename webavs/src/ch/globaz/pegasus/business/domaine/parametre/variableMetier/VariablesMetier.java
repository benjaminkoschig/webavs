package ch.globaz.pegasus.business.domaine.parametre.variableMetier;

import java.util.Collection;
import ch.globaz.pegasus.business.domaine.parametre.MapWithListSortedByDate;

public class VariablesMetier extends MapWithListSortedByDate<VariableMetierType, VariableMetier, VariablesMetier> {
    public VariablesMetier() {
    }

    public VariablesMetier(Collection<VariableMetier> variablesMetier) {
        super(variablesMetier);
    }

    public VariablesMetier getFraisEntretienImmeuble() {
        return getParameters(VariableMetierType.FRAIS_ENTRETIEN_IMMEUBLE);
    }

    public VariablesMetier getTauxOfasInteretFortune() {
        return getParameters(VariableMetierType.TAUX_OFAS);
    }

    @Override
    public Class<VariablesMetier> getTypeClass() {
        return VariablesMetier.class;
    }

}
