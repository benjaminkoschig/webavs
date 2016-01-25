package ch.globaz.al.businessimpl.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Classe factory g�rant les �ch�ances des allocataires
 * 
 * @author PTA
 * 
 */
public class EcheancesAllocataireFactory {

    /**
     * M�thode qui retourne la liste des �ch�ance de l'allocataire
     * 
     * @param droitModelAlloc
     *            mod�le de DroitEcheanceComplexeModel
     * @param attributAffilieTypeAvisEch
     *            type d'avis d'�ch�ance
     * @param attributAffiliePaiementDirect
     *            tpye affilie (direct)
     * @return la liste des �ch�ances pour un allocataire � paiement direct ou la liste pour un allocataire � paiement
     *         indirect
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    public static EcheancesAllocataire getEcheanceListeAllocataire(DroitEcheanceComplexModel droitModelAlloc,
            AttributEntiteModel attributAffilieTypeAvisEch, boolean isDocAffilieGenerated)
            throws JadePersistenceException, JadeApplicationException {

        if (droitModelAlloc == null) {
            throw new ALEcheanceModelException(
                    "EcheancesListeAllocataireFactory#getEcheanceListeAllocataire : droitModelAlloc is null");
        }

        if (ALCSAffilie.ATTRIBUT_AVIS_ECH_AFFILIE.equals(attributAffilieTypeAvisEch.getValeurAlpha())
                && isDocAffilieGenerated) {
            return new EcheancesAllocataireViaAffilie();
        }// FIXME: attribut affilie direct influence aussi ??
        else if (ALServiceLocator.getDroitEcheanceService().getTypePaiement(droitModelAlloc)) {
            return new EcheancesAllocataireDirect();
        } else {
            return new EcheancesAllocataireIndirectAvecAvisEcheance();
        }

    }
}
