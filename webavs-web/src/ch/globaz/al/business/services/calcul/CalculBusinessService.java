package ch.globaz.al.business.services.calcul;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.HashMap;
import ch.globaz.al.business.models.dossier.DossierComplexModelRoot;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;

/**
 * Service permettant le calcul des droits AF d'un dossier
 * 
 * @author jts
 */
public interface CalculBusinessService extends JadeApplicationService {

    /**
     * Execute le calcul des droits pour un dossier.
     * 
     * @param dossier
     *            Dossier pour lequel le calcul doit être effectué
     * @param dateCalcul
     *            date pour laquelle le calcul doit être effectué
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel} correspondant chacun au résultat du calcul
     *         pour un droit du <code>dossier</code>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public ArrayList<CalculBusinessModel> getCalcul(DossierComplexModelRoot dossier, String dateCalcul)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Calcul le montant total des droits.
     * 
     * Retourne une <code>HashMap</code> contenant les résultats accessibles à l'aide des clés :
     * <ul>
     * <li>CalculMontants.TOTAL_BASE</li>
     * <li>CalculMontants.TOTAL_EFFECTIF</li>
     * <li>CalculMontants.TOTAL_HEURE_BASE</li>
     * <li>CalculMontants.TOTAL_HEURE_EFFECTIF</li>
     * <li>CalculMontants.TOTAL_JOUR_BASE</li>
     * <li>CalculMontants.TOTAL_JOUR_EFFECTIF</li>
     * </ul>
     * 
     * @param dossier
     *            Dossier pour lequel le calcul a été être effectué
     * @param droitsCalcules
     *            Liste des droits calculé. Il s'agit d'une <code>ArrayList</code> contenant des instances de
     *            <code>CalculBusinessModel</code>
     * @param unite
     *            Unité à utiliser pour le calcul du montant effectif
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL}
     * @param nbUnite
     *            Nombre d'unité. Multiplie le montant unitaire par la valeur indiquée
     * @param avecNAIS
     *            Indique si les prestation de naissance/accueil doivent être prises en compte pour le calcul du total
     * @param date
     *            Date pour laquelle le calcul a été effectué
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel} correspondant chacun au résultat du calcul
     *         pour un droit du <code>dossier</code>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see CalculBusinessService#getCalcul(DossierComplexModelRoot, String)
     */
    public HashMap getTotal(DossierModel dossier, ArrayList<CalculBusinessModel> droitsCalcules, String unite,
            String nbUnite, boolean avecNAIS, String date) throws JadeApplicationException, JadePersistenceException;
}