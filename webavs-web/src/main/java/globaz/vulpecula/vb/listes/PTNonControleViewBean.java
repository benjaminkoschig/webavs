package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.common.Date;

public class PTNonControleViewBean extends BJadeSearchObjectELViewBean {
    private String email;
    private String dateDebut;
    private String nombreAnnee;
    private Boolean uniquementAVS;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    public Boolean getUniquementAVS() {
        return uniquementAVS;
    }

    public void setUniquementAVS(Boolean uniquementAVS) {
        this.uniquementAVS = uniquementAVS;
    }

    /**
     * Méthode utilisée par le framework pour setter l'email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Méthode utilisée par le framework pour setter l'id de la convention.
     * 
     * @return email auquel le mail sera envoyé
     */

    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    public String getMessageProcessusLance() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LISTE_PROCESSUS_LANCE");
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateDefaut() {
        Date date = Date.now();
        date = date.addYear(-1);
        return Date.getLastDayOfYear(date.getYear()).toString();
    }

    public String getNombreAnnee() {
        return nombreAnnee;
    }

    public void setNombreAnnee(String nombreAnnee) {
        this.nombreAnnee = nombreAnnee;
    }

    public String getNombreAnneeDefaut() {
        return "4";
    }

}
