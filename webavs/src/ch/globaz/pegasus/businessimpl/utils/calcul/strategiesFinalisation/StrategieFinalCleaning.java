package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CasMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class StrategieFinalCleaning implements StrategieCalculFinalisation {

    private static final String[] revenuAgricoleKeys = { IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_REVENUS_SOUS_LOCATIONS,
            IPCValeursPlanCalcul.CLE_DEPEN_FRAISIMM_INTERET_HYPOTHECAIRE,
            IPCValeursPlanCalcul.CLE_DEPEN_COT_PSAL_TOTAL,

    };

    /**
     * On implémente la méthode calcule, qui nettoie
     */
    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
        if (context.getCasMetier().contains(CasMetier.REVENU_INDEPENDANT_AGRICOLE)) {
            for (String cle : StrategieFinalCleaning.revenuAgricoleKeys) {
                donnee.getEnfants().remove(cle);
            }
        }
    }
}
