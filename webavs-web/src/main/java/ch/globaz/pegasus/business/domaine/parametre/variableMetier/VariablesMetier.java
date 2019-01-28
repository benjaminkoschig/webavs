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

    public VariablesMetier getForfaitTenueMenage() {
        return getParameters(VariableMetierType.FORFAIT_REVENU_NATURE_TENUE_MENAGE);
    }

    public VariablesMetier getPlafondAnnuelEms() {
        return getParameters(VariableMetierType.PLAFOND_ANNUEL_EMS);
    }

    public VariablesMetier getPlafondAnnuelInstitution() {
        return getParameters(VariableMetierType.PLAFOND_ANNUEL_INSTITUTION);
    }

    public VariablesMetier getPlafondAnnuelListAttente() {
        return getParameters(VariableMetierType.PLAFOND_ANNUEL_LITS_ATTENTE);
    }

    public VariablesMetier getForfaitFraitChauffage() {
        return getParameters(VariableMetierType.FORFAIT_FRAIS_CHAUFFAGE);
    }

    public VariablesMetier getForfaitCharge() {
        return getParameters(VariableMetierType.FORFAIT_CHARGES);
    }
    
    public VariablesMetier getTauxPensionNonReconnue() {
        return getParameters(VariableMetierType.TAUX_PENSION_NON_RECONNUE);
    }

    @Override
    public Class<VariablesMetier> getTypeClass() {
        return VariablesMetier.class;
    }

}
