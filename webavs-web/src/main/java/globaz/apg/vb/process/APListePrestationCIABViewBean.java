/*
 * Cr?? le 29.03.2019
 */
package globaz.apg.vb.process;

import globaz.apg.process.APListePrestationCIABProcess;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * 
 * @author eniv
 */
public class APListePrestationCIABViewBean extends PRAbstractViewBeanSupport {

    private String dateDebut = "";
    private String dateFin = "";
    private boolean displayGedCheckbox = false;
    private String eMailAddress = "";
    private Boolean envoyerGed = new Boolean(false);
    private String numeroAffilie = "";
    private String selecteurPrestation = APListePrestationCIABProcess.SELECTEUR_PRESTATION_PAR_PAIEMENT;

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getSelecteurPrestation() {
        return selecteurPrestation;
    }

    public boolean isDisplayGedCheckbox() {
        return displayGedCheckbox;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDisplayGedCheckbox(boolean displayGedCheckbox) {
        this.displayGedCheckbox = displayGedCheckbox;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setEnvoyerGed(Boolean envoyerGed) {
        this.envoyerGed = envoyerGed;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setSelecteurPrestation(String selecteurPrestation) {
        this.selecteurPrestation = selecteurPrestation;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
