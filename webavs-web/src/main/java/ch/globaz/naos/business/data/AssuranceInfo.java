package ch.globaz.naos.business.data;

import java.util.ArrayList;
import java.util.List;

public class AssuranceInfo {

    private String canton;
    private String categorieCotisant;
    private String codeCaisseProf;
    private Boolean couvert;
    private String dateDebutAffiliation;
    private String dateDebutCotisation;
    private String dateFinAffiliation;
    private String dateFinCotisation;
    private String designation;
    private String designationAbrege;
    private String genreAffiliation;
    private String idAffiliation;
    private String idAssurance;
    private String idCotisation;
    private String idTiersAffiliation;
    private String idTiersCaisseProf;
    private String langue;
    private String libelleCourt;
    private String libelleLong;
    private String numeroAffilie;
    private String periodicitieAffiliation;
    private String periodicitieCotisation;
    private String titre;

    /**
     * Conteneur de messages d'avertissement
     */
    private List<String> warningsContainer = new ArrayList<String>();

    public String getCanton() {
        return canton;
    }

    public String getCategorieCotisant() {
        return categorieCotisant;
    }

    public String getCodeCaisseProf() {
        return codeCaisseProf;
    }

    public Boolean getCouvert() {
        return couvert;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateDebutCotisation() {
        return dateDebutCotisation;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateFinCotisation() {
        return dateFinCotisation;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDesignationAbrege() {
        return designationAbrege;
    }

    public String getGenreAffiliation() {
        return genreAffiliation;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    public String getIdTiersAffiliation() {
        return idTiersAffiliation;
    }

    public String getIdTiersCaisseProf() {
        return idTiersCaisseProf;
    }

    public String getLangue() {
        return langue;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getPeriodicitieAffiliation() {
        return periodicitieAffiliation;
    }

    public String getPeriodicitieCotisation() {
        return periodicitieCotisation;
    }

    public String getTitre() {
        return titre;
    }

    public List<String> getWarningsContainer() {
        return warningsContainer;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCategorieCotisant(String categorieCotisant) {
        this.categorieCotisant = categorieCotisant;
    }

    public void setCodeCaisseProf(String codeCaisseProf) {
        this.codeCaisseProf = codeCaisseProf;
    }

    public void setCouvert(Boolean couvert) {
        this.couvert = couvert;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutCotisation(String dateDebutCotisation) {
        this.dateDebutCotisation = dateDebutCotisation;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinCotisation(String dateFinCotisation) {
        this.dateFinCotisation = dateFinCotisation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setDesignationAbrege(String designationAbrege) {
        this.designationAbrege = designationAbrege;
    }

    public void setGenreAffiliation(String genreAffiliation) {
        this.genreAffiliation = genreAffiliation;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
    }

    public void setIdTiersAffiliation(String idTiersAffiliation) {
        this.idTiersAffiliation = idTiersAffiliation;
    }

    public void setIdTiersCaisseProf(String idTiersCaisseProf) {
        this.idTiersCaisseProf = idTiersCaisseProf;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setPeriodicitieAffiliation(String periodicitieAffiliation) {
        this.periodicitieAffiliation = periodicitieAffiliation;
    }

    public void setPeriodicitieCotisation(String periodicitieCotisation) {
        this.periodicitieCotisation = periodicitieCotisation;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setWarningsContainer(List<String> warningsContainer) {
        this.warningsContainer = warningsContainer;
    }

    public String getLibelleLong() {
        return libelleLong;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

}
