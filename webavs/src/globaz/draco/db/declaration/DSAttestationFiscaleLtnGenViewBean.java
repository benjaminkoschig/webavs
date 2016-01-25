package globaz.draco.db.declaration;

import globaz.draco.vb.DSAbstractPersistentViewBean;
import globaz.globall.util.JACalendar;

/**
 * ViewBean utilisé pour mapper des datas qui vont permettre de lancer le process...
 * 
 * @author BJO
 */
public class DSAttestationFiscaleLtnGenViewBean extends DSAbstractPersistentViewBean {
    private boolean affilieTous = false;
    private String annee;
    private String dateImpression;// Pour ré-imprimer des attestations déjà taguée
    private String dateValeur = JACalendar.todayJJsMMsAAAA();// date sur le document
    private String fromAffilies;
    private boolean simulation = false;
    private String untilAffilies;

    // imprimer à cette date

    public DSAttestationFiscaleLtnGenViewBean() {
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateImpression() {
        return dateImpression;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public String getFromAffilies() {
        return fromAffilies;
    }

    public String getUntilAffilies() {
        return untilAffilies;
    }

    public boolean isAffilieTous() {
        return affilieTous;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setAffilieTous(boolean affilieTous) {
        this.affilieTous = affilieTous;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setFromAffilies(String fromAffilies) {
        this.fromAffilies = fromAffilies;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public void setUntilAffilies(String untilAffilies) {
        this.untilAffilies = untilAffilies;
    }
}
