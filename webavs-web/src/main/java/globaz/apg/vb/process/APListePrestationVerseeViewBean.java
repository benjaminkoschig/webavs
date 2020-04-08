/*
 * Créé le 15.09.2006
 */
package globaz.apg.vb.process;

import globaz.prestation.vb.PRAbstractViewBeanSupport;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> messagesError = new ArrayList<>();
    private boolean hasMessagePropError = false;

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


    public void setMessagePropError(boolean b) {
        hasMessagePropError = b;
    }
    public boolean hasMessagePropError() {
        return hasMessagePropError;
    }

    public String getMessagesError() {
        String msgHTML = "";
        for(String msg: messagesError){
            msgHTML = msgHTML+ "<p>"+msg+"<p><br>";
        }
        return msgHTML;
    }

    public void setMessagesError(List<String> messagesError) {
        this.messagesError = messagesError;
    }
}
