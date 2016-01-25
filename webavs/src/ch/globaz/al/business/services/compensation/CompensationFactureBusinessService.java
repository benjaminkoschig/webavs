package ch.globaz.al.business.services.compensation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import ch.globaz.al.business.compensation.CompensationBusinessModel;

/**
 * Service fournissant les méthodes nécessaires à une compensation sur facture
 * 
 * @author jts
 * 
 */
public interface CompensationFactureBusinessService extends JadeApplicationService {

    /**
     * Charge les récaps correspondant aux paramètres et ayant l'état 'TR' . Elles sont stockée sous forme d'instance de
     * <code>CompensationDetailBusinessModel</code>.
     * 
     * @param periodeA
     *            Période traitée (format MM.AAAA). Toutes les prestations ayant une fin de période inférieur ou égale
     *            au paramètre seront récupérées
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @return Liste des récaps récupérée (instances de <code>CompensationBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public Collection<CompensationBusinessModel> loadRecapsPreparees(String periodeA, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les récaps correspondant aux paramètres et ayant l'état 'TR' . Elles sont stockée sous forme d'instance de
     * <code>CompensationDetailBusinessModel</code>.
     * 
     * @param idProcessus
     *            processus lié au récaps
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @return Liste des récaps récupérée (instances de <code>CompensationBusinessModel</code>)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public Collection<CompensationBusinessModel> loadRecapsPrepareesByNumProcessus(String idProcessus, String typeCoti)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Restaure l'état vers l'état TR pour les prestations et les récaps ayant l'id de Passage passé en paramètre
     * 
     * @param idPassage
     *            Id de passage pour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void restoreEtatPrestations(String idPassage) throws JadeApplicationException, JadePersistenceException;

    /**
     * Mets à jour l'état, l'id de passage et la date de versement des prestations ainsi que l'état de la récap pour
     * l'id de récap passé en paramètre
     * 
     * @param idRecap
     *            Numéro de récap AF
     * @param idPassage
     *            Numéro de passage de la compensation
     * @param date
     *            Date de versement
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void updatePrestationsCompensees(String idRecap, String date, String idPassage)
            throws JadeApplicationException, JadePersistenceException;
}
