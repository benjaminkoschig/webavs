package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COParametreTaxe;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CARubrique;

public class COParametreTaxeViewBean extends COParametreTaxe implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CARubrique rubrique;

    public COParametreTaxeViewBean() {
        super();
    }

    /**
     * @return
     */
    public CARubrique getRubriqueEntity() {
        if (JadeStringUtil.isIntegerEmpty(getIdRubrique())) {
            return null;
        }
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(getIdRubrique());
            try {
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }
            } catch (Exception e) {
                rubrique = null;
            }
        }

        return rubrique;
    }
}
