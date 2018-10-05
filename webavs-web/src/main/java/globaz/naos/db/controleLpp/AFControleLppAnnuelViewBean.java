/*
 * Globaz SA
 */
package globaz.naos.db.controleLpp;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.AFAbstractViewBean;

/**
 * 
 * @author sco
 * @since 08 sept. 2011
 */
public class AFControleLppAnnuelViewBean extends AFAbstractViewBean {

    private String anneeDebut;
    private String anneeFin;
    private String email;
    private String filename;
    private String dateCreation = JACalendar.todayJJsMMsAAAA();

    // true = simulation
    private boolean modeControle = true;
    private String typeAdresse;

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public boolean isModeControleSimulation() {
        return modeControle;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setModeControle(boolean modeControle) {
        this.modeControle = modeControle;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        JACalendarGregorian calendar = new JACalendarGregorian();
        try {
            JADate nextWorkingDay = calendar.getNextWorkingDay(dateCreation);
            this.dateCreation = JACalendar.format(nextWorkingDay);
        } catch (JAException e) {
            
            JadeLogger
            .info("Erreur dans la date de creation Controle Annuel LPP = ", e);
        }
    }
}
