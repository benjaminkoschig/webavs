package globaz.naos.db.masse;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class AFMasseListViewBean extends AFMasseManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getCotisationId(int index) {
        return ((AFMasse) getEntity(index)).getCotisationId();
    }

    public String getDateDebut(int index) {
        return ((AFMasse) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {
        return ((AFMasse) getEntity(index)).getDateFin();
    }

    public String getLibelleAssurance(int index) {
        return ((AFMasse) getEntity(index)).getCotisation().getAssurance().getAssuranceLibelleCourt();
    }

    public String getMasseId(int index) {
        return ((AFMasse) getEntity(index)).getMasseId();
    }

    public String getNouvelleMasseAnnuelle(int index) {
        return ((AFMasse) getEntity(index)).getNouvelleMasseAnnuelle();
    }

    public String getNouvelleMassePeriodicite(int index) {
        return ((AFMasse) getEntity(index)).getNouvelleMassePeriodicite();
    }

    public Boolean isTraitement(int index) {
        return ((AFMasse) getEntity(index)).isTraitement();
    }
}
