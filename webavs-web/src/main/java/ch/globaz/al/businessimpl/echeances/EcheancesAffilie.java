package ch.globaz.al.businessimpl.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Interface destinée à la gestion des avis d'échéances liés à l'affilié
 * 
 * @author PTA
 * 
 */

public interface EcheancesAffilie {

    /**
     * Chargement des échéances destinées à l'impression des avis d'échéances liées à l'allocataire
     * 
     * @param listDroits
     *            liste des droits à imprimer
     * @param numAffilie
     *            numeroAffilie
     * @param typeActivite
     *            type de l'activité
     * @return document document à imprimer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DocumentData loadData(ArrayList<DroitEcheanceComplexModel> listDroits, String numAffilie, String typeActivite)
            throws JadePersistenceException, JadeApplicationException;

}
