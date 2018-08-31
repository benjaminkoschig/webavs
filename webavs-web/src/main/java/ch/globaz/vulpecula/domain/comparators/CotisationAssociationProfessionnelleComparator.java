/**
 * 
 */
package ch.globaz.vulpecula.domain.comparators;

import java.io.Serializable;
import java.util.Comparator;
import ch.globaz.vulpecula.domain.models.association.CotisationFacture;

/**
 * Tri des postes de travails :
 * 1. Par groupe actif (1) / inactif (-1)
 * 2. Par chronologie
 * 
 * 
 */
public class CotisationAssociationProfessionnelleComparator implements Comparator<CotisationFacture>, Serializable {
    private static final long serialVersionUID = 949051582851452262L;

    @Override
    public int compare(final CotisationFacture coti1, final CotisationFacture coti2) {

        return coti1.getCotisation().getAssociationProfessionnelle().getId()
                .compareTo(coti2.getCotisation().getAssociationProfessionnelle().getId());
    }
}
