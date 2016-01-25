package globaz.corvus.module.calcul.api;

import globaz.globall.util.JAException;
import globaz.prestation.tools.PRDateFormater;
import java.util.Comparator;

public class REPeriodeAPIComparator implements Comparator<REMontantPrestationAPIParPeriode> {

    @Override
    public int compare(REMontantPrestationAPIParPeriode c1, REMontantPrestationAPIParPeriode c2) {
        String key1;
        try {
            key1 = c1.getTypePrestation() + "-" + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(c1.getDateDebut());
        } catch (JAException e) {
            e.printStackTrace();
            key1 = c1.getTypePrestation();
        }

        String key2;
        try {
            key2 = c2.getTypePrestation() + "-" + PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(c2.getDateDebut());
        } catch (JAException e) {
            e.printStackTrace();
            key2 = c2.getTypePrestation();
        }

        return key1.compareTo(key2);
    }

}
