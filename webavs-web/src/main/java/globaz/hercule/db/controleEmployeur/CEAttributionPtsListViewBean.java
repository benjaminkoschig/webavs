package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

public class CEAttributionPtsListViewBean extends CEAttributionPtsManager implements FWViewBeanInterface {

    private static final long serialVersionUID = -3446895033889708143L;

    /**
     * Constructeur de CEAttributionPtsListViewBean
     */
    public CEAttributionPtsListViewBean() {
        super();
    }

    /**
     * @see globaz.hercule.db.controleEmployeur.CEAttributionPtsManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEAttributionPtsViewBean();
    }
}
