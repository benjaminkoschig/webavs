package globaz.orion.utils;

import java.util.Comparator;
import ch.globaz.orion.business.models.inscription.InscriptionEbusiness;

public class InscComparator implements Comparator<InscriptionEbusiness> {

    @Override
    public int compare(InscriptionEbusiness o1, InscriptionEbusiness o2) {
        try {
            int statut1 = 0;
            int statut2 = 0;
            try {
                statut1 = Integer.parseInt(o1.getStatut());
                statut2 = Integer.parseInt(o2.getStatut());
            } catch (Exception e) {
                statut1 = 0;
                statut2 = 0;
            }

            if (statut1 == statut2) {
                if (o1.getNumAffilie().compareTo(o2.getNumAffilie()) == 0) {
                    // Si retourne 0 => tuple pas pris en compte
                    return -1;
                } else {
                    return o1.getNumAffilie().compareTo(o2.getNumAffilie());
                }

            } else if (statut1 < statut2) {
                return 1;
            }
            if (statut1 > statut2) {
                return -1;
            }
            return 0;
        } catch (Exception f) {
            // En cas de problème, on ne fait pas planter l'impression
            f.printStackTrace();
            return -1;
        }

    }

}
