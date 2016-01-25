/**
 * 
 */
package ch.globaz.amal.business.models.parametremodel;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author CBU
 * 
 */
public class ParametreModelComplexSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdFormule = null;
    private String forLangue = null;
    private String forlibelleDocument = null;
    private String forNomWord = null;

    public String getForIdFormule() {
        return forIdFormule;
    }

    public String getForLangue() {
        return forLangue;
    }

    public String getForlibelleDocument() {
        return forlibelleDocument + "%";
    }

    public String getForNomWord() {
        if (forNomWord == null) {
            return forNomWord;
        } else {
            return forNomWord.toUpperCase();
        }
    }

    public void setForIdFormule(String forIdFormule) {
        this.forIdFormule = forIdFormule;
    }

    public void setForLangue(String forLangue) {
        this.forLangue = forLangue;
    }

    public void setForlibelleDocument(String forlibelleDocument) {
        this.forlibelleDocument = forlibelleDocument;
    }

    public void setForNomWord(String forNomWord) {
        this.forNomWord = forNomWord;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ParametreModelComplex.class;
    }

}
