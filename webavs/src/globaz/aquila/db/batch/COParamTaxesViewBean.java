package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COCalculTaxe;
import globaz.aquila.db.access.batch.COParamTaxes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;

public class COParamTaxesViewBean extends COParamTaxes implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createDetailEtapeTags(BSession session, String selectedId) throws Exception {
        return createEtapeTags(session, selectedId, false);
    }

    public static final String createEtapeTags(BSession session, String selectedId) throws Exception {
        return createEtapeTags(session, selectedId, true);
    }

    public COParamTaxesViewBean() {
        super();
    }

    public COCalculTaxe returnISOLangueLibelleValue(String idTrad) throws Exception {
        COCalculTaxe ct = new COCalculTaxe();
        ct.setSession(getSession());
        ct.setIdTraduction(idTrad);
        ct.retrieve();

        return ct;
    }
}
