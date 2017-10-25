package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.coupleSepare.fusion;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFusion;

public class StrategieFusionRevenus implements StrategieCalculFusion {

    private static final String[] champs = new String[] { IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJ_LCA,
            IPCValeursPlanCalcul.CLE_REVEN_RENFORMO_VALEUR_LOCATIVE, IPCValeursPlanCalcul.CLE_REVEN_ALLOCAPI_AVS_AI,
            IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER };

    @Override
    public void calcule(TupleDonneeRapport donneeCommun, TupleDonneeRapport donneeAvecEnfants,
            TupleDonneeRapport donneeSeul, TupleDonneeRapport donneeFusionne, CalculContext context, Date dateDebut)
            throws CalculException {

        for (String champ : StrategieFusionRevenus.champs) {

            TupleDonneeRapport tuple = new TupleDonneeRapport(champ);

            if (IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_CONTRAT_ENTRETIEN_VIAGER.equals(champ)) {
                tuple.setValeur(donneeCommun.getValeurEnfant(champ));
            } else {
                tuple.setValeur(donneeAvecEnfants.getValeurEnfant(champ));
            }

            donneeFusionne.addEnfantTuple(tuple);

        }
    }

}
