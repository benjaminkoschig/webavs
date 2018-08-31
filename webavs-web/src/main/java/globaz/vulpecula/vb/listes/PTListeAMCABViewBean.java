package globaz.vulpecula.vb.listes;

import globaz.jade.context.JadeThread;
import ch.globaz.common.domaine.Date;

public class PTListeAMCABViewBean extends PTListeProcessViewBean {

    private String email;
    private String dateFrom = initialisationDateDebut();
    private String dateTo = initialisationDateFin();

    @Override
    public String getEmail() {
        if (email == null) {
            return JadeThread.currentUserEmail();
        }
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * Permet d'initaliser les dates de début.
     * 
     * Si le mois de la date actuelle > 1 --> on prend l'année pour la date de début sinon l'année en cours -1.
     */
    private String initialisationDateDebut() {
        Date date = new Date();
        int mois = Integer.parseInt(date.getMois());
        int annee = Integer.parseInt(date.getAnnee());

        if (mois > 1) {
            return "01.02." + annee;
        } else {
            return "01.02." + (annee - 1);
        }
    }

    /**
     * Permet d'initaliser les dates de fin.
     * 
     * Si le mois de la date actuelle > 1 --> on prend l'année +1 pour la date de fin sinon l'année en cours.
     */
    private String initialisationDateFin() {
        Date date = new Date();
        int mois = Integer.parseInt(date.getMois());
        int annee = Integer.parseInt(date.getAnnee());

        if (mois > 1) {
            return "31.01." + (annee + 1);
        } else {
            return "31.01." + annee;
        }

    }
}
