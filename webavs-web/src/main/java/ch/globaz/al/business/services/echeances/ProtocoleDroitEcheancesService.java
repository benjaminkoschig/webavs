package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service permettant la création d'un document de données ( <code>DocumentData</code>) pour génération d'un protocole
 * de liste d'échéances des droits
 * 
 * @author PTA
 * 
 */
public interface ProtocoleDroitEcheancesService extends JadeApplicationService {

    /**
     * Méthode qui insère les données dans le document du protocole d'échéance
     * 
     * @param droits
     *            liste des droits
     * @param typeList
     *            type de liste (avis d'échéances, autres échéances)
     * @return DocumentData données d'un protocole de génération d'une liste des échéances
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String typeList, String dateEcheance)
            throws JadePersistenceException, JadeApplicationException;
}
