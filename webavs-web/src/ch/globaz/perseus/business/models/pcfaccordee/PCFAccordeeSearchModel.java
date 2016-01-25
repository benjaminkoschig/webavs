/**
 * 
 */
package ch.globaz.perseus.business.models.pcfaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author DDE
 * 
 */
public class PCFAccordeeSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ORDER_BY_DATE_DECISION = "dateDecision";
    public static String SEARCH_EN_COURS = "enCours";
    public static String WITH_DATE_SEARCH = "withDate";
    public static String WITH_DECISION = "withDecision";

    private String forCsEtatPCFAccordee = null;
    private String forCsSexe = null;
    private String forDateDecision = null;
    private String forDateDiminution = null;
    private String forDateNaissance = null;
    private String forIdDemande = null;
    private String forIdDossier = null;
    private String forIdGestionnaire = null;
    private String forMoisDebut = null;
    private String forMoisEnCours = null;
    private String forMoisFin = null;
    private Boolean forOnError = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    /**
	 * 
	 */
    public PCFAccordeeSearchModel() {
        super();
    }

    /**
     * @return the forCsEtatPCFAccordee
     */
    public String getForCsEtatPCFAccordee() {
        return forCsEtatPCFAccordee;
    }

    /**
     * @return the forCsSexe
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    /**
     * @return the forDateDecision
     */
    public String getForDateDecision() {
        return forDateDecision;
    }

    /**
     * @return the forDateDiminution
     */
    public String getForDateDiminution() {
        return forDateDiminution;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
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

    /**
     * @return the forIdGestionnaire
     */
    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    /**
     * @return the forMoisDebut
     */
    public String getForMoisDebut() {
        return forMoisDebut;
    }

    /**
     * @return the forMoisEnCours
     */
    public String getForMoisEnCours() {
        return forMoisEnCours;
    }

    /**
     * @return the forMoisFin
     */
    public String getForMoisFin() {
        return forMoisFin;
    }

    /**
     * @return the forOnError
     */
    public Boolean getForOnError() {
        return forOnError;
    }

    /**
     * @return the likeNom
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return the likeNss
     */
    public String getLikeNss() {
        return likeNss;
    }

    /**
     * @return the likePrenom
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * @param forCsEtatPCFAccordee
     *            the forCsEtatPCFAccordee to set
     */
    public void setForCsEtatPCFAccordee(String forCsEtatPCFAccordee) {
        this.forCsEtatPCFAccordee = forCsEtatPCFAccordee;
    }

    /**
     * @param forCsSexe
     *            the forCsSexe to set
     */
    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    /**
     * @param forDateDecision
     *            the forDateDecision to set
     */
    public void setForDateDecision(String forDateDecision) {
        this.forDateDecision = forDateDecision;
    }

    /**
     * @param forDateDiminution
     *            the forDateDiminution to set
     */
    public void setForDateDiminution(String forDateDiminution) {
        this.forDateDiminution = forDateDiminution;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
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

    /**
     * @param forIdGestionnaire
     *            the forIdGestionnaire to set
     */
    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    /**
     * @param forMoisDebut
     *            the forMoisDebut to set
     */
    public void setForMoisDebut(String forMoisDebut) {
        if (!JadeStringUtil.isEmpty(forMoisDebut)) {
            setWhereKey(PCFAccordeeSearchModel.WITH_DATE_SEARCH);
            this.forMoisDebut = forMoisDebut;
        }
    }

    /**
     * @param forMoisEnCours
     *            the forMoisEnCours to set
     */
    public void setForMoisEnCours(String forMoisEnCours) {
        if (!JadeStringUtil.isEmpty(forMoisEnCours)) {
            setWhereKey(PCFAccordeeSearchModel.SEARCH_EN_COURS);
            this.forMoisEnCours = forMoisEnCours;
        }
    }

    /**
     * @param forMoisFin
     *            the forMoisFin to set
     */
    public void setForMoisFin(String forMoisFin) {
        if (!JadeStringUtil.isEmpty(forMoisDebut)) {
            setWhereKey(PCFAccordeeSearchModel.WITH_DATE_SEARCH);
            this.forMoisFin = forMoisFin;
        }
    }

    /**
     * @param forOnError
     *            the forOnError to set
     */
    public void setForOnError(Boolean forOnError) {
        this.forOnError = forOnError;
    }

    /**
     * @param likeNom
     *            the likeNom to set
     */
    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom != null ? JadeStringUtil.convertSpecialChars(likeNom).toUpperCase() : null;
    }

    /**
     * @param likeNss
     *            the likeNss to set
     */
    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    /**
     * @param likePrenom
     *            the likePrenom to set
     */
    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom != null ? JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase() : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return PCFAccordee.class;
    }

}
