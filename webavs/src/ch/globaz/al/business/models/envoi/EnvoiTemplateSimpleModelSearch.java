/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dhi
 * 
 *         Modèle de recherche pour les élément envoi template
 * 
 */
public class EnvoiTemplateSimpleModelSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCodeEtatDossier = null;

    private String forIdEnvoiTemplate = null;

    private String forIdFormule = null;

    private String forTypeGed = null;

    /**
     * @return the forCodeEtatDossier
     */
    public String getForCodeEtatDossier() {
        return forCodeEtatDossier;
    }

    /**
     * @return the forIdEnvoiTemplate
     */
    public String getForIdEnvoiTemplate() {
        return forIdEnvoiTemplate;
    }

    /**
     * @return the forIdFormule
     */
    public String getForIdFormule() {
        return forIdFormule;
    }

    /**
     * @return the forTypeGed
     */
    public String getForTypeGed() {
        return forTypeGed;
    }

    /**
     * @param forCodeEtatDossier
     *            the forCodeEtatDossier to set
     */
    public void setForCodeEtatDossier(String forCodeEtatDossier) {
        this.forCodeEtatDossier = forCodeEtatDossier;
    }

    /**
     * @param forIdEnvoiTemplate
     *            the forIdEnvoiTemplate to set
     */
    public void setForIdEnvoiTemplate(String forIdEnvoiTemplate) {
        this.forIdEnvoiTemplate = forIdEnvoiTemplate;
    }

    /**
     * @param forIdFormule
     *            the forIdFormule to set
     */
    public void setForIdFormule(String forIdFormule) {
        this.forIdFormule = forIdFormule;
    }

    /**
     * @param forTypeGed
     *            the forTypeGed to set
     */
    public void setForTypeGed(String forTypeGed) {
        this.forTypeGed = forTypeGed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<EnvoiTemplateSimpleModel> whichModelClass() {
        return EnvoiTemplateSimpleModel.class;
    }

}
