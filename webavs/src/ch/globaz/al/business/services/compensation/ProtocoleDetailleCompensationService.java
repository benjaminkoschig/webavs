package ch.globaz.al.business.services.compensation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import java.util.HashMap;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service permettant de générer les <code>DocumentData</code> des protocoles de compensation
 * 
 * @author jts
 */
public interface ProtocoleDetailleCompensationService extends JadeApplicationService {

    /**
     * Charge et retourne la structure de données du protocole détaillé de compensation
     * 
     * @param listeRecap
     *            liste contenant des instances de
     *            {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel}
     * @param params
     *            HashMap contenant les paramètres tels que la période, le numéro de passage, les noms du processus et
     *            du traitement
     * @return Contenu du protocole
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData getDocumentData(Collection<CompensationBusinessModel> listeRecap, HashMap<String, String> params)
            throws JadePersistenceException, JadeApplicationException;
}
