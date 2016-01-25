package ch.globaz.al.business.services.recapitulatifs;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service permettant la gestion des listes dews r�capitulatifs li�s aux affili�s
 * 
 * @author PTA
 * 
 */
public interface RecapitulatifsListeAffilieService extends JadeApplicationService {

    /**
     * M�thode qui ins�re les donnn�es dans le document pour les r�capitulatifs
     * 
     * @param recapitulatifs
     *            liste des r�capitulatifs d'entreprise
     * @param numAffilie
     *            num�ro de l'affili�
     * @param idRecap
     *            identifiant de la recapitulation
     * @param periodeDe
     *            p�riode d�but r�cap
     * @param periodeA
     *            p�riode fin r�cap
     * @param agenceCommunaleAvs
     *            agence communale AVS
     * @param ActiviteAllocataire
     *            Activit� de l'allocataire
     * @param dateImpression
     *            date d'impression des r�cap
     * @return DocumentData document � imprimer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DocumentData loadData(ArrayList recapitulatifs, String numAffilie, String idRecap, String periodeDe,
            String periodeA, String agenceCommunaleAvs, String ActiviteAllocataire, String dateImpression,
            String typeBonification) throws JadePersistenceException, JadeApplicationException;

    /**
     * M�thode qui retourne les donn�es pour fichier CSV
     * 
     * @param listTempRecap
     *            liste des r�cap.
     * @return ArrayList
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    StringBuffer loadDataCSV(ArrayList listTempRecap) throws JadePersistenceException, JadeApplicationException;
}
