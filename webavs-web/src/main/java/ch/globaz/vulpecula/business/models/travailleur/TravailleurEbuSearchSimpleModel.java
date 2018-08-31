/**
 *
 */
package ch.globaz.vulpecula.business.models.travailleur;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import ch.globaz.vulpecula.business.models.ebusiness.TravailleurEbuSimpleModel;

public class TravailleurEbuSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = -7353723917486081635L;

    private String forId;
    private String likeNom;
    private String likePrenom;
    private String forNumAvs;
    private String forDateNaissance;
    private String forIdEmployeur;
    private String forCorrelationId;
    private String forPosteCorrelationId;
    private String forConvention;
    private String forStatus;
    public static final String WHERE_KEY_SANS_QUITTANCE = "sansQuittance";

    public String getForCorrelationId() {
        return forCorrelationId;
    }

    public void setForCorrelationId(String forCorrelationId) {
        this.forCorrelationId = forCorrelationId;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public String getForNumAvs() {
        return forNumAvs;
    }

    public void setForNumAvs(String forNumAvs) {
        this.forNumAvs = forNumAvs;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public String getForPosteCorrelationId() {
        return forPosteCorrelationId;
    }

    public void setForPosteCorrelationId(String forPosteCorrelationId) {
        this.forPosteCorrelationId = forPosteCorrelationId;
    }

    public String getForStatus() {
        return forStatus;
    }

    public void setForStatus(String forStatus) {
        this.forStatus = forStatus;
    }

    /**
     * @return the forIdEmployeur
     */
    public String getForIdEmployeur() {
        return forIdEmployeur;
    }

    /**
     * @param forIdEmployeur the forIdEmployeur to set
     */
    public void setForIdEmployeur(String forIdEmployeur) {
        this.forIdEmployeur = forIdEmployeur;
    }

    public String getForConvention() {
        return forConvention;
    }

    public void setForConvention(String forConvention) {
        this.forConvention = forConvention;
    }

    @Override
    public Class<TravailleurEbuSimpleModel> whichModelClass() {
        return TravailleurEbuSimpleModel.class;
    }
}
