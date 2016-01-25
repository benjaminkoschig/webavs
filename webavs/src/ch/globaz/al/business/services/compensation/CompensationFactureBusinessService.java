package ch.globaz.al.business.services.compensation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import ch.globaz.al.business.compensation.CompensationBusinessModel;

/**
 * Service fournissant les m�thodes n�cessaires � une compensation sur facture
 * 
 * @author jts
 * 
 */
public interface CompensationFactureBusinessService extends JadeApplicationService {

    /**
     * Charge les r�caps correspondant aux param�tres et ayant l'�tat 'TR' . Elles sont stock�e sous forme d'instance de
     * <code>CompensationDetailBusinessModel</code>.
     * 
     * @param periodeA
     *            P�riode trait�e (format MM.AAAA). Toutes les prestations ayant une fin de p�riode inf�rieur ou �gale
     *            au param�tre seront r�cup�r�es
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @return Liste des r�caps r�cup�r�e (instances de <code>CompensationBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public Collection<CompensationBusinessModel> loadRecapsPreparees(String periodeA, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les r�caps correspondant aux param�tres et ayant l'�tat 'TR' . Elles sont stock�e sous forme d'instance de
     * <code>CompensationDetailBusinessModel</code>.
     * 
     * @param idProcessus
     *            processus li� au r�caps
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @return Liste des r�caps r�cup�r�e (instances de <code>CompensationBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public Collection<CompensationBusinessModel> loadRecapsPrepareesByNumProcessus(String idProcessus, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Restaure l'�tat vers l'�tat TR pour les prestations et les r�caps ayant l'id de Passage pass� en param�tre
     * 
     * @param idPassage
     *            Id de passage pour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void restoreEtatPrestations(String idPassage) throws JadeApplicationException, JadePersistenceException;

    /**
     * Mets � jour l'�tat, l'id de passage et la date de versement des prestations ainsi que l'�tat de la r�cap pour
     * l'id de r�cap pass� en param�tre
     * 
     * @param idRecap
     *            Num�ro de r�cap AF
     * @param idPassage
     *            Num�ro de passage de la compensation
     * @param date
     *            Date de versement
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void updatePrestationsCompensees(String idRecap, String date, String idPassage)
            throws JadeApplicationException, JadePersistenceException;
}
