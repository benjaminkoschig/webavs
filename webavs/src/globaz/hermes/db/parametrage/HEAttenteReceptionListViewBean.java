package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hermes.db.gestion.HELotViewBean;

/**
 * Insérez la description du type ici. Date de création : (25.03.2003 11:12:54)
 * 
 * @author: Administrator
 */
public class HEAttenteReceptionListViewBean extends HEAttenteEnvoiListViewBean implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_getOrder(BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        // return "NUMAVS,NOM";
        return "P2.RNAVS,P2.RNDDAN,P2.RNIANN";
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    public BEntity _newEntity() throws Exception {
        HEAttenteReceptionViewBean n = new HEAttenteReceptionViewBean();
        n.setIsArchivage(Boolean.valueOf(getIsArchivage()).booleanValue());
        return n;
    }

    /**
     * @see globaz.hermes.db.parametrage.HEAttenteEnvoiListViewBean#getEnregistrementLike(BStatement)
     */
    @Override
    protected String getEnregistrementLike(BStatement statement) {
        return "(SUBSTR(P2.RNLENR,3,2)='01' OR SUBSTR(P2.RNLENR,3,3)='001')";
    }

    /**
     * @see globaz.hermes.db.parametrage.HEAttenteEnvoiListViewBean#getTypeLot()
     */
    @Override
    protected String[] getTypeLot() {
        return HELotViewBean.getLotReception();
    }
}
