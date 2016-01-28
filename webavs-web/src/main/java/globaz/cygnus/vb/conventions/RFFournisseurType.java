/*
 * Créé le 06 avril 2010
 */
package globaz.cygnus.vb.conventions;

import globaz.jade.client.util.JadeStringUtil;

/**
 * author : fha
 * 
 * création d'un objet composé de 3 strings : fournisseur, type de soin et sous type de soin utilisé plus tard pour
 * afficher le tableau Fournisseur-Type qui est une liste d'objets RFFournisseurType
 */
public class RFFournisseurType {

    private String fournisseur = "";

    private String idAdressePaiement = "";

    private String idFournisseur = "";

    /* fields */
    private Integer idFourType = null;

    private String idSousTypeDeSoin = "";

    private Boolean isChargeDepuisDB = Boolean.FALSE;

    private String sousTypeDeSoin = "";

    private Boolean supprimer = Boolean.FALSE;

    private String typeDeSoin = "";

    /* constructor */
    public RFFournisseurType(Integer _idFourType, String _fournisseur, String _idAdressePaiement, String _typeDeSoin,
            String _sousTypeDeSoin, String _idFournisseur, String _idSousTypeDeSoin, Boolean _isChargeDepuisDB,
            Boolean _supprimer) {
        idFourType = _idFourType;
        fournisseur = _fournisseur;
        idAdressePaiement = _idAdressePaiement;
        typeDeSoin = _typeDeSoin;
        sousTypeDeSoin = _sousTypeDeSoin;
        idFournisseur = _idFournisseur;
        idSousTypeDeSoin = _idSousTypeDeSoin;
        isChargeDepuisDB = _isChargeDepuisDB;
        supprimer = _supprimer;
    }

    @Override
    public boolean equals(Object obj) {

        RFFournisseurType other = (RFFournisseurType) obj;

        if (!fournisseur.equals(other.fournisseur)) {
            return false;
        }
        if (!typeDeSoin.equals(other.typeDeSoin)) {
            return false;
        }
        if (!sousTypeDeSoin.equals(other.sousTypeDeSoin)) {
            return false;
        }
        if (!idFournisseur.equals(other.idFournisseur)) {
            return false;
        }
        if (!idAdressePaiement.equals(other.idAdressePaiement)) {
            return false;
        }
        if (!idSousTypeDeSoin.equals(other.idSousTypeDeSoin)) {
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
     * @return vrai si le RFFournisseurType a tous ces champs remplis
     */
    public boolean estValide() {
        return !(JadeStringUtil.isEmpty(fournisseur) || JadeStringUtil.isEmpty(typeDeSoin)
                || JadeStringUtil.isEmpty(sousTypeDeSoin) || JadeStringUtil.isEmpty(idFournisseur));

    }

    public String getFournisseur() {
        return fournisseur;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    /* methods */
    public Integer getIdFourType() {
        return idFourType;
    }

    public String getIdSousTypeDeSoin() {
        return idSousTypeDeSoin;
    }

    public Boolean getIsChargeDepuisDB() {
        return isChargeDepuisDB;
    }

    public String getSousTypeDeSoin() {
        return sousTypeDeSoin;
    }

    public Boolean getSupprimer() {
        return supprimer;
    }

    public String getTypeDeSoin() {
        return typeDeSoin;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fournisseur == null) ? 0 : fournisseur.hashCode());
        result = prime * result + ((idFourType == null) ? 0 : idFourType.hashCode());
        result = prime * result + ((idFournisseur == null) ? 0 : idFournisseur.hashCode());
        result = prime * result + ((idAdressePaiement == null) ? 0 : idAdressePaiement.hashCode());
        result = prime * result + ((idSousTypeDeSoin == null) ? 0 : idSousTypeDeSoin.hashCode());
        result = prime * result + ((sousTypeDeSoin == null) ? 0 : sousTypeDeSoin.hashCode());
        result = prime * result + ((typeDeSoin == null) ? 0 : typeDeSoin.hashCode());
        result = prime * result + ((isChargeDepuisDB == null) ? 0 : isChargeDepuisDB.hashCode());
        result = prime * result + ((supprimer == null) ? 0 : supprimer.hashCode());
        return result;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdFourType(Integer idFourType) {
        this.idFourType = idFourType;
    }

    public void setIdSousTypeDeSoin(String idSousTypeDeSoin) {
        this.idSousTypeDeSoin = idSousTypeDeSoin;
    }

    public void setIsChargeDepuisDB(Boolean isChargeDepuisDB) {
        this.isChargeDepuisDB = isChargeDepuisDB;
    }

    public void setSousTypeDeSoin(String sousTypeDeSoin) {
        this.sousTypeDeSoin = sousTypeDeSoin;
    }

    public void setSupprimer(Boolean supprimer) {
        this.supprimer = supprimer;
    }

    public void setTypeDeSoin(String typeDeSoin) {
        this.typeDeSoin = typeDeSoin;
    }

}
