package globaz.hermes.utils;

import globaz.hermes.print.itext.HEDocumentRemiseCAStruct;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.Comparator;

public class HECompareCA implements Comparator, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Object o1, Object o2) {
        // ce compare ne doit pas retourner 0 sinon, le TreeSet ne l'ajoute pas
        // !
        // ALD : bz 1699 trier par utilisateur
        int compareUser = ((HEDocumentRemiseCAStruct) o1).getUser()
                .compareTo(((HEDocumentRemiseCAStruct) o2).getUser());

        if (compareUser == 0) {

            if (JadeStringUtil.isEmpty(((HEDocumentRemiseCAStruct) o1).getNAffilie())
                    && !JadeStringUtil.isEmpty(((HEDocumentRemiseCAStruct) o2).getNAffilie())) {
                return -1;
            }
            if (!JadeStringUtil.isEmpty(((HEDocumentRemiseCAStruct) o1).getNAffilie())
                    && JadeStringUtil.isEmpty(((HEDocumentRemiseCAStruct) o2).getNAffilie())) {
                return 1;
            }
            if (JadeStringUtil.isEmpty(((HEDocumentRemiseCAStruct) o1).getNAffilie())
                    && JadeStringUtil.isEmpty(((HEDocumentRemiseCAStruct) o2).getNAffilie())) {
                int compare = ((HEDocumentRemiseCAStruct) o1).getNom().compareTo(
                        ((HEDocumentRemiseCAStruct) o2).getNom());
                if (compare == 0) {
                    return -1;
                } else {
                    return compare;
                }
            }
            if (!JadeStringUtil.isEmpty(((HEDocumentRemiseCAStruct) o1).getNAffilie())
                    && !JadeStringUtil.isEmpty(((HEDocumentRemiseCAStruct) o2).getNAffilie())) {
                int compare = ((HEDocumentRemiseCAStruct) o1).getNAffilie().compareTo(
                        ((HEDocumentRemiseCAStruct) o2).getNAffilie());
                if (compare == 0) {
                    compare = ((HEDocumentRemiseCAStruct) o1).getNom().compareTo(
                            ((HEDocumentRemiseCAStruct) o2).getNom());
                    if (compare == 0) {
                        return -1;
                    } else {
                        return compare;
                    }
                } else {
                    return compare;
                }
            }
        } else {
            return compareUser;
        }
        return -1;
    }

}
