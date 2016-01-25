package globaz.pavo.db.bta;

import java.util.Comparator;

public class CIRequerantBtaComparator implements Comparator {
    // Comparateur de requerant (sur l'id du requérant)
    @Override
    public int compare(Object o1, Object o2) {
        CIRequerantBta req1 = (CIRequerantBta) o1;
        CIRequerantBta req2 = (CIRequerantBta) o2;

        String idReq1 = new String(req1.getIdRequerant());
        String idReq2 = new String(req2.getIdRequerant());

        return idReq1.compareTo(idReq2);
    }
}
