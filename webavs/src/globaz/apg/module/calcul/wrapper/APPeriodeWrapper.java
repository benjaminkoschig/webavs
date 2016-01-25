package globaz.apg.module.calcul.wrapper;

import globaz.globall.util.JADate;

/**
 * Descpription
 * 
 * @author scr Date de création 18 mai 05
 */
public class APPeriodeWrapper {

    JADate dateDebut = null;
    JADate dateFin = null;

    public APPeriodeWrapper() {
        super();
    }

    public APPeriodeWrapper(JADate dateDebut, JADate dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public boolean equals(APPeriodeWrapper pw) {
        if ((getDateDebut().toAMJ().compareTo(pw.getDateDebut().toAMJ()) == 0)
                && (getDateFin().toAMJ().compareTo(pw.getDateFin().toAMJ()) == 0)) {

            return true;
        } else {
            return false;
        }
    }

    public JADate getDateDebut() {
        return dateDebut;
    }

    public JADate getDateFin() {
        return dateFin;
    }

    public void setDateDebut(JADate date) {
        dateDebut = date;
    }

    public void setDateFin(JADate date) {
        dateFin = date;
    }
}
