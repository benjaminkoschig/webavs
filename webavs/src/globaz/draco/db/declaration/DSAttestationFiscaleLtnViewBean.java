package globaz.draco.db.declaration;

import globaz.draco.vb.DSAbstractPersistentViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.naos.db.affiliation.AFAffiliation;

/**
 * @author BJO
 * 
 */
public class DSAttestationFiscaleLtnViewBean extends DSAbstractPersistentViewBean implements FWViewBeanInterface {
    private String dateValeur = JACalendar.todayJJsMMsAAAA();
    private boolean simulation = false;

    public DSAttestationFiscaleLtnViewBean() throws java.lang.Exception {
    }

    public AFAffiliation getAffilie() {
        return getDeclaration().getAffiliation();
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public DSDeclarationViewBean getDeclaration() {
        DSDeclarationViewBean declaration = new DSDeclarationViewBean();
        try {
            declaration = new DSDeclarationViewBean();
            declaration.setSession(getSession());
            declaration.setIdDeclaration(getId());
            declaration.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return declaration;

    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }
}
