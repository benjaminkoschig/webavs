/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.vb.conventions;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * author fha
 */
public class RFAssureConvention {
    private String adresse = "";

    private String dateDebut = "";

    private String dateFin = "";

    /* fields */
    private String idAssure = "";

    private Boolean isChargeDepuisDB = Boolean.FALSE;

    private String montant = "";

    private String nom = "";

    private String NSS = "";

    private String prenom = "";

    private Boolean supprimer = Boolean.FALSE;

    /* constructor */
    public RFAssureConvention(String _idAssure, String _montant, String _NSS, String _nom, String _prenom,
            String _adresse, String _dateDebut, String _dateFin, Boolean _isChargeDepuisDB, Boolean _supprimer) {
        idAssure = _idAssure;
        montant = _montant;
        NSS = _NSS;
        nom = _nom;
        prenom = _prenom;
        adresse = _adresse;
        dateDebut = _dateDebut;
        dateFin = _dateFin;
        isChargeDepuisDB = _isChargeDepuisDB;
        supprimer = _supprimer;
    }

    @Override
    public boolean equals(Object obj) {
        RFAssureConvention other = (RFAssureConvention) obj;

        if (!montant.equals(other.montant)) {
            return false;
        }
        if (!NSS.equals(other.NSS)) {
            return false;
        }
        if (!nom.equals(other.nom)) {
            return false;
        }
        if (!prenom.equals(other.prenom)) {
            return false;
        }
        if (!adresse.equals(other.adresse)) {
            return false;
        }
        if (!dateDebut.equals(other.dateDebut)) {
            return false;
        }
        if (!dateFin.equals(other.dateFin)) {
            return false;
        }
        if (!isChargeDepuisDB.equals(other.isChargeDepuisDB)) {
            return false;
        }
        if (!supprimer.equals(other.supprimer)) {
            return false;
        }
        return true;
    }

    /**
     * @return vrai si le RFAssureConvention a un NSS, une date de début et de fin (avec date de fin <= date de début)
     */
    public boolean estValide() {
        return !(JadeStringUtil.isEmpty(NSS) || JadeStringUtil.isEmpty(dateDebut) || JadeStringUtil.isEmpty(dateFin))
                && (JadeDateUtil.isDateAfter(dateFin, dateDebut) || JadeDateUtil.areDatesEquals(dateFin, dateDebut));
    }

    public String getAdresse() {
        return adresse;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdAssure() {
        return idAssure;
    }

    public Boolean getIsChargeDepuisDB() {
        return isChargeDepuisDB;
    }

    public String getMontant() {
        return montant;
    }

    public String getNom() {
        return nom;
    }

    /* getters - setters */
    public String getNSS() {
        return NSS;
    }

    public String getPrenom() {
        return prenom;
    }

    public Boolean getSupprimer() {
        return supprimer;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((montant == null) ? 0 : montant.hashCode());
        result = prime * result + ((NSS == null) ? 0 : NSS.hashCode());
        result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
        result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
        result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
        result = prime * result + ((idAssure == null) ? 0 : idAssure.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
        result = prime * result + ((isChargeDepuisDB == null) ? 0 : isChargeDepuisDB.hashCode());
        result = prime * result + ((supprimer == null) ? 0 : supprimer.hashCode());
        return result;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdAssure(String idAssure) {
        this.idAssure = idAssure;
    }

    public void setIsChargeDepuisDB(Boolean isChargeDepuisDB) {
        this.isChargeDepuisDB = isChargeDepuisDB;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNSS(String nSS) {
        NSS = nSS;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSupprimer(Boolean supprimer) {
        this.supprimer = supprimer;
    }

}
