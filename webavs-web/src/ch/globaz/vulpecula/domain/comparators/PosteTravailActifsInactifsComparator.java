/**
 * 
 */
package ch.globaz.vulpecula.domain.comparators;

import java.io.Serializable;
import java.util.Comparator;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * Tri des postes de travails :
 * 1. Par groupe actif (1) / inactif (-1)
 * 2. Par chronologie
 * 
 * 
 */
public class PosteTravailActifsInactifsComparator implements Comparator<PosteTravail>, Serializable {

    @Override
    public int compare(final PosteTravail p1, final PosteTravail p2) {
        if (p1.isActif() && !p2.isActif()) {
            return -1;
        } else if (!p1.isActif() && p2.isActif()) {
            return 1;
        } else if (!p1.isActif() && !p2.isActif() || p1.isActif() && p2.isActif()) {
            if (p1.getPeriodeActivite() == null || p2.getPeriodeActivite() == null) {
                return 0;
            }
            if (p1.getFinActivite() == null && p2.getFinActivite() == null) {
                if (p1.getDebutActivite().before(p2.getDebutActivite())) {
                    return 1;
                } else if (p1.getDebutActivite().after(p2.getDebutActivite())) {
                    return -1;
                } else {
                    return 0;
                }
            } else if (p1.getFinActivite() == null && p2.getFinActivite() != null) {
                return -1;
            } else if (p1.getFinActivite() != null && p2.getFinActivite() == null) {
                return -1;
            } else if (p1.getFinActivite().after(p2.getFinActivite())) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }
}
