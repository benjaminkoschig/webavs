package ch.globaz.al.businessimpl.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Interface destin�e � la gestion des avis d'�ch�ances li�s � l'allocataire
 * 
 * @author PTA
 * 
 */

public interface EcheancesAllocataire {

    /**
     * Indique la langue du document impl�mentant l'interface
     * 
     * @return
     */
    String getLangueDocument();

    /**
     * Chargement des �ch�ances destin�es � l'impression des avis d'�ch�ances li�es � l'allocataire
     * 
     * @param droits
     *            liste des droits � imprimer
     * @param nss
     *            num�ro nss
     * @param idTiersAllocataire
     *            identifiant du tiers allocataire
     * @param numDossier
     *            identifiant du dossier
     * @param numAffilie
     *            num�ro de l'affili�
     * @param nomAlloc
     *            nom de l'allocataire
     * @param prenomAlloc
     *            pr�nom de l'allocataire
     * @param titre
     *            titre de l'allocataire
     * @param idTiersBenficiaire
     *            identifiant du tiers b�n�ficiaire
     * @return document document � imprimer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
            String numDossier, String numAffilie, String nomAlloc, String prenomAlloc, String titre,
            String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException;

    /**
     * Chargement des �ch�ances destin�es � l'impression des avis d'�ch�ances li�es � l'allocataire
     *
     * @param droits
     *            liste des droits � imprimer
     * @param nss
     *            num�ro nss
     * @param idTiersAllocataire
     *            identifiant du tiers allocataire
     * @param numDossier
     *            identifiant du dossier
     * @param numAffilie
     *            num�ro de l'affili�
     * @param numContribuable
     *            num�ro de contribuable
     * @param nomAlloc
     *            nom de l'allocataire
     * @param prenomAlloc
     *            pr�nom de l'allocataire
     * @param titre
     *            titre de l'allocataire
     * @param idTiersBenficiaire
     *            identifiant du tiers b�n�ficiaire
     * @return document document � imprimer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String nss, String idTiersAllocataire,
                          String numDossier, String numAffilie, String numContribuable, String nomAlloc, String prenomAlloc, String titre,
                          String idTiersBenficiaire) throws JadePersistenceException, JadeApplicationException;

}
