package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategiesFinalDepenseTotalReconnu;

import globaz.jade.log.JadeLogger;
import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

/**
 * extend de la stratégie de base, override du calcul des dépenses personnelles
 * 
 * @author cel
 * 
 */
public class StrategieFinalDepenseTotalReconnu201501 extends StrategieFinalDepenseTotalReconnu {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {
        super.calcule(donnee, context, dateDebut);
    }

    @Override
    float calculeDepensesPersonnelles(CalculContext context, TupleDonneeRapport donnee) throws CalculBusinessException,
            CalculException {
        String csCategorieArgentDePocheTypeChambre = null;
        try {
            csCategorieArgentDePocheTypeChambre = donnee
                    .getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_DEPENSE_CS_TYPE_ARGENT_DE_POCHE_TYPE_CHAMBRE);

            if (IPCTaxeJournaliere.CS_CATEGORIE_NON_DEFINIE.equals(csCategorieArgentDePocheTypeChambre)) {
                throw new CalculBusinessException(
                        "pegasus.calcul.strategie.final.depenseTotalReconnu.csTypeChambre.undefined",
                        csCategorieArgentDePocheTypeChambre);
            }
        } catch (NullPointerException e) {
            JadeLogger.info(this,
                    "The value csTypeChambre comming from calculator is null. Meaning the people is not in a home.");
        }

        // valeur par defaut 0, pas de depense, dans le cas ou la personne n'est
        // pas dans un home

        Float depensesPersonnelles = 0f;

        try {
            depensesPersonnelles = ArgentDePocheHomeResolver.getMontantForCalcul(context,
                    csCategorieArgentDePocheTypeChambre);
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            throw new CalculBusinessException(
                    "pegasus.calcul.strategie.final.depenseTotalReconnu.csTypeChambre.integrity",
                    csCategorieArgentDePocheTypeChambre, (String) context.get(Attribut.DATE_DEBUT_PERIODE));
        }

        return depensesPersonnelles * 12;
    }

}
