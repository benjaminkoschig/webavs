/**
 *
 */
package ch.globaz.amal.business.models.parametreannuel;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author CBU
 *
 */
public class SimpleParametreAnnuel extends JadeSimpleModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String anneeParametre = null;
    private String codeTypeParametre = null;
    private String idParametreAnnuel = null;
    private String valeurParametre = null;
    private String valeurParametreString = null;
    private String valeurFrom = null;
    private String valeurTo = null;

    public String getAnneeParametre() {
        return anneeParametre;
    }

    public String getCodeTypeParametre() {
        return codeTypeParametre;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idParametreAnnuel;
    }

    public String getIdParametreAnnuel() {
        return idParametreAnnuel;
    }

    public String getValeurParametre() {
        return valeurParametre;
    }

    public String getValeurParametreString() {
        return valeurParametreString;
    }

    public String getValeurFrom() {
        return valeurFrom;
    }

    public String getValeurTo() {
        return valeurTo;
    }

    public void setAnneeParametre(String anneeParametre) {
        this.anneeParametre = anneeParametre;
    }

    public void setCodeTypeParametre(String codeTypeParametre) {
        this.codeTypeParametre = codeTypeParametre;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idParametreAnnuel = id;
    }

    public void setIdParametreAnnuel(String idParametreAnnuel) {
        this.idParametreAnnuel = idParametreAnnuel;
    }

    public void setValeurParametre(String valeurParametre) {
        this.valeurParametre = valeurParametre;
    }

    public void setValeurParametreString(String valeurParametreString) {
        this.valeurParametreString = valeurParametreString;
    }

    public void setValeurFrom(String valeurFrom) {
        this.valeurFrom = valeurFrom;
    }

    public void setValeurTo(String valeurTo) {
        this.valeurTo = valeurTo;
    }

}
