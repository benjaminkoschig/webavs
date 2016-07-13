package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalDepenseHome implements StrategieCalculFinalisation {

    private final static String[] champs = {
    // IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_NON_RECONNUE,
    IPCValeursPlanCalcul.CLE_DEPEN_GR_LOYER_TAXES_PENSION_RECONNUE };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        String legende = "";
        float somme = 0;
        for (String champ : StrategieFinalDepenseHome.champs) {
            somme += donnee.getValeurEnfant(champ);
            legende += donnee.getLegendeEnfant(champ);
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_DEPEN_TAXEHOME_TOTAL, somme, legende));
    }
}
