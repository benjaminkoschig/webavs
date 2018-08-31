package ch.globaz.vulpecula.domain.repositories.decompte;

import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;

/***
 * Le poste de travail correspond � la relation entre un travailleur et un
 * employeur.
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 17 d�c. 2013
 */
public class PrestationsAFPourRevision {

    private Date dateDebut = null;
    private Date dateFin = null;
    private Montant montant = Montant.ZERO;
    private String dateVersement = "";

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Montant getMontant() {
        return montant;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public String getDateVersement() {
        return dateVersement;
    }

    public void setDateVersement(String dateVersement) {
        this.dateVersement = dateVersement;
    }
}
