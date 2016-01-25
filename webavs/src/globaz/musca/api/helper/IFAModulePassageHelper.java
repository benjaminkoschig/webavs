package globaz.musca.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.musca.api.IFAModulePassage;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IFAModulePassageHelper extends GlobazHelper implements IFAModulePassage {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 */
    public IFAModulePassageHelper() {
        super("globaz.musca.db.facturation.FAModulePassage");
    }

    /**
     * Constructeur du type IFAModulePassageHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IFAModulePassageHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IFAModulePassageHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IFAModulePassageHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public java.lang.String getIdAction() {
        return (java.lang.String) _getValueObject().getProperty("idAction");
    }

    @Override
    public java.lang.String getIdModuleFacturation() {
        return (java.lang.String) _getValueObject().getProperty("idModuleFacturation");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdPassage() {
        return (java.lang.String) _getValueObject().getProperty("idPassage");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:13:30)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNiveauAppel() {
        return (String) _getValueObject().getProperty("niveauAppel");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:14:03)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNomClasse() {
        return (String) _getValueObject().getProperty("nomClasse");
    }

    @Override
    public void setEstGenere(java.lang.Boolean newEstGenere) {
        _getValueObject().setProperty("estGenere", newEstGenere);
    }

    @Override
    public void setIdAction(java.lang.String newIdAction) {
        _getValueObject().setProperty("idAction", newIdAction);
    }

    @Override
    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation) {
        _getValueObject().setProperty("idModuleFacturation", newIdModuleFacturation);
    }

    /**
     * Setter
     */
    @Override
    public void setIdPassage(java.lang.String newIdPassage) {
        _getValueObject().setProperty("idPassage", newIdPassage);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:13:30)
     * 
     * @return java.lang.String
     */
    @Override
    public void setNiveauAppel(String niveauAppel) {
        _getValueObject().setProperty("niveauAppel", niveauAppel);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:14:03)
     * 
     * @return java.lang.String
     */
    @Override
    public void setNomClasse(String nomClasse) {
        _getValueObject().setProperty("nomClasse", nomClasse);
    }
}
