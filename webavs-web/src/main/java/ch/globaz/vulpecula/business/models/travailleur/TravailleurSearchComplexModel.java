package ch.globaz.vulpecula.business.models.travailleur;

import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author JPA
 * 
 */
public class TravailleurSearchComplexModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 8656900807497643999L;
    private String forDateNaissance;
    private String forIdTiers;
    private String forIdTravailleur;
    private String forNumAvs;
    private String likeNom;
    private String likePrenom;
    private Boolean annonceMeroba;
    private String forCorrelationId;

    private String sexeHomme = "516001";
    private String sexeFemme = "516002";

    private String personnePhysique = "1";

    public static final String WHERE_WITHSEXANDPERSON = "sexeObligatoireEtPersonnePhysique";

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public String getForNumAvs() {
        return forNumAvs;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setForDateNaissance(String forDateNaissance) {
        if (!JAUtil.isDateEmpty(forDateNaissance)) {
            this.forDateNaissance = forDateNaissance;
        }
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    public void setForNumAvs(String forNumAvs) {
        this.forNumAvs = forNumAvs;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = JadeStringUtil.convertSpecialChars(likeNom.toUpperCase());
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = JadeStringUtil.convertSpecialChars(likePrenom.toUpperCase());
    }

    public Boolean getAnnonceMeroba() {
        return annonceMeroba;
    }

    public void setAnnonceMeroba(Boolean annonceMeroba) {
        this.annonceMeroba = annonceMeroba;
    }

    public String getPersonnePhysique() {
        return personnePhysique;
    }

    public String getSexeHomme() {
        return sexeHomme;
    }

    public String getSexeFemme() {
        return sexeFemme;
    }

    public String getForCorrelationId() {
        return forCorrelationId;
    }

    public void setForCorrelationId(String forCorrelationId) {
        this.forCorrelationId = forCorrelationId;
    }

    @Override
    public Class<TravailleurComplexModel> whichModelClass() {
        return TravailleurComplexModel.class;
    }
}
