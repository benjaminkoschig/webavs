package globaz.orion.utils;

import java.util.Comparator;
import ch.globaz.orion.business.models.acl.Acl;

public class AclComparator implements Comparator<Acl> {

    @Override
    public int compare(Acl o1, Acl o2) {
        try {
            if (o1.getStatut().compareTo(o2.getStatut()) == 0) {
                if (o1.getNumeroAffilie().compareTo(o2.getNumeroAffilie()) == 0) {
                    if (o1.getNom().compareTo(o2.getNom()) == 0) {
                        return -1;
                    } else {
                        return o1.getNom().compareTo(o2.getNom());
                    }
                } else {
                    return o1.getNumeroAffilie().compareTo(o2.getNumeroAffilie());
                }
            } else {
                return o1.getStatut().compareTo(o2.getStatut());
            }
        } catch (Exception e) {
            // En cas de problème, on ne fait pas planter l'impression
            e.printStackTrace();
            return -1;
        }

    }

}
