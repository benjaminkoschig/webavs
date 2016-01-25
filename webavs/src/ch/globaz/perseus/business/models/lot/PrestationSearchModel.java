package ch.globaz.perseus.business.models.lot;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author MBO
 * 
 */

public class PrestationSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ORDER_BY_DATE_COMPTABILIASTION_LOT_ASC = "dateComptabilisationASC";
    public static String ORDER_BY_DATE_FIN_AND_DATE_VALIDATION_AND_NUM_DECISION_DESC = "dateDebutAndDateValidationAndNumDecisionDesc";
    public static String WITH_DATE_SEARCH = "withDate";
    public static String WITH_IDGESTIONNAIRE_SEARCH = "withIdGestionnaire";
    private String betweenDateComptabilisationDebut = null;
    private String betweenDateComptabilisationFin = null;
    private String forCsTypeLot = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forDateNaissance = null;
    private String forEtatLot = null;
    private String forIdDemande = null;
    private String forIdDossier = null;
    private String forIdFacture = null;
    private List<String> forIdGestionnaireFactureIn = null;
    private List<String> forIdGestionnaireFactureNotIn = null;
    private String forIdLot = null;
    private String forSexe = null;
    private List<String> inTypeLot = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;
    private Class modelClass = null;

    public PrestationSearchModel() {
        super();
        inTypeLot = new ArrayList<String>();
        forIdGestionnaireFactureIn = new ArrayList<String>();
        forIdGestionnaireFactureNotIn = new ArrayList<String>();
    }

    public String getBetweenDateComptabilisationDebut() {
        return betweenDateComptabilisationDebut;
    }

    public String getBetweenDateComptabilisationFin() {
        return betweenDateComptabilisationFin;
    }

    public String getForCsTypeLot() {
        return forCsTypeLot;
    }

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForEtatLot() {
        return forEtatLot;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdFacture() {
        return forIdFacture;
    }

    public List<String> getForIdGestionnaireFactureIn() {
        return forIdGestionnaireFactureIn;
    }

    public List<String> getForIdGestionnaireFactureNotIn() {
        return forIdGestionnaireFactureNotIn;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForSexe() {
        return forSexe;
    }

    /**
     * @return the inTypeLot
     */
    public List<String> getInTypeLot() {
        return inTypeLot;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNss() {
        return likeNss;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setBetweenDateComptabilisationDebut(String betweenDateComptabilisationDebut) {
        this.betweenDateComptabilisationDebut = betweenDateComptabilisationDebut;
    }

    public void setBetweenDateComptabilisationFin(String betweenDateComptabilisationFin) {
        this.betweenDateComptabilisationFin = betweenDateComptabilisationFin;
    }

    public void setForCsTypeLot(String forCsTypeLot) {
        this.forCsTypeLot = forCsTypeLot;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        if (!JadeStringUtil.isEmpty(forDateDebut)) {
            setWhereKey(PrestationSearchModel.WITH_DATE_SEARCH);
            this.forDateDebut = forDateDebut;
        }
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        if (!JadeStringUtil.isEmpty(forDateDebut)) {
            setWhereKey(PrestationSearchModel.WITH_DATE_SEARCH);
            this.forDateFin = forDateFin;
        }
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForEtatLot(String forEtatLot) {
        this.forEtatLot = forEtatLot;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdFacture(String forIdFacture) {
        this.forIdFacture = forIdFacture;
    }

    public void setForIdGestionnaireFactureIn(List<String> forIdGestionnaire) {
        forIdGestionnaireFactureIn = forIdGestionnaire;
    }

    public void setForIdGestionnaireFactureNotIn(List<String> forNotIdGestionnaire) {
        forIdGestionnaireFactureNotIn = forNotIdGestionnaire;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForSexe(String forSexe) {
        this.forSexe = forSexe;
    }

    /**
     * @param inTypeLot
     *            the inTypeLot to set
     */
    public void setInTypeLot(List<String> inTypeLot) {
        this.inTypeLot = inTypeLot;
    }

    /**
     * définit la condition de recherche sur le nom de l'assuré
     * 
     * @param likeNom
     *            nom de l'assuré
     */
    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom != null ? JadeStringUtil.convertSpecialChars(likeNom).toUpperCase() : null;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    /**
     * définit la condition de recherche sur le prénom de l'assuré
     * 
     * @param likePrenom
     *            prénom de l'assuré
     */
    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom != null ? JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase() : null;
    }

    /**
     * @param modelClass
     *            the modelClass to set
     */
    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }

    @Override
    public Class whichModelClass() {
        if (modelClass == null) {
            return Prestation.class;
        } else {
            return modelClass;
        }
    };

}
