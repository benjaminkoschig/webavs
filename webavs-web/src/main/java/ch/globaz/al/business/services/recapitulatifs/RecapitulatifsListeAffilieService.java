package ch.globaz.al.business.services.recapitulatifs;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service permettant la gestion des listes dews récapitulatifs liés aux affiliés
 * 
 * @author PTA
 * 
 */
public interface RecapitulatifsListeAffilieService extends JadeApplicationService {

    /**
     * Méthode qui insère les donnnées dans le document pour les récapitulatifs
     * 
     * @param recapitulatifs
     *            liste des récapitulatifs d'entreprise
     * @param numAffilie
     *            numéro de l'affilié
     * @param idRecap
     *            identifiant de la recapitulation
     * @param periodeDe
     *            période début récap
     * @param periodeA
     *            période fin récap
     * @param agenceCommunaleAvs
     *            agence communale AVS
     * @param ActiviteAllocataire
     *            Activité de l'allocataire
     * @param dateImpression
     *            date d'impression des récap
     * @return DocumentData document à imprimer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DocumentData loadData(ArrayList recapitulatifs, String numAffilie, String idRecap, String periodeDe,
            String periodeA, String agenceCommunaleAvs, String ActiviteAllocataire, String dateImpression,
            String typeBonification) throws JadePersistenceException, JadeApplicationException;

    /**
     * Méthode qui retourne les données pour fichier CSV
     * 
     * @param listTempRecap
     *            liste des récap.
     * @return ArrayList
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    StringBuffer loadDataCSV(ArrayList listTempRecap) throws JadePersistenceException, JadeApplicationException;
}
