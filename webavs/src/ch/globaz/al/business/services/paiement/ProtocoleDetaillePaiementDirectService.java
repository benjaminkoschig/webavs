package ch.globaz.al.business.services.paiement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.paiement.PaiementBusinessModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service permettant de g�n�rer les <code>DocumentData</code> des protocoles de compensation
 * 
 * @author jts
 */
public interface ProtocoleDetaillePaiementDirectService extends JadeApplicationService {

    /**
     * Charge et retourne la structure de donn�es du protocole d�taill� de paiement direct
     * 
     * @param listeRecap
     *            liste contenant des instances de
     *            {@link ch.globaz.al.businessimpl.compensation.CompensationBusinessModel}
     * @param params
     *            HashMap contenant les param�tres tels que la p�riode, le num�ro de passage, les noms du processus et
     *            du traitement
     * @return Contenu du protocole
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData getDocumentData(java.util.Collection<PaiementBusinessModel> listePaiement,
            HashMap<String, String> params) throws JadePersistenceException, JadeApplicationException;
}
