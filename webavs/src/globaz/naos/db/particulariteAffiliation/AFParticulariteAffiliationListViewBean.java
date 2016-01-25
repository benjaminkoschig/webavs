package globaz.naos.db.particulariteAffiliation;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité ParticulariteAffiliation.
 * 
 * @author: Administrator
 */
public class AFParticulariteAffiliationListViewBean extends AFParticulariteAffiliationManager implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getChampAlphanumerique(int index) {

        return ((AFParticulariteAffiliation) getEntity(index)).getChampAlphanumerique();
    }

    public String getChampNumerique(int index) {

        return ((AFParticulariteAffiliation) getEntity(index)).getChampNumerique();
    }

    public String getDateDebut(int index) {

        return ((AFParticulariteAffiliation) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {

        return ((AFParticulariteAffiliation) getEntity(index)).getDateFin();
    }

    public String getParticulariteAffiliation(int index) {

        return ((AFParticulariteAffiliation) getEntity(index)).getParticularite();
    }

    public String getParticulariteId(int index) {

        return ((AFParticulariteAffiliation) getEntity(index)).getParticulariteId();
    }
}
