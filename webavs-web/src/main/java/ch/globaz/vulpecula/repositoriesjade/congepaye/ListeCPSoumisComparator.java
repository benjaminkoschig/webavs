package ch.globaz.vulpecula.repositoriesjade.congepaye;

import java.util.Comparator;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;

public class ListeCPSoumisComparator implements Comparator<CongePaye> {

    @Override
    public int compare(CongePaye cp1, CongePaye cp2) {
        if (cp1.getEmployeur().getAffilieNumero().equals(cp2.getEmployeur().getAffilieNumero())) {
            return 0;
        } else if (cp1.getEmployeur().getAffilieNumero().compareTo(cp2.getEmployeur().getAffilieNumero()) > 0) {
            return 1;
        } else if (cp1.getEmployeur().getAffilieNumero().compareTo(cp2.getEmployeur().getAffilieNumero()) < 0) {
            return -1;
        }
        return 0;
    }

}
