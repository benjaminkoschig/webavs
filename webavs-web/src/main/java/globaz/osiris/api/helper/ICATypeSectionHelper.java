package globaz.osiris.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.osiris.api.APITypeSection;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICATypeSectionHelper extends GlobazHelper implements APITypeSection {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICATypeSectionHelper
     */
    public ICATypeSectionHelper() {
        super("globaz.osiris.db.comptes.CATypeSection");
    }

    /**
     * Constructeur du type ICATypeSectionHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICATypeSectionHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICATypeSectionHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICATypeSectionHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDescription() {
        return (java.lang.String) _getValueObject().getProperty("description");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.12.2001 11:02:39)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdentificationSource() {
        return (String) _getValueObject().getProperty("identificationSource");
    }

    @Override
    public java.lang.String getIdSequenceContentieux() {
        return (java.lang.String) _getValueObject().getProperty("idSequenceContentieux");
    }

    public String getIdSequenceContentieuxAquila() {
        return (java.lang.String) _getValueObject().getProperty("idSequenceContentieuxAquila");
    }

    @Override
    public java.lang.String getIdTraduction() {
        return (java.lang.String) _getValueObject().getProperty("idTraduction");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdTypeSection() {
        return (java.lang.String) _getValueObject().getProperty("idTypeSection");
    }

    @Override
    public java.lang.String getNomClasse() {
        return (java.lang.String) _getValueObject().getProperty("nomClasse");
    }

    @Override
    public java.lang.String getNomPageDetail() {
        return (java.lang.String) _getValueObject().getProperty("nomPageDetail");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 18:26:06)
     * 
     * @return globaz.osiris.api.APISectionDescriptor
     */
    @Override
    public globaz.osiris.api.APISectionDescriptor getSectionDescriptor() {
        return (globaz.osiris.api.APISectionDescriptor) _getValueObject().getProperty("sectionDescriptor");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(java.lang.String newDescription) throws Exception {
        _getValueObject().setProperty("description", newDescription);
    }

    @Override
    public void setDescriptionDe(String newDescription) throws Exception {
        _getValueObject().setProperty("descriptionDe", newDescription);
    }

    @Override
    public void setDescriptionFr(String newDescription) throws Exception {
        _getValueObject().setProperty("descriptionFr", newDescription);
    }

    @Override
    public void setDescriptionIt(String newDescription) throws Exception {
        _getValueObject().setProperty("descriptionIt", newDescription);
    }

    @Override
    public void setIdSequenceContentieux(java.lang.String newIdSequenceContentieux) {
        _getValueObject().setProperty("idSequenceContentieux", newIdSequenceContentieux);
    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        _getValueObject().setProperty("idTraduction", newIdTraduction);
    }

    /**
     * Setter
     */
    @Override
    public void setIdTypeSection(java.lang.String newIdTypeSection) {
        _getValueObject().setProperty("idTypeSection", newIdTypeSection);
    }

    @Override
    public void setNomClasse(java.lang.String newNomClasse) {
        _getValueObject().setProperty("nomClasse", newNomClasse);
    }

    @Override
    public void setNomPageDetail(java.lang.String newNomPageDetail) {
        _getValueObject().setProperty("nomPageDetail", newNomPageDetail);
    }
}
