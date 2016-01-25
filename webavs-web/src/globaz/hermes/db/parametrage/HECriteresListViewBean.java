package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (30.10.2002 14:50:45)
 * 
 * @author: Administrator
 */
public class HECriteresListViewBean extends HECriteresManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getIdCode(int pos) {
        HECriteres entity = (HECriteres) getEntity(pos);
        return entity.getIdCode();
        // return pos + "";
    }

    public String getLibelle(int pos) {
        HECriteres entity = (HECriteres) getEntity(pos);
        return entity.getLibelle();
        // return "Libelle " + pos;
    }
}
