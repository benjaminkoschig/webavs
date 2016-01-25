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
     *            Dossier pour lequel le calcul doit �tre effectu�
     * @param dateCalcul
     *            date pour laquelle le calcul doit �tre effectu�
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel} correspondant chacun au r�sultat du calcul
     *         pour un droit du <code>dossier</code>
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public ArrayList<CalculBusinessModel> getCalcul(DossierComplexModelRoot dossier, String dateCalcul)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Calcul le montant total des droits.
     * 
     * Retourne une <code>HashMap</code> contenant les r�sultats accessibles � l'aide des cl�s :
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
     *            Dossier pour lequel le calcul a �t� �tre effectu�
     * @param droitsCalcules
     *            Liste des droits calcul�. Il s'agit d'une <code>ArrayList</code> contenant des instances de
     *            <code>CalculBusinessModel</code>
     * @param unite
     *            Unit� � utiliser pour le calcul du montant effectif
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL}
     * @param nbUnite
     *            Nombre d'unit�. Multiplie le montant unitaire par la valeur indiqu�e
     * @param avecNAIS
     *            Indique si les prestation de naissance/accueil doivent �tre prises en compte pour le calcul du total
     * @param date
     *            Date pour laquelle le calcul a �t� effectu�
     * @return Une <code>ArrayList</code> contenant des objets
     *         {@link ch.globaz.al.business.models.droit.CalculBusinessModel} correspondant chacun au r�sultat du calcul
     *         pour un droit du <code>dossier</code>
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @see CalculBusinessService#getCalcul(DossierComplexModelRoot, String)
     */
    public HashMap getTotal(DossierModel dossier, ArrayList<CalculBusinessModel> droitsCalcules, String unite,
            String nbUnite, boolean avecNAIS, String date) throws JadeApplicationException, JadePersistenceException;
}