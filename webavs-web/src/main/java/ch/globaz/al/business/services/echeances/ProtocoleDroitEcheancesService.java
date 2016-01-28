package ch.globaz.al.business.services.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service permettant la cr�ation d'un document de donn�es ( <code>DocumentData</code>) pour g�n�ration d'un protocole
 * de liste d'�ch�ances des droits
 * 
 * @author PTA
 * 
 */
public interface ProtocoleDroitEcheancesService extends JadeApplicationService {

    /**
     * M�thode qui ins�re les donn�es dans le document du protocole d'�ch�ance
     * 
     * @param droits
     *            liste des droits
     * @param typeList
     *            type de liste (avis d'�ch�ances, autres �ch�ances)
     * @return DocumentData donn�es d'un protocole de g�n�ration d'une liste des �ch�ances
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String typeList, String dateEcheance)
            throws JadePersistenceException, JadeApplicationException;
}
