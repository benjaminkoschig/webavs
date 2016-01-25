package ch.globaz.al.businessimpl.echeances;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.exceptions.echeances.ALEcheancesException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;

/**
 * Classe factory g�rant les �ch�ances des affili�s
 * 
 * @author PTA
 * 
 */

public class EcheancesAffilieFactory {

    /**
     * M�thode qui retourne la liste des �ch�ances de l'affili�
     * 
     * @return la liste des �ch�ances pour un allocataire � paiement direct ou
     * @param attrAffilieTypeAvisEch
     *            type d'�ch�ances pour l'attribu affili�
     * @param idTiersLie
     *            idTiersLie
     * @param activiteAllocataire
     *            activit� de l'allocataire
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static EcheancesAffilie getEcheanceListeAffilie(AttributEntiteModel attrAffilieTypeAvisEch)
            throws JadePersistenceException, JadeApplicationException {
        if (attrAffilieTypeAvisEch != null) {
            // si liste r�capitulative
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
