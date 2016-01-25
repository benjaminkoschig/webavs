package ch.globaz.al.business.services.compensation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import java.util.HashMap;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.services.documents.DocumentDataContainer;

/**
 * Service permettant l'impression des protocoles liés à la compensation sur facture
 * 
 * @author jts
 * 
 */
public interface CompensationFactureProtocolesService extends JadeApplicationService {

    /**
     * Génère les protocoles liés à la compensation définitive des prestations.
     * 
     * @param idPassage
     *            Numéro de passage provenant de la facturation
     * @param dateFacturation
     *            Date de la facturation
     * @param periode
     *            Période traitée
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param logger
     *            Logger contenant d'éventuelles erreur survenue pendant la compensation
     * @return <code>DocumentDataContainer</code> contenant les documents générés (pdf + év. csv)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DocumentDataContainer genererProtocolesDefinitif(String idPassage, String dateFacturation, String periode,
            String typeCoti, String email, ProtocoleLogger logger) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Génère les protocoles liés à la simulation de la compensation des prestations.
     * 
     * @param periode
     *            Période à traiter (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param numTraitement
     *            Numéro de traitement/passage
     * @param infoProcessus
     *            Code système correspondant au processus
     * @param infoTraitement
     *            Code système correspondant au traitement
     * @return <code>DocumentDataContainer</code> contenant les documents générés (pdf + év CSV)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DocumentDataContainer genererProtocolesSimulation(String periode, String typeCoti, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException;

    /**
     * Génère les protocoles liés à la simulation de la compensation des prestations.
     * 
     * @param idProcessus
     *            processus lié aux récaps
     * 
     * @param numTraitement
     *            Numéro de traitement/passage
     * @param infoProcessus
     *            Code système correspondant au processus
     * @param infoTraitement
     *            Code système correspondant au traitement
     * @return <code>DocumentDataContainer</code> contenant les documents générés (pdf + év. CSV)
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DocumentDataContainer genererProtocolesSimulationByNumProcessus(String idProcessus, String typeCoti,
            String numTraitement, String infoProcessus, String infoTraitement) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le protocole CSV de la liste affiliés
     * 
     * @param prest
     * @param params
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String getProtocoleCSVListeAffilie(HashMap<String, Collection<CompensationBusinessModel>> recapsByActivites,
            HashMap<String, String> params) throws JadeApplicationException, JadePersistenceException;

}
