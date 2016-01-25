package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Représente un taux lié à une assurance dans le module d'affiliation (NAOS).
 * 
 * @since WebBMS 0.4.1
 */
public class TauxAssuranceSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -324050758407120748L;

    private String tauxAssuranceId;
    private String assuranceId;
    private String dateDebut;
    private String dateFin;
    private String codeSexe;
    private String genreValeur;
    private String valeurEmployeur;
    private String valeurEmploye;
    private String fraction;
    private String periodiciteMontant;
    private String rang;
    private String tranche;
    private String typeTaux;
    private String categorieTaux;

    @Override
    public String getId() {
        return getTauxAssuranceId();
    }

    @Override
    public void setId(String id) {
        setTauxAssuranceId(id);
    }

    /**
     * @return the tauxAssuranceId
     */
    public String getTauxAssuranceId() {
        return tauxAssuranceId;
    }

    /**
     * @return the assuranceId
     */
    public String getAssuranceId() {
        return assuranceId;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the codeSexe
     */
    public String getCodeSexe() {
        return codeSexe;
    }

    /**
     * @return the genreValeur
     */
    public String getGenreValeur() {
        return genreValeur;
    }

    /**
     * @return the valeurEmployeur
     */
    public String getValeurEmployeur() {
        return valeurEmployeur;
    }

    /**
     * @return the valeurEmploye
     */
    public String getValeurEmploye() {
        return valeurEmploye;
    }

    /**
     * @return the fraction
     */
    public String getFraction() {
        return fraction;
    }

    /**
     * @return the periodiciteMontant
     */
    public String getPeriodiciteMontant() {
        return periodiciteMontant;
    }

    /**
     * @return the rang
     */
    public String getRang() {
        return rang;
    }

    /**
     * @return the tranche
     */
    public String getTranche() {
        return tranche;
    }

    /**
     * @return the typeTaux
     */
    public String getTypeTaux() {
        return typeTaux;
    }

    /**
     * @return the categorieTaux
     */
    public String getCategorieTaux() {
        return categorieTaux;
    }

    /**
     * @param tauxAssuranceId the tauxAssuranceId to set
     */
    public void setTauxAssuranceId(String tauxAssuranceId) {
        this.tauxAssuranceId = tauxAssuranceId;
    }

    /**
     * @param assuranceId the assuranceId to set
     */
    public void setAssuranceId(String assuranceId) {
        this.assuranceId = assuranceId;
    }

    /**
     * @param dateDebut the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param codeSexe the codeSexe to set
     */
    public void setCodeSexe(String codeSexe) {
        this.codeSexe = codeSexe;
    }

    /**
     * @param genreValeur the genreValeur to set
     */
    public void setGenreValeur(String genreValeur) {
        this.genreValeur = genreValeur;
    }

    /**
     * @param valeurEmployeur the valeurEmployeur to set
     */
    public void setValeurEmployeur(String valeurEmployeur) {
        this.valeurEmployeur = valeurEmployeur;
    }

    /**
     * @param valeurEmploye the valeurEmploye to set
     */
    public void setValeurEmploye(String valeurEmploye) {
        this.valeurEmploye = valeurEmploye;
    }

    /**
     * @param fraction the fraction to set
     */
    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    /**
     * @param periodiciteMontant the periodiciteMontant to set
     */
    public void setPeriodiciteMontant(String periodiciteMontant) {
        this.periodiciteMontant = periodiciteMontant;
    }

    /**
     * @param rang the rang to set
     */
    public void setRang(String rang) {
        this.rang = rang;
    }

    /**
     * @param tranche the tranche to set
     */
    public void setTranche(String tranche) {
        this.tranche = tranche;
    }

    /**
     * @param typeTaux the typeTaux to set
     */
    public void setTypeTaux(String typeTaux) {
        this.typeTaux = typeTaux;
    }

    /**
     * @param categorieTaux the categorieTaux to set
     */
    public void setCategorieTaux(String categorieTaux) {
        this.categorieTaux = categorieTaux;
    }
}
