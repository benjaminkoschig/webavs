/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu;

import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul;

/**
 * Classe abstraite de strat�gie d'une cat�gorie de revenu pour un droit.
 * 
 * @author ECO
 * 
 */
public abstract class StrategieCalculRevenu extends StrategieCalcul {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul #calcule(java.util.List,
     * java.util.Map, ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee, int)
     */
    @Override
    public final TupleDonneeRapport calcule(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {
        return calculeRevenu(donnee, context, resultatExistant);
    }

    /**
     * Calcule l'int�ret/rendement fictif selon le montant initial, le montant �ventuellement saisi et le taux OFAS
     * 
     * @param context
     *            contexte de calcul contenant le taux OFAS
     * @param montantInitial
     *            montant initial
     * @param isSansInteret
     *            champ BD isSansInteret/isSansRendement
     * @param montantInteret
     *            champ BD du montant de l'int�ret/rendement
     * @return le montant calcul� de l'int�ret/rendement fictif
     * @throws CalculException
     *             en cas d'erreur de calcul
     */
    protected float calculeInteretFictif(CalculContext context, float montantInitial, boolean isSansInteret,
            String montantInteret) throws CalculException {
        float montantInteretCalcule = 0f;
        if (!isSansInteret && JadeStringUtil.isBlankOrZero(montantInteret)) {
            float interetFictif = Float.parseFloat(((ControlleurVariablesMetier) context
                    .get(CalculContext.Attribut.CS_2091_DPC)).getValeurCourante()) / 100f;
            montantInteretCalcule = montantInitial * interetFictif;
        } else if (isSansInteret) {
            montantInteretCalcule = 0f;
        } else {
            montantInteretCalcule = Float.parseFloat(montantInteret);
        }
        return montantInteretCalcule;
    }

    protected abstract TupleDonneeRapport calculeRevenu(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException;

    /**
     * R�gles m�tiers permettant de d�terminer si une api doit �tre prise en compte
     * 
     * @param donnee
     *            , container des donnes du calcul PC
     * @param idMembreFamilleConcerne
     *            l'identifiant du membre de famille concern�
     * @return un idicateur d�finissant si l'api doit �tre ou non prise en compte
     * @throws CalculException
     *             , si un probl�me survient avec le contexte de calcul
     * @throws NullPointerException
     *             , si donnee, ou context null
     */
    protected boolean determineIfApiPrisEnCompte(CalculDonneesCC donnee, CalculContext context) throws CalculException {

        if ((donnee == null) || (context == null)) {
            throw new NullPointerException("The parameter(s) of the method canot be null, [donnee:" + donnee
                    + "], [context:" + context + "]");
        }

        @SuppressWarnings("unchecked")
        final List<CalculDonneesHome> homesPeriode = (List<CalculDonneesHome>) context.get(Attribut.DONNEES_HOMES);

        final String idMembreFamilleConcerne = donnee.getIdDroitMembreFamille();

        boolean apiPriseEnCompte = false;

        // si la liste des homes ne contient rien api � domicille, on ne prend pas en compte
        if (homesPeriode.isEmpty()) {
            apiPriseEnCompte = false;
        } else {
            // on itere sur les homes de la p�riode
            for (CalculDonneesHome donneesHome : homesPeriode) {

                // si le membre famille matche avec ceuil d�fini dans le home
                if (donneesHome.getIdMembreFamille().equals(idMembreFamilleConcerne)) {
                    apiPriseEnCompte = !donneesHome.getIsApiFactureParHome();
                    break;
                }
            }
        }
        return apiPriseEnCompte;
    }

}
