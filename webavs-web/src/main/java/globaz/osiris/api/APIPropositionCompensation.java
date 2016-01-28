/*
 * Cr�� le 27 juin 05
 */
package globaz.osiris.api;

import globaz.framework.util.FWCurrency;
import java.util.Collection;

/**
 * @author jts 27 juin 05 10:15:47
 */
public interface APIPropositionCompensation {

    /**
     * Retourne une collection d'objet de type CASectionCompensee qui
     * 
     * @param idTiers
     *            id du tiers pour lequel il faut effectuer la compensation
     * @param montant
     *            globaz.framework.util.FWCurrency le montant � compenser sign�
     * @param ordre
     *            java.lang.int l'ordre dans lequel les factures doivent �tre compens�es
     * @return java.util.Collection une collection de Sections � compenser
     */
    public Collection<APISection> propositionCompensation(int idTiers, FWCurrency montant, int ordre) throws Exception;
}
