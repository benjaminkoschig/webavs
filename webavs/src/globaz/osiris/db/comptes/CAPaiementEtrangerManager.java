package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;
import globaz.osiris.api.APIOperation;

/**
 * Insérez la description du type ici. Date de création : (14.02.2002 13:35:05)
 * 
 * @author: Administrator
 */
public class CAPaiementEtrangerManager extends CAPaiementManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean orderByISOMe = false;
    private boolean orderByRubrique = false;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        String _order = super._getOrder(statement);

        if (isOrderByRubrique()) {
            _order = _getCollection() + "CARUBRP.IDEXTERNE, " + _getCollection() + "CAOPERP.AECISO";
        } else if (isOrderByISOMe()) {
            _order = _getCollection() + "CAOPERP.AECISO, " + _getCollection() + "CARUBRP.IDEXTERNE";
        }

        return _order;
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {

        // Récupérer depuis la superclasse
        setForIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
        setLikeIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);

        String sqlWhere = super._getWhere(statement);

        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CAPaiementEtranger();
    }

    /**
     * @return
     */
    public boolean isOrderByISOMe() {
        return orderByISOMe;
    }

    /**
     * @return
     */
    public boolean isOrderByRubrique() {
        return orderByRubrique;
    }

    /**
     * @param b
     */
    public void setOrderByISOMe(boolean b) {
        orderByISOMe = b;
    }

    /**
     * @param boolean1
     */
    public void setOrderByRubrique(boolean boolean1) {
        orderByRubrique = boolean1;
    }

}
