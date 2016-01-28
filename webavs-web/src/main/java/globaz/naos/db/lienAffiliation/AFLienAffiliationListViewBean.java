/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.lienAffiliation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TITiers;

/**
 * Le listViewBean de l'entité LienAffiliation.
 * 
 * @author sau
 */
public class AFLienAffiliationListViewBean extends AFLienAffiliationManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFLienAffiliationViewBean();
    }

    public String getAff_AffiliationId(int index) {
        return ((AFLienAffiliation) getEntity(index)).getAff_AffiliationId();
    }

    public AFAffiliation getAffiliation(int index) {
        return ((AFLienAffiliation) getEntity(index)).getAffiliation();
    }

    public String getAffiliationId(int index) {
        return ((AFLienAffiliation) getEntity(index)).getAffiliationId();
    }

    public String getDateDebut(int index) {
        return ((AFLienAffiliation) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {
        return ((AFLienAffiliation) getEntity(index)).getDateFin();
    }

    public AFAffiliation getLienAffiliation(int index) {
        return ((AFLienAffiliation) getEntity(index)).getLienAffiliation();
    }

    public String getLienAffiliationId(int index) {
        return ((AFLienAffiliation) getEntity(index)).getLienAffiliationId();
    }

    public TITiers getLienTiers(int index) {
        return ((AFLienAffiliation) getEntity(index)).getLienTiers();
    }

    public TITiers getTiers(int index) {
        return ((AFLienAffiliation) getEntity(index)).getTiers();
    }

    public String getTypeLien(int index) {
        return ((AFLienAffiliation) getEntity(index)).getTypeLien();
    }

}
