package ch.globaz.al.business.services.paiement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

/**
 * Service permettant l'impression des protocoles liés aux traitement des paiements direct
 * 
 * @author jts
 * 
 */
public interface PaiementDirectProtocolesService extends JadeApplicationService {
    /**
     * Génère les protocoles liés au paiement directs des prestations
     * 
     * @param idJournal
     *            Numéro de journal provenant de la compta
     * @param dateVersement
     *            Date de l'exécution du traitement
     * @param periode
     *            Période traitée
     * @param numTraitement
     *            Numéro de traitement/passage
     * @param infoProcessus
     *            Code système correspondant au processus
     * @param infoTraitement
     *            Code système correspondant au traitement
     * @param logger
     *            Logger devant
     * 
     * @return <code>DocumentDataContainer</code> contenant les documents générés ainsi qu'un logger contenant
     *         d'éventuelles erreurs
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DocumentDataContainer genererProtocolesDefinitif(String idJournal, String dateVersement, String periode,
            String numTraitement, String infoProcessus, String infoTraitement, ProtocoleLogger logger)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Génère les protocoles liés à la simulation de la compensation des prestations.
     * 
     * @param periode
     *            Période à traiter (format MM.AAAA)
     * @param numTraitement
     *            Numéro de traitement/passage
     * @param infoProcessus
     *            Code système correspondant au processus
     * @param infoTraitement
     *            Code système correspondant au traitement
     * @return <code>DocumentDataContainer</code> contenant les documents générés ainsi qu'un logger contenant
     *         d'éventuelles erreurs
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DocumentDataContainer genererProtocolesSimulation(String periode, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException;

    /**
     * Génère les protocoles liés à la simulation de la compensation des prestations en fonction d'un numéro de
     * processus et de traitement
     * 
     * 
     * @param idProcessus
     *            Numéro du processus
     * @param numTraitement
     *            Numéro du traitement
     * @param infoProcessus
     *            Code système correspondant au processus
     * @param infoTraitement
     *            Code système correspondant au traitement
     * @return < d'éventuelles erreurscode>DocumentDataContainer</code> contenant les documents générés ainsi qu'un
     *         logger contenant
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DocumentDataContainer genererProtocolesSimulationByNumProcessus(String idProcessus, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException;
}
