/**
 * 
 */
package ch.globaz.vulpecula.domain.comparators;

import java.io.Serializable;
import java.util.Comparator;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * Tri des poste de travail par ordre alphabétique des travailleurs
 * 
 */
public class PosteTravailsAlphabetiqueComparator implements Comparator<PosteTravail>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final PosteTravail p1, final PosteTravail p2) {
        return 0;
    }

}
