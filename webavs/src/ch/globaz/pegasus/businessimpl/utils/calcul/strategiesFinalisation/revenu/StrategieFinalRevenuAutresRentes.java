package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuAutresRentes implements StrategieCalculFinalisation {

    private final static String[] champs = { IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAA,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LPP, IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ETRANGERE,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ASSURANCE_PRIVEE,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_3EME_PILLIER,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_LAM,
            IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ASSURANCE_RENTE_VIAGERE

    };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {
        float somme = 0;
        for (String champ : StrategieFinalRevenuAutresRentes.champs) {
            somme += donnee.getValeurEnfant(champ);
        }

        somme += donnee.getSommeEnfants(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_AUTRE_RENTE);

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_TOTAL, somme));

    }

}
