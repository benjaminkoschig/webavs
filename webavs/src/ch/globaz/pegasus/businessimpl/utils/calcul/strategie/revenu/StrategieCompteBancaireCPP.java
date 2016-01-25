/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.IStrategieDessaisissable;

/**
 * @author ECO
 * 
 */
public class StrategieCompteBancaireCPP extends StrategieCalculRevenu implements IStrategieDessaisissable {

    @Override
    public float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException {
        return getMontantInterets(donnee, context, false, "0");
    }

    private float calculeMontantInteretFictif(float montantInitial, CalculContext context, boolean isSansInteret,
            String montantInteret) throws CalculException {
        float interetFictif;
        if (!isSansInteret && JadeStringUtil.isBlankOrZero(montantInteret)) {
            interetFictif = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_2091_DPC)).getValeurCourante()) / 100f;
        } else if (isSansInteret) {
            interetFictif = 0;
        } else {
            return Float.parseFloat(montantInteret);
        }
        return montantInitial * interetFictif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calculeRevenu
     * (ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif, java.util.Map)
     */
    @Override
    protected TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        final String csTypePropriete = donnee.getCompteBancaireCPPCsTypePropriete();

        if (!isNuProprietaire(donnee.getCompteBancaireCPPCsTypePropriete())) {

            // calcul de l'interet fictif
            String strMontantFrais = donnee.getCompteBancaireCPPMontantFrais();
            float montantFraisBancaire = 0;
            if (!JadeStringUtil.isEmpty(strMontantFrais)) {
                montantFraisBancaire = Float.parseFloat(strMontantFrais);
            }

            // ajout des frais bancaires
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_INTER_FRAIS_COMPTEBANCAIRECPP,
                    montantFraisBancaire);

            Boolean isSansInteret = donnee.getCompteBancaireIsSansInteret();
            String montantInterets = donnee.getCompteBancaireCPPMontantInterets();

            float montantFinal = getMontantInterets(donnee, context, isSansInteret, montantInterets);

            // ajout des interets
            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_INTER_INTERETS_COMPTEBANCAIRECPP,
                    montantFinal);

            montantFinal = Math.round(Math.max(montantFinal - montantFraisBancaire, 0));

            this.getOrCreateChild(resultatExistant, IPCValeursPlanCalcul.CLE_REVEN_INTFORMO_INTERETS_COMPTES_BANCAIRE,
                    montantFinal);

        }
        return resultatExistant;
    }

    private float getMontantInterets(CalculDonneesCC donnee, CalculContext context, Boolean isSansInteret,
            String montantInterets) throws CalculException {
        float fraction = Float.parseFloat(donnee.getCompteBancaireCPPFractionNumerateur())
                / Float.parseFloat(donnee.getCompteBancaireCPPFractionDenominateur());

        float montantFinal = calculeInteretFictif(context, Float.parseFloat(donnee.getCompteBancaireCPPMontant()),
                isSansInteret, montantInterets);
        return montantFinal * fraction;
    }
}
