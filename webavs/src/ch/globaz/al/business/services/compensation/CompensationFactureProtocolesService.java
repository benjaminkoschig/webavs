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
 * Service permettant l'impression des protocoles li�s � la compensation sur facture
 * 
 * @author jts
 * 
 */
public interface CompensationFactureProtocolesService extends JadeApplicationService {

    /**
     * G�n�re les protocoles li�s � la compensation d�finitive des prestations.
     * 
     * @param idPassage
     *            Num�ro de passage provenant de la facturation
     * @param dateFacturation
     *            Date de la facturation
     * @param periode
     *            P�riode trait�e
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param logger
     *            Logger contenant d'�ventuelles erreur survenue pendant la compensation
     * @return <code>DocumentDataContainer</code> contenant les documents g�n�r�s (pdf + �v. csv)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DocumentDataContainer genererProtocolesDefinitif(String idPassage, String dateFacturation, String periode,
            String typeCoti, String email, ProtocoleLogger logger) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * G�n�re les protocoles li�s � la simulation de la compensation des prestations.
     * 
     * @param periode
     *            P�riode � traiter (format MM.AAAA)
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @param numTraitement
     *            Num�ro de traitement/passage
     * @param infoProcessus
     *            Code syst�me correspondant au processus
     * @param infoTraitement
     *            Code syst�me correspondant au traitement
     * @return <code>DocumentDataContainer</code> contenant les documents g�n�r�s (pdf + �v CSV)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DocumentDataContainer genererProtocolesSimulation(String periode, String typeCoti, String numTraitement,
            String infoProcessus, String infoTraitement) throws JadePersistenceException, JadeApplicationException;

    /**
     * G�n�re les protocoles li�s � la simulation de la compensation des prestations.
     * 
     * @param idProcessus
     *            processus li� aux r�caps
     * 
     * @param numTraitement
     *            Num�ro de traitement/passage
     * @param infoProcessus
     *            Code syst�me correspondant au processus
     * @param infoTraitement
     *            Code syst�me correspondant au traitement
     * @return <code>DocumentDataContainer</code> contenant les documents g�n�r�s (pdf + �v. CSV)
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public DocumentDataContainer genererProtocolesSimulationByNumProcessus(String idProcessus, String typeCoti,
            String numTraitement, String infoProcessus, String infoTraitement) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le protocole CSV de la liste affili�s
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
