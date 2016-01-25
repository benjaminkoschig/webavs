package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;

/**
 * Insérez la description du type ici. Date de création : (06.05.2003 10:46:56)
 * 
 * @author: Administrator
 */
public class HEAttenteEnvoiCIListViewBean extends HEOutputAnnonceListViewBean implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEAttenteEnvoiCIListViewBean.
     */
    public HEAttenteEnvoiCIListViewBean() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "RNIANN";
    }

    /**
     * Crée une nouvelle entité
     * 
     * @return la nouvelle entité
     * @exception java.lang.Exception
     *                si la création a échouée
     */
    @Override
    public BEntity _newEntity() {
        HEAttenteEnvoiCIViewBean n = new HEAttenteEnvoiCIViewBean();
        n.setArchivage(isArchivage());
        return n;
    }
}
