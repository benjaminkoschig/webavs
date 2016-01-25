package globaz.musca.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.musca.api.IFAModuleImpression;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IFAModuleImpressionHelper extends GlobazHelper implements IFAModuleImpression {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IFAModuleImpressionHelper
     */
    public IFAModuleImpressionHelper() {
        super("globaz.musca.db.facturation.FAModuleImpression");
    }

    /**
     * Constructeur du type IFAModuleImpressionHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IFAModuleImpressionHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IFAModuleImpressionHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IFAModuleImpressionHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public java.lang.String getIdCritereDecompte() {
        return (java.lang.String) _getValueObject().getProperty("idCritereDecompte");
    }

    @Override
    public java.lang.String getIdModeRecouvrement() {
        return (java.lang.String) _getValueObject().getProperty("idModeRecouvrement");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdModuleImpression() {
        return (java.lang.String) _getValueObject().getProperty("idModuleImpression");
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
    public java.lang.String getNomClasse() {
        return (java.lang.String) _getValueObject().getProperty("nomClasse");
    }

    @Override
    public void setIdCritereDecompte(java.lang.String newIdCritereDecompte) {
        _getValueObject().setProperty("idCritereDecompte", newIdCritereDecompte);
    }

    @Override
    public void setIdModeRecouvrement(java.lang.String newIdModeRecouvrement) {
        _getValueObject().setProperty("idModeRecouvrement", newIdModeRecouvrement);
    }

    /**
     * Setter
     */
    @Override
    public void setIdModuleImpression(java.lang.String newIdModuleImpression) {
        _getValueObject().setProperty("idModuleImpression", newIdModuleImpression);
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
    public void setNomClasse(java.lang.String newNomClasse) {
        _getValueObject().setProperty("nomClasse", newNomClasse);
    }
}
