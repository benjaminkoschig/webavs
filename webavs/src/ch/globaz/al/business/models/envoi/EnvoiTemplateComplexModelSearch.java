/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author dhi
 * 
 */
public class EnvoiTemplateComplexModelSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCsDocument = null;

    private String forIdFormule = null;

    private String forLangue = null;

    private String forLibelleDocument = null;

    /**
     * @return the forCsDocument
     */
    public String getForCsDocument() {
        return forCsDocument;
    }

    /**
     * @return the forIdFormule
     */
    public String getForIdFormule() {
        return forIdFormule;
    }

    /**
     * @return the forLangue
     */
    public String getForLangue() {
        return forLangue;
    }

    /**
     * @return the forLibelleDocument
     */
    public String getForLibelleDocument() {
        return forLibelleDocument;
    }

    /**
     * @param forCsDocument
     *            the forCsDocument to set
     */
    public void setForCsDocument(String forCsDocument) {
        this.forCsDocument = forCsDocument;
    }

    /**
     * @param forIdFormule
     *            the forIdFormule to set
     */
    public void setForIdFormule(String forIdFormule) {
        this.forIdFormule = forIdFormule;
    }

    /**
     * @param forLangue
     *            the forLangue to set
     */
    public void setForLangue(String forLangue) {
        this.forLangue = forLangue;
    }

    /**
     * @param forLibelleDocument
     *            the forLibelleDocument to set
     */
    public void setForLibelleDocument(String forLibelleDocument) {
        this.forLibelleDocument = forLibelleDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<EnvoiTemplateComplexModel> whichModelClass() {
        return EnvoiTemplateComplexModel.class;
    }

}
