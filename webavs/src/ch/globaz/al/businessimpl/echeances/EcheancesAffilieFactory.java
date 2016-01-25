package ch.globaz.al.businessimpl.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.exceptions.echeances.ALEcheancesException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;

/**
 * Classe factory gérant les échéances des affiliés
 * 
 * @author PTA
 * 
 */

public class EcheancesAffilieFactory {

    /**
     * Méthode qui retourne la liste des échéances de l'affilié
     * 
     * @return la liste des échéances pour un allocataire à paiement direct ou
     * @param attrAffilieTypeAvisEch
     *            type d'échéances pour l'attribu affilié
     * @param idTiersLie
     *            idTiersLie
     * @param activiteAllocataire
     *            activité de l'allocataire
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static EcheancesAffilie getEcheanceListeAffilie(AttributEntiteModel attrAffilieTypeAvisEch)
            throws JadePersistenceException, JadeApplicationException {
        if (attrAffilieTypeAvisEch != null) {
            // si liste récapitulative
            if (ALCSAffilie.ATTRIBUT_SANS_AVIS_ECH.equals(attrAffilieTypeAvisEch.getValeurAlpha())) {
                return new EcheancesAffilieIndirectSansDocAlloc();
            }// si lettre allocataire
            else if (ALCSAffilie.ATTRIBUT_AVIS_ECH_ALLOCATAIRE.equals(attrAffilieTypeAvisEch.getValeurAlpha())) {
                return new EcheancesAffilieDirect();
            }// sinon (liste+lettres)
            else {
                return new EcheancesAffilieIndirect();
            }

        } else {
            throw new ALEcheancesException(
                    "EcheancesAffilieFactory:unable to instanciate an implementation of EcheancesAffilie");
        }

    }

}
