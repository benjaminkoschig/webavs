package globaz.corvus.acorweb.business;

import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.jade.client.util.JadeStringUtil;

import java.text.SimpleDateFormat;
import java.util.Comparator;

public class REDateNaissanceComparator implements Comparator<ISFMembreFamilleRequerant> {
    @Override
    public int compare(ISFMembreFamilleRequerant o1, ISFMembreFamilleRequerant o2) {
        String ddn1 = o1.getDateNaissance();
        String ddn2 = o2.getDateNaissance();

        // 2 dates vides
        if (JadeStringUtil.isBlankOrZero(ddn1) && JadeStringUtil.isBlankOrZero(ddn2)) {
            return 0;
        }

        if (JadeStringUtil.isBlankOrZero(ddn1)) {
            return -1;
        }

        if (JadeStringUtil.isBlankOrZero(ddn2)) {
            return 1;
        }

        SimpleDateFormat reader = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat writer = new SimpleDateFormat("yyyyMMdd");

        int d1 = 0;
        int d2 = 0;
        try {
            d1 = Integer.valueOf(writer.format(reader.parse(ddn1)));
            d2 = Integer.valueOf(writer.format(reader.parse(ddn2)));
        } catch (Exception e) {
            // What can I do :(
        }

        if (d1 > d2) {
            return -1;
        } else if (d2 < d1) {
            return -1;
        } else {
            return 0;
        }
    }
}
