package globaz.draco.db.declaration;

import globaz.draco.vb.DSAbstractPersistentViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;

/**
 * @author BJO
 * 
 */
public class DSDecompteImpotLtnViewBean extends DSAbstractPersistentViewBean implements FWViewBeanInterface {
    private String annee = "";
    private String cantonSelectionne = "";
    private String dateImpression = "";
    private String dateValeur = JACalendar.todayJJsMMsAAAA();;
    private boolean reImpression = false;
    private boolean simulation = false;
    private String typeImpression = "pdf";

    public DSDecompteImpotLtnViewBean() throws java.lang.Exception {
    }

    public String getAnnee() {
        return annee;
    }

    public String getCantonSelectionne() {
        return cantonSelectionne;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    public boolean isReImpression() {
        return reImpression;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCantonSelectionne(String cantonSelectionne) {
        this.cantonSelectionne = cantonSelectionne;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setReImpression(boolean reImpression) {
        this.reImpression = reImpression;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }
}
