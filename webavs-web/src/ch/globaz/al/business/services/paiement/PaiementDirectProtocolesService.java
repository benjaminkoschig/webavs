package ch.globaz.al.business.services.paiement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

/**
 * Service permettant l'impression des protocoles li�s aux traitement des paiements direct
 * 
 * @author jts
 * 
 */
public interface PaiementDirectProtocolesService extends JadeApplicationService {
    /**
     * G�n�re les protocoles li�s au paiement directs des prestations
     * 
     * @param idJournal
     *            Num�ro de journal provenant de la compta
     * @param dateVersement
     *            Date de l'ex�cution du traitement
     * @param periode
     *            P�riode trait�e
     * @param numTraitement
     *            Num�ro de traitement/passage
     * @param infoProcessus
     *            Code syst�me correspondant au processus
     * @param infoTraitement
     *            Code syst�me correspondant au traitement
     * @param logger
     *            Logger devant
     * 
     * @return <code>DocumentDataContainer</code> contenant les documents g�n�r�s ainsi qu'un logger contenant
     *         d'�ventuelles erreurs
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DocumentDataContainer genererProtocolesDefinitif(String idJournal, String dateVersement, String periode,
            String numTraitement, String infoProcessus, String infoTraitement, ProtocoleLogger logger)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * G�n�re les protocoles li�s � la simulation de la compensation des prestations.
     * 
     * @param periode
     *            P�riode � traiter (format MM.AAAA)
     * @param numTraitement
     *            Num�ro de traitement/passage
     * @param infoProcessus
     *            Code syst�me correspondant au processus
     * @param infoTraitement
     *            Code syst�me correspondant au traitement
     * @return <code>DocumentDataContainer</code> contenant les documents g�n�r�s ainsi qu'un logger contenant
     *         d'�ventuelles erreurs
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DocumentDataContainer genererProtocolesSimulation(String periode, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException;

    /**
     * G�n�re les protocoles li�s � la simulation de la compensation des prestations en fonction d'un num�ro de
     * processus et de traitement
     * 
     * 
     * @param idProcessus
     *            Num�ro du processus
     * @param numTraitement
     *            Num�ro du traitement
     * @param infoProcessus
     *            Code syst�me correspondant au processus
     * @param infoTraitement
     *            Code syst�me correspondant au traitement
     * @return < d'�ventuelles erreurscode>DocumentDataContainer</code> contenant les documents g�n�r�s ainsi qu'un
     *         logger contenant
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DocumentDataContainer genererProtocolesSimulationByNumProcessus(String idProcessus, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException;
}
