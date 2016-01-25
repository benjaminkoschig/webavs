/*
 * Créé le 15.09.2006
 */
package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * 
 * @author mmo
 */
public class APListePrestationVerseeViewBean extends PRAbstractViewBeanSupport {

    private String dateDebut = "";
    private String dateFin = "";
    private boolean displayGedCheckbox = false;
    private String eMailAddress = "";
    private Boolean envoyerGed = new Boolean(false);
    private String numeroAffilie = "";
    private String selecteurPrestation = "";

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
