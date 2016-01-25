package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

public class RentePontSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ORDER_BY_DATE_DEBUT_ASC_NOM_ASC = "dateDebutAscNomAsc";
    public final static String ORDER_BY_DATE_DEBUT_DESC = "dateFinDesc";
    public final static String ORDER_BY_DATE_DECISION = "dateTimeDecision";
    public final static String ORDERBY_DATE_FIN_DESC = "dateFin";
    public final static String SEARCH_EN_COURS = "enCours";
    public final static String WITH_ANNEE_VALABLE = "withAnneeValable";
    public final static String WITH_DATE_VALABLE = "withDateValable";
    public final static String WITHOUT_DATE_FIN = "WithOutDateFin";
    private String afterDateFin = null;
    private String beforeDateDebut = null;
    private String dateDebut = null;
    private String forCsCaisse = null;
    private String forCsEtat = null;
    private String forCsSexe = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forDateNaissance = null;
    private String forDateValable = null;
    private String forIdDossier = null;
    private String forIdGestionnaire = null;
    private String forIdSituationFamiliale = null;
    private String forMoisEnCours = null;
    private String forMoisPmtMensuel = null;
    private Boolean forOnError = null;
    private String likeNom = null;
    private String likeNss = null;

    private String likePrenom = null;

    public String getAfterDateFin() {
        return afterDateFin;
    }

    public String getBeforeDateDebut() {
        return beforeDateDebut;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the forCsCaisse
     */
    public String getForCsCaisse() {
        return forCsCaisse;
    }

    /**
     * @return the forCsEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * @return the forCsSexe
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
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
     * @return the forIdSituationFamiliale
     */
    public String getForIdSituationFamiliale() {
        return forIdSituationFamiliale;
    }

    /**
     * @return the forMoisEnCours
     */
    public String getForMoisEnCours() {
        return forMoisEnCours;
    }

    public String getForMoisPmtMensuel() {
        return forMoisPmtMensuel;
    }

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

    public void setAfterDateFin(String afterDateFin) {
        this.afterDateFin = afterDateFin;
    }

    public void setBeforeDateDebut(String beforeDateDebut) {
        this.beforeDateDebut = beforeDateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param forCsCaisse
     *            the forCsCaisse to set
     */
    public void setForCsCaisse(String forCsCaisse) {
        this.forCsCaisse = forCsCaisse;
    }

    /**
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    /**
     * @param forCsSexe
     *            the forCsSexe to set
     */
    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forDateValable
     *            the forDateValable to set
     */
    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
        if (!JadeStringUtil.isEmpty(forDateValable)) {
            setWhereKey(RentePontSearchModel.WITH_DATE_VALABLE);
        }
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
     * @param forIdSituationFamiliale
     *            the forIdSituationFamiliale to set
     */
    public void setForIdSituationFamiliale(String forIdSituationFamiliale) {
        this.forIdSituationFamiliale = forIdSituationFamiliale;
    }

    /**
     * @param forMoisEnCours
     *            the forMoisEnCours to set
     */
    public void setForMoisEnCours(String forMoisEnCours) {
        if (!JadeStringUtil.isEmpty(forMoisEnCours)) {
            setWhereKey(RentePontSearchModel.SEARCH_EN_COURS);
            setForMoisPmtMensuel(forMoisEnCours);
            this.forMoisEnCours = "01." + forMoisEnCours;
        }
    }

    public void setForMoisPmtMensuel(String forMoisPmtMensuel) {
        this.forMoisPmtMensuel = forMoisPmtMensuel;
    }

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

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    @Override
    public Class whichModelClass() {
        return RentePont.class;
    }

}
