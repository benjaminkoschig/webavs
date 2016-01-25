/*
 * Créé le 06 avril 2010
 */
package globaz.cygnus.vb.motifsDeRefus;

import globaz.jade.client.util.JadeStringUtil;

/**
 * author : fha
 * 
 * création d'un objet composé de 3 strings : fournisseur, type de soin et sous type de soin utilisé plus tard pour
 * afficher le tableau Fournisseur-Type qui est une liste d'objets RFFournisseurType
 */
public class RFSoinMotif {

    private String descriptionLongueMotifRefus = "";

    private String descriptionLongueMotifRefusDE = "";

    private String descriptionLongueMotifRefusIT = "";

    private String descriptionMotifRefus = "";

    private String descriptionMotifRefusDE = "";

    private String descriptionMotifRefusIT = "";

    private String idMotifRefus = "";

    /* fields */
    private Integer idSoinMotif = null;

    private String idSousTypeDeSoin = "";

    private Boolean isChargeDepuisDB = Boolean.FALSE;

    private String sousTypeDeSoin = "";

    private Boolean supprimer = Boolean.FALSE;

    private String typeDeSoin = "";

    /* constructor */
    public RFSoinMotif(Integer _idSoinMotif, String _idMotifRefus, String _descriptionMotifRefus,
            String _descriptionMotifRefusIT, String _descriptionMotifRefusDE, String _descriptionLongueMotifRefus,
            String _descriptionLongueMotifRefusIT, String _descriptionLongueMotifRefusDE, String _typeDeSoin,
            String _sousTypeDeSoin, String _idSousTypeDeSoin, Boolean _isChargeDepuisDB, Boolean _supprimer) {
        idSoinMotif = _idSoinMotif;
        descriptionMotifRefus = _descriptionMotifRefus;
        descriptionMotifRefusIT = _descriptionMotifRefusIT;
        descriptionMotifRefusDE = _descriptionMotifRefusDE;

        descriptionLongueMotifRefus = _descriptionLongueMotifRefus;
        descriptionLongueMotifRefusIT = _descriptionLongueMotifRefusIT;
        descriptionLongueMotifRefusDE = _descriptionLongueMotifRefusDE;

        typeDeSoin = _typeDeSoin;
        sousTypeDeSoin = _sousTypeDeSoin;
        idMotifRefus = _idMotifRefus;
        idSousTypeDeSoin = _idSousTypeDeSoin;
        isChargeDepuisDB = _isChargeDepuisDB;
        supprimer = _supprimer;
    }

    @Override
    public boolean equals(Object obj) {

        RFSoinMotif other = (RFSoinMotif) obj;

        if (!typeDeSoin.equals(other.typeDeSoin)) {
            return false;
        }
        if (!sousTypeDeSoin.equals(other.sousTypeDeSoin)) {
            return false;
        }
        if (!descriptionMotifRefus.equals(other.descriptionMotifRefus)) {
            return false;
        }
        if (!descriptionMotifRefusIT.equals(other.descriptionMotifRefusIT)) {
            return false;
        }
        if (!descriptionMotifRefusDE.equals(other.descriptionMotifRefusDE)) {
            return false;
        }
        if (!descriptionLongueMotifRefus.equals(other.descriptionLongueMotifRefus)) {
            return false;
        }
        if (!descriptionLongueMotifRefusIT.equals(other.descriptionLongueMotifRefusIT)) {
            return false;
        }
        if (!descriptionLongueMotifRefusDE.equals(other.descriptionLongueMotifRefusDE)) {
            return false;
        }
        if (!idMotifRefus.equals(other.idMotifRefus)) {
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
     * @return vrai si le RFSoinMotif a tous ces champs remplis : id, description, type et sous type de soin
     */
    public boolean estValide() {
        return !(JadeStringUtil.isEmpty(descriptionMotifRefus) || JadeStringUtil.isEmpty(descriptionMotifRefusIT)
                || JadeStringUtil.isEmpty(descriptionMotifRefusDE) || JadeStringUtil.isEmpty(typeDeSoin));
    }

    public String getDescriptionLongueMotifRefus() {
        return descriptionLongueMotifRefus;
    }

    /* methods */

    public String getDescriptionLongueMotifRefusDE() {
        return descriptionLongueMotifRefusDE;
    }

    public String getDescriptionLongueMotifRefusIT() {
        return descriptionLongueMotifRefusIT;
    }

    public String getDescriptionMotifRefus() {
        return descriptionMotifRefus;
    }

    public String getDescriptionMotifRefusDE() {
        return descriptionMotifRefusDE;
    }

    public String getDescriptionMotifRefusIT() {
        return descriptionMotifRefusIT;
    }

    public String getIdMotifRefus() {
        return idMotifRefus;
    }

    public Integer getIdSoinMotif() {
        return idSoinMotif;
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
        result = prime * result + ((idSoinMotif == null) ? 0 : idSoinMotif.hashCode());
        result = prime * result + ((descriptionMotifRefus == null) ? 0 : descriptionMotifRefus.hashCode());
        result = prime * result + ((descriptionMotifRefusIT == null) ? 0 : descriptionMotifRefusIT.hashCode());
        result = prime * result + ((descriptionMotifRefusDE == null) ? 0 : descriptionMotifRefusDE.hashCode());
        result = prime * result + ((descriptionLongueMotifRefus == null) ? 0 : descriptionLongueMotifRefus.hashCode());
        result = prime * result
                + ((descriptionLongueMotifRefusIT == null) ? 0 : descriptionLongueMotifRefusIT.hashCode());
        result = prime * result
                + ((descriptionLongueMotifRefusDE == null) ? 0 : descriptionLongueMotifRefusDE.hashCode());

        result = prime * result + ((idMotifRefus == null) ? 0 : idMotifRefus.hashCode());
        result = prime * result + ((idSousTypeDeSoin == null) ? 0 : idSousTypeDeSoin.hashCode());
        result = prime * result + ((sousTypeDeSoin == null) ? 0 : sousTypeDeSoin.hashCode());
        result = prime * result + ((typeDeSoin == null) ? 0 : typeDeSoin.hashCode());
        result = prime * result + ((isChargeDepuisDB == null) ? 0 : isChargeDepuisDB.hashCode());
        result = prime * result + ((supprimer == null) ? 0 : supprimer.hashCode());
        return result;
    }

    public void setDescriptionLongueMotifRefus(String descriptionLongueMotifRefus) {
        this.descriptionLongueMotifRefus = descriptionLongueMotifRefus;
    }

    public void setDescriptionLongueMotifRefusDE(String descriptionLongueMotifRefusDE) {
        this.descriptionLongueMotifRefusDE = descriptionLongueMotifRefusDE;
    }

    public void setDescriptionLongueMotifRefusIT(String descriptionLongueMotifRefusIT) {
        this.descriptionLongueMotifRefusIT = descriptionLongueMotifRefusIT;
    }

    public void setDescriptionMotifRefus(String descriptionMotifRefus) {
        this.descriptionMotifRefus = descriptionMotifRefus;
    }

    public void setDescriptionMotifRefusDE(String descriptionMotifRefusDE) {
        this.descriptionMotifRefusDE = descriptionMotifRefusDE;
    }

    public void setDescriptionMotifRefusIT(String descriptionMotifRefusIT) {
        this.descriptionMotifRefusIT = descriptionMotifRefusIT;
    }

    public void setIdMotifRefus(String idMotifRefus) {
        this.idMotifRefus = idMotifRefus;
    }

    public void setIdSoinMotif(Integer idSoinMotif) {
        this.idSoinMotif = idSoinMotif;
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
