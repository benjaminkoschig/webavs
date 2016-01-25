package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.revenu;

import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;

public class StrategieFinalRevenuMobiliere implements StrategieCalculFinalisation {

    private final static String[] champs = { IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_NUMERAIRES,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_COMPTES_BANCAIRE,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_RENDEMENT_TITRES,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_PRETS,
            IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_CAPITAL_LPP };

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) {

        float somme = 0;

        for (String champ : StrategieFinalRevenuMobiliere.champs) {

            // gestion compte bancaires
            if (hasInteretsCompteBancaire(champ)) {
                dealInteretCompteBancaireCPP(donnee);
            }

            somme += donnee.getValeurEnfant(champ);
        }

        donnee.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_TOTAL, somme));

    }

    private boolean hasInteretsCompteBancaire(String champ) {
        return champ.equals(IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_COMPTES_BANCAIRE);
    }

    private void dealInteretCompteBancaireCPP(TupleDonneeRapport donnee) {
        float frais = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_FRAIS_COMPTEBANCAIRECPP);
        float interet = donnee.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_INTERETS_COMPTEBANCAIRECPP);

        float interetPrisEnCompte = Math.round(Math.max(interet - frais, 0));

        donnee.addEnfantTuple((new TupleDonneeRapport(
                IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_COMPTES_BANCAIRE, interetPrisEnCompte)));
    }

}
