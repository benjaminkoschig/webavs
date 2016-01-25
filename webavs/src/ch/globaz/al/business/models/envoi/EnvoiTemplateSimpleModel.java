/**
 * 
 */
package ch.globaz.al.business.models.envoi;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author dhi
 * 
 *         Classe de modèle de données Envoi Template >> table ALTEMENV
 * 
 */
public class EnvoiTemplateSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String codeEtatDossier = null;

    private String idEnvoiTemplate = null;

    private String idFormule = null;

    private String typeGed = null;

    /**
     * @return the codeEtatDossier
     */
    public String getCodeEtatDossier() {
        return codeEtatDossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdEnvoiTemplate();
    }

    /**
     * @return the idEnvoiTemplate
     */
    public String getIdEnvoiTemplate() {
        return idEnvoiTemplate;
    }

    /**
     * @return the idFormule
     */
    public String getIdFormule() {
        return idFormule;
    }

    /**
     * @return the typeGed
     */
    public String getTypeGed() {
        return typeGed;
    }

    /**
     * @param codeEtatDossier
     *            the codeEtatDossier to set
     */
    public void setCodeEtatDossier(String codeEtatDossier) {
        this.codeEtatDossier = codeEtatDossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        setIdEnvoiTemplate(id);
    }

    /**
     * @param idEnvoiTemplate
     *            the idEnvoiTemplate to set
     */
    public void setIdEnvoiTemplate(String idEnvoiTemplate) {
        this.idEnvoiTemplate = idEnvoiTemplate;
    }

    /**
     * @param idFormule
     *            the idFormule to set
     */
    public void setIdFormule(String idFormule) {
        this.idFormule = idFormule;
    }

    /**
     * @param typeGed
     *            the typeGed to set
     */
    public void setTypeGed(String typeGed) {
        this.typeGed = typeGed;
    }

}
