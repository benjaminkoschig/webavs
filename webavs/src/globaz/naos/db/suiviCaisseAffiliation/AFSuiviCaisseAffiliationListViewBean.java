/*
 * Created on 04-Jul-05
 */
package globaz.naos.db.suiviCaisseAffiliation;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Le listViewBean de l'entité SuiviCaisseAffiliation.
 * 
 * @author sau
 */
public class AFSuiviCaisseAffiliationListViewBean extends AFSuiviCaisseAffiliationManager implements
        FWViewBeanInterface {

    private static final long serialVersionUID = 5679919800072138886L;

    public Boolean getAccessoire(int index) {
        return ((AFSuiviCaisseAffiliation) getEntity(index)).getAccessoire();

    }

    public String getCodeAdministration(int index) {
        if (((AFSuiviCaisseAffiliation) getEntity(index)).getAdministration() != null) {
            return ((AFSuiviCaisseAffiliation) getEntity(index)).getAdministration().getCodeAdministration();
        } else {
            return "";
        }

    }

    public String getDateDebut(int index) {
        return ((AFSuiviCaisseAffiliation) getEntity(index)).getDateDebut();
    }

    public String getDateFin(int index) {
        return ((AFSuiviCaisseAffiliation) getEntity(index)).getDateFin();
    }

    public String getGenreCaisse(int index) {
        return ((AFSuiviCaisseAffiliation) getEntity(index)).getGenreCaisse();
    }

    public String getMotif(int index) {
        return ((AFSuiviCaisseAffiliation) getEntity(index)).getMotif();
    }

    public String getSuiviCaisseId(int index) {
        return ((AFSuiviCaisseAffiliation) getEntity(index)).getSuiviCaisseId();
    }
}
