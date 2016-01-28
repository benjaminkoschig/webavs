package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPCotisationListViewBean extends CPCotisationManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private java.lang.String totalAnnuel = "";
    private java.lang.String totalMensuel = "";
    private java.lang.String totalSemestriel = "";
    private java.lang.String totalTrimestriel = "";

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Retourne la date de début Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getDebutCotisation(int pos) {
        return ((CPCotisation) getEntity(pos)).getDebutCotisation();
    }

    /**
     * Retourne la date de fin Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getFinCotisation(int pos) {
        return ((CPCotisation) getEntity(pos)).getFinCotisation();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdCotisation(int pos) {
        return ((CPCotisation) getEntity(pos)).getIdCotisation();
    }

    /**
     * Retourne le libéllé court de la cotisation Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleAssurance(int pos) {
        String libelleAssurance = "";
        try {
            CPCotisation cotisationCp = (CPCotisation) getEntity(pos);
            if (cotisationCp != null && !cotisationCp.isNew()) {
                AFCotisation cotisationAf = new AFCotisation();
                cotisationAf.setSession(getSession());
                String idCotiAffiliation = cotisationCp.getIdCotiAffiliation();
                if (!JadeStringUtil.isIntegerEmpty(idCotiAffiliation)) {
                    cotisationAf.setCotisationId(idCotiAffiliation);
                    cotisationAf.retrieve();
                    if (cotisationAf != null && !cotisationAf.isNew()) {
                        AFAssurance assurance = cotisationAf.getAssurance();
                        if (assurance != null && !assurance.isNew()) {
                            libelleAssurance = assurance.getAssuranceLibelleCourt();
                        }
                    }
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return libelleAssurance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMontantAnnuel(int pos) {
        float cumul = 0;
        float montant = 0;
        String montantAnnuel = JANumberFormatter.deQuote(((CPCotisation) getEntity(pos)).getMontantAnnuel());
        String total = JANumberFormatter.deQuote(getTotalAnnuel());
        if (!JadeStringUtil.isDecimalEmpty(montantAnnuel)) {
            montant = Float.parseFloat(montantAnnuel);
        }
        if (!JadeStringUtil.isDecimalEmpty(total)) {
            cumul = Float.parseFloat(total);
        }
        cumul = new FWCurrency(cumul + montant).floatValue();
        setTotalAnnuel(Float.toString(cumul));
        return ((CPCotisation) getEntity(pos)).getMontantAnnuel();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMontantMensuel(int pos) {
        float cumul = 0;
        float montant = 0;
        String montantMensuel = JANumberFormatter.deQuote(((CPCotisation) getEntity(pos)).getMontantMensuel());
        String total = JANumberFormatter.deQuote(getTotalMensuel());
        if (!JadeStringUtil.isDecimalEmpty(montantMensuel)) {
            montant = Float.parseFloat(montantMensuel);
        }
        if (!JadeStringUtil.isDecimalEmpty(total)) {
            cumul = Float.parseFloat(total);
        }
        cumul = new FWCurrency(cumul + montant).floatValue();
        setTotalMensuel(Float.toString(cumul));
        return ((CPCotisation) getEntity(pos)).getMontantMensuel();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMontantSemestriel(int pos) {
        float cumul = 0;
        float montant = 0;
        String montantSem = JANumberFormatter.deQuote(((CPCotisation) getEntity(pos)).getMontantSemestriel());
        String total = JANumberFormatter.deQuote(getTotalSemestriel());
        if (!JadeStringUtil.isDecimalEmpty(montantSem)) {
            montant = Float.parseFloat(montantSem);
        }
        if (!JadeStringUtil.isDecimalEmpty(total)) {
            cumul = Float.parseFloat(total);
        }
        cumul = new FWCurrency(cumul + montant).floatValue();
        setTotalSemestriel(Float.toString(cumul));
        return ((CPCotisation) getEntity(pos)).getMontantSemestriel();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMontantTrimestriel(int pos) {
        float cumul = 0;
        float montant = 0;
        String montantTrim = JANumberFormatter.deQuote(((CPCotisation) getEntity(pos)).getMontantTrimestriel());
        String total = JANumberFormatter.deQuote(getTotalTrimestriel());
        if (!JadeStringUtil.isDecimalEmpty(montantTrim)) {
            montant = Float.parseFloat(montantTrim);
        }
        if (!JadeStringUtil.isDecimalEmpty(total)) {
            cumul = Float.parseFloat(total);
        }
        cumul = new FWCurrency(cumul + montant).floatValue();
        setTotalTrimestriel(Float.toString(cumul));
        return ((CPCotisation) getEntity(pos)).getMontantTrimestriel();
    }

    /**
     * Retourne la périodicité Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getPeriodicite(int pos) {
        String periodicite = "";
        try {
            CPCotisation cotisation = (CPCotisation) getEntity(pos);
            if (cotisation != null && !cotisation.isNew()) {
                periodicite = cotisation.getPeriodicite();
                if (!JadeStringUtil.isIntegerEmpty(periodicite)) {
                    periodicite = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), periodicite);
                }
            }
        } catch (Exception e) {
            periodicite = "";
            getSession().addError(e.getMessage());
        }
        return periodicite;
    }

    /**
     * Retourne le taux d'une cotisation Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTaux(int pos) {
        return ((CPCotisation) getEntity(pos)).getTaux();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 14:24:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTotalAnnuel() {
        return JANumberFormatter.fmt(totalAnnuel, true, true, true, 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 14:25:17)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTotalMensuel() {
        return JANumberFormatter.fmt(totalMensuel, true, true, true, 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 14:24:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTotalSemestriel() {
        return JANumberFormatter.fmt(totalSemestriel, true, true, true, 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 14:25:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTotalTrimestriel() {
        return JANumberFormatter.fmt(totalTrimestriel, true, true, true, 2);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 14:24:21)
     * 
     * @param newTotalAnnuel
     *            java.lang.String
     */
    public void setTotalAnnuel(java.lang.String newTotalAnnuel) {
        totalAnnuel = JANumberFormatter.deQuote(newTotalAnnuel);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 14:25:17)
     * 
     * @param newTotalMensuel
     *            java.lang.String
     */
    public void setTotalMensuel(java.lang.String newTotalMensuel) {
        totalMensuel = JANumberFormatter.deQuote(newTotalMensuel);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 14:24:43)
     * 
     * @param newTotalSemestriel
     *            java.lang.String
     */
    public void setTotalSemestriel(java.lang.String newTotalSemestriel) {
        totalSemestriel = JANumberFormatter.deQuote(newTotalSemestriel);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.04.2003 14:25:03)
     * 
     * @param newTotalTrimestriel
     *            java.lang.String
     */
    public void setTotalTrimestriel(java.lang.String newTotalTrimestriel) {
        totalTrimestriel = JANumberFormatter.deQuote(newTotalTrimestriel);
    }
}
