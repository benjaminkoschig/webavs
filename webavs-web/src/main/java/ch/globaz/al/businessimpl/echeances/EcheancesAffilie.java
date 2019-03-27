package ch.globaz.al.businessimpl.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Interface destin�e � la gestion des avis d'�ch�ances li�s � l'affili�
 * 
 * @author PTA
 * 
 */

public interface EcheancesAffilie {

    /**
     * Chargement des �ch�ances destin�es � l'impression des avis d'�ch�ances li�es � l'allocataire
     * 
     * @param listDroits
     *            liste des droits � imprimer
     * @param numAffilie
     *            numeroAffilie
     * @param typeActivite
     *            type de l'activit�
     * @return document document � imprimer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DocumentData loadData(ArrayList<DroitEcheanceComplexModel> listDroits, String numAffilie, String typeActivite)
            throws JadePersistenceException, JadeApplicationException;

}
