package globaz.naos.db.tauxAssurance;

import java.math.BigDecimal;
import java.util.Comparator;

public class AFTauxAssuranceComparator implements Comparator {

    // méthode retournant 0 si taux1=taux2, 1 si taux1>taux2, -1 si taux1<taux2
    @Override
    public int compare(Object o1, Object o2) {
        AFTauxAssurance taux1 = (AFTauxAssurance) o1;
        AFTauxAssurance taux2 = (AFTauxAssurance) o2;

        BigDecimal decimaltaux1 = new BigDecimal(taux1.getValeurTotal());
        BigDecimal decimaltaux2 = new BigDecimal(taux2.getValeurTotal());

        return decimaltaux1.compareTo(decimaltaux2);
    }
}
