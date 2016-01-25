package globaz.orion.utils;

import java.util.Comparator;
import ch.globaz.orion.business.models.acl.EBAttestationComparatorInterface;

public class AclComparatorSuccursale implements Comparator<EBAttestationComparatorInterface> {

    @Override
    public int compare(EBAttestationComparatorInterface o1, EBAttestationComparatorInterface o2) {
        try {
            String succ1 = "";
            String succ2 = "";

            succ1 = o1.getNoSuccursale();
            if (succ1 == null) {
                succ1 = "0";
            }
            succ2 = o2.getNoSuccursale();
            if (succ2 == null) {
                succ2 = "0";
            }
            if (o1.getUser().compareTo(o2.getUser()) == 0) {

                if (o1.getNumeroAffilie().compareTo(o2.getNumeroAffilie()) == 0) {
                    if (succ1.equals(succ2)) {
                        if (o1.getNom().compareTo(o2.getNom()) == 0) {
                            // attention si le comparator retourne 0, la structure n'est pas inséré dans la map
                            return -1;
                        } else {
                            return o1.getNom().compareTo(o2.getNom());
                        }
                    } else {
                        return succ1.compareTo(succ2);
                    }
                } else {
                    return o1.getNumeroAffilie().compareTo(o2.getNumeroAffilie());
                }
            } else {
                return o1.getUser().compareTo(o2.getUser());
            }
        } catch (Exception e) {
            // En cas de problème, on ne fait pas planter l'impression
            e.printStackTrace();
            return -1;
        }
    }
}
