package ch.globaz.param.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ParameterModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutValidite = null;
    private String designationParametre = null;
    private String idActeurParametre = null;
    private String idApplParametre = null;
    private String idCleDiffere = null;
    private String idCodeSysteme = null;
    private String idParam = null;
    private String idTypeCode = null;
    private String plageValDeParametre = null;
    private String plageValFinParametre = null;
    private String uniteParametre = null;
    private String valeurAlphaParametre = null;
    private String valeurNumParametre = null;

    public String getDateDebutValidite() {
        return dateDebutValidite;
    }

    public String getDesignationParametre() {
        return designationParametre;
    }

    @Override
    public String getId() {
        return idParam;
    }

    public String getIdActeurParametre() {
        return idActeurParametre;
    }

    public String getIdApplParametre() {
        return idApplParametre;
    }

    public String getIdCleDiffere() {
        return idCleDiffere;
    }

    public String getIdCodeSysteme() {
        return idCodeSysteme;
    }

    public String getIdParam() {
        return idParam;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public String getPlageValDeParametre() {
        return plageValDeParametre;
    }

    public String getPlageValFinParametre() {
        return plageValFinParametre;
    }

    public String getUniteParametre() {
        return uniteParametre;
    }

    public String getValeurAlphaParametre() {
        return valeurAlphaParametre;
    }

    public String getValeurNumParametre() {
        return valeurNumParametre;
    }

    public void setDateDebutValidite(String dateDebutValidite) {
        this.dateDebutValidite = dateDebutValidite;
    }

    public void setDesignationParametre(String designationParametre) {
        this.designationParametre = designationParametre;
    }

    @Override
    public void setId(String id) {
        idParam = id;

    }

    public void setIdActeurParametre(String idActeurParametre) {
        this.idActeurParametre = idActeurParametre;
    }

    public void setIdApplParametre(String idApplParametre) {
        this.idApplParametre = idApplParametre;
    }

    public void setIdCleDiffere(String idCleDiffere) {
        this.idCleDiffere = idCleDiffere;
    }

    public void setIdCodeSysteme(String idCodeSysteme) {
        this.idCodeSysteme = idCodeSysteme;
    }

    public void setIdParam(String idParam) {
        this.idParam = idParam;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public void setPlageValDeParametre(String plageValDeParametre) {
        this.plageValDeParametre = plageValDeParametre;
    }

    public void setPlageValFinParametre(String plageValFinParametre) {
        this.plageValFinParametre = plageValFinParametre;
    }

    public void setUniteParametre(String uniteParametre) {
        this.uniteParametre = uniteParametre;
    }

    public void setValeurAlphaParametre(String valeurAlphaParametre) {
        this.valeurAlphaParametre = valeurAlphaParametre;
    }

    public void setValeurNumParametre(String valeurNumParametre) {
        this.valeurNumParametre = valeurNumParametre;
    }

}
