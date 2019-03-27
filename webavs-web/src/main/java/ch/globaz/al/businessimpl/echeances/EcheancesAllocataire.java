package ch.globaz.al.businessimpl.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Interface destinée à la gestion des avis d'échéances liés à l'allocataire
 * 
 * @author PTA
 * 
 */

public interface EcheancesAllocataire {

    /**
     * Indique la langue du document implémentant l'interface
     * 
     * @return
     */
    String getLangueDocument();

    /**
     * Chargement des échéances destinées à l'impression des avis d'échéances liées à l'allocataire
     * 
     * @param droits
     *            liste des droits à imprimer
     * @param nss
     *            numéro nss
     * @param idTiersAllocataire
     *            identifiant du tiers allocataire
     * @param numDossier
     *            identifiant du dossier
     * @param numAffilie
     *            numéro de l'affilié
     * @param nomAlloc
     *            nom de l'allocataire
     * @param prenomAlloc
     *            prénom de l'allocataire
     * @param titre
     *            titre de l'allocataire
     * @param idTiersBenficiaire
     *            identifiant du tiers bénéficiaire
     * @return document document à imprimer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
            String numDossier, String numAffilie, String nomAlloc, String prenomAlloc, String titre,
            String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException;

    /**
     * Chargement des échéances destinées à l'impression des avis d'échéances liées à l'allocataire
     *
     * @param droits
     *            liste des droits à imprimer
     * @param nss
     *            numéro nss
     * @param idTiersAllocataire
     *            identifiant du tiers allocataire
     * @param numDossier
     *            identifiant du dossier
     * @param numAffilie
     *            numéro de l'affilié
     * @param numContribuable
     *            numéro de contribuable
     * @param nomAlloc
     *            nom de l'allocataire
     * @param prenomAlloc
     *            prénom de l'allocataire
     * @param titre
     *            titre de l'allocataire
     * @param idTiersBenficiaire
     *            identifiant du tiers bénéficiaire
     * @return document document à imprimer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
                          String numDossier, String numAffilie, String numContribuable, String nomAlloc, String prenomAlloc, String titre,
                          String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException;

}
