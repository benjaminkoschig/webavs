package ch.globaz.al.businessimpl.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Classe factory gérant les échéances des allocataires
 * 
 * @author PTA
 * 
 */
public class EcheancesAllocataireFactory {

    /**
     * Méthode qui retourne la liste des échéance de l'allocataire
     * 
     * @param droitModelAlloc
     *            modèle de DroitEcheanceComplexeModel
     * @param attributAffilieTypeAvisEch
     *            type d'avis d'échéance
     * @param attributAffiliePaiementDirect
     *            tpye affilie (direct)
     * @return la liste des échéances pour un allocataire à paiement direct ou la liste pour un allocataire à paiement
     *         indirect
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
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
