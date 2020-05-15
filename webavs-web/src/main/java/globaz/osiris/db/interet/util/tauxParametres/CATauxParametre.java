package globaz.osiris.db.interet.util.tauxParametres;

import ch.globaz.common.domaine.Date;

public class CATauxParametre {

    String cleDiff;
    String csFamille;
    String libelle;
    Date dateDebut;
    Date dateFin;
    double taux;

    public CATauxParametre() { }


    public String getCsFamille() {
        return csFamille;
    }

    public void setCsFamille(String csFamille) {
        this.csFamille = csFamille;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

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

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }
    public String getCleDiff() {
        return cleDiff;
    }

    public void setCleDiff(String cleDiff) {
        this.cleDiff = cleDiff;
    }
}
