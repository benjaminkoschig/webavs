package ch.globaz.pegasus.business.domaine.donneeFinanciere.bienImmobilier;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetier;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariableMetierType;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;

public class FraisEntretiensImmeuble {

    VariablesMetier variablesMetier;

    public FraisEntretiensImmeuble(VariablesMetier variablesMetier) {
        this.variablesMetier = variablesMetier.getParameters(VariableMetierType.FRAIS_ENTRETIEN_IMMEUBLE);
    }

    Montant compute(Montant valeurLocative, VariableMetier variableMetier) {
        return valeurLocative.multiply(variableMetier.getPart());
    }

    public Montant computeBrut(BienImmobilier bienImmobilier, Date dateValidite) {
        return compute(bienImmobilier.computeRevenuAnnuelBrut(), variablesMetier.resolveCourant(dateValidite));
    }

    public Montant computeBrut(BienImmobilier bienImmobilier) {
        return compute(bienImmobilier.computeRevenuAnnuelBrut(), variablesMetier.resolveMostRecent());
    }
}
