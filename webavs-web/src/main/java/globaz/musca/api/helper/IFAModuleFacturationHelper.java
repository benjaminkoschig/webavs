package globaz.musca.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.musca.api.IFAModuleFacturation;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IFAModuleFacturationHelper extends GlobazHelper implements IFAModuleFacturation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IFAModuleFacturationHelper
     */
    public IFAModuleFacturationHelper() {
        super("globaz.musca.db.facturation.FAModuleFacturation");
    }

    /**
     * Constructeur du type IFAModuleFacturationHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IFAModuleFacturationHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IFAModuleFacturationHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IFAModuleFacturationHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdModuleFacturation() {
        return (java.lang.String) _getValueObject().getProperty("idModuleFacturation");
    }

    @Override
    public java.lang.String getIdTypeModule() {
        return (java.lang.String) _getValueObject().getProperty("idTypeModule");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:20:10)
     * 
     * @return int
     */
    @Override
    public java.lang.String getLibelle() {
        return (java.lang.String) _getValueObject().getProperty("libelle");
    }

    @Override
    public java.lang.String getLibelleDe() {
        return (java.lang.String) _getValueObject().getProperty("libelleDe");
    }

    @Override
    public java.lang.String getLibelleFr() {
        return (java.lang.String) _getValueObject().getProperty("libelleFr");
    }

    @Override
    public java.lang.String getLibelleIt() {
        return (java.lang.String) _getValueObject().getProperty("libelleIt");
    }

    @Override
    public java.lang.String getNiveauAppel() {
        return (java.lang.String) _getValueObject().getProperty("niveauAppel");
    }

    @Override
    public java.lang.String getNomClasse() {
        return (java.lang.String) _getValueObject().getProperty("nomClasse");
    }

    /**
     * Setter
     */
    @Override
    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation) {
        _getValueObject().setProperty("idModuleFacturation", newIdModuleFacturation);
    }

    @Override
    public void setIdTypeModule(java.lang.String newIdTypeModule) {
        _getValueObject().setProperty("idTypeModule", newIdTypeModule);
    }

    @Override
    public void setLibelleDe(java.lang.String newLibelleDe) {
        _getValueObject().setProperty("libelleDe", newLibelleDe);
    }

    @Override
    public void setLibelleFr(java.lang.String newLibelleFr) {
        _getValueObject().setProperty("libelleFr", newLibelleFr);
    }

    @Override
    public void setLibelleIt(java.lang.String newLibelleIt) {
        _getValueObject().setProperty("libelleIt", newLibelleIt);
    }

    @Override
    public void setModifierAfact(java.lang.Boolean newModifierAfact) {
        _getValueObject().setProperty("modifierAfact", newModifierAfact);
    }

    @Override
    public void setNiveauAppel(java.lang.String newNiveauAppel) {
        _getValueObject().setProperty("niveauAppel", newNiveauAppel);
    }

    @Override
    public void setNomClasse(java.lang.String newNomClasse) {
        _getValueObject().setProperty("nomClasse", newNomClasse);
    }
}
