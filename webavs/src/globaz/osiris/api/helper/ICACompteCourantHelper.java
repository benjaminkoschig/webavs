package globaz.osiris.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIRubrique;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICACompteCourantHelper extends GlobazHelper implements APICompteCourant {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICACompteCourantHelper
     */
    public ICACompteCourantHelper() {
        super("globaz.osiris.db.comptes.CACompteCourant");
    }

    /**
     * Constructeur du type ICACompteCourantHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICACompteCourantHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICACompteCourantHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICACompteCourantHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public Boolean getAccepterVentilation() {
        return (Boolean) _getValueObject().getProperty("accepterVentilation");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:00:03)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAlias() {
        return (java.lang.String) _getValueObject().getProperty("alias");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdCompteCourant() {
        return (java.lang.String) _getValueObject().getProperty("idCompteCourant");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 15:27:37)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdExterne() {
        return (java.lang.String) _getValueObject().getProperty("idExterne");
    }

    @Override
    public java.lang.String getIdRubrique() {
        return (java.lang.String) _getValueObject().getProperty("idRubrique");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2002 08:41:20)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getPriorite() {
        return (java.lang.String) _getValueObject().getProperty("priorite");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 08:55:03)
     * 
     * @return globaz.osiris.api.APIRubrique
     */
    @Override
    public APIRubrique getRubrique() {
        GlobazValueObject vo = (GlobazValueObject) _getValueObject().getProperty("rubrique");
        return new ICARubriqueHelper(vo);
    }

    @Override
    public java.lang.String getSolde() {
        return (java.lang.String) _getValueObject().getProperty("solde");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.01.2002 15:07:39)
     * 
     * @return java.lang.String
     */
    @Override
    public String getSoldeFormate() {
        return (String) _getValueObject().getProperty("soldeFormate");
    }

    @Override
    public void setAccepterVentilation(Boolean newAccepterVentilation) {
        _getValueObject().setProperty("accepterVentilation", newAccepterVentilation);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:00:03)
     * 
     * @param newAlias
     *            java.lang.String
     */
    @Override
    public void setAlias(java.lang.String newAlias) {
        _getValueObject().setProperty("alias", newAlias);
    }

    /**
     * Setter
     */
    @Override
    public void setIdCompteCourant(java.lang.String newIdCompteCourant) {
        _getValueObject().setProperty("idCompteCourant", newIdCompteCourant);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 15:27:37)
     * 
     * @param newIdExterne
     *            java.lang.String
     */
    @Override
    public void setIdExterne(java.lang.String newIdExterne) {
        _getValueObject().setProperty("idExterne", newIdExterne);
    }

    @Override
    public void setIdRubrique(java.lang.String newIdRubrique) {
        _getValueObject().setProperty("idRubrique", newIdRubrique);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2002 08:41:20)
     * 
     * @param newPriorite
     *            java.lang.String
     */
    @Override
    public void setPriorite(java.lang.String newPriorite) {
        _getValueObject().setProperty("priorite", newPriorite);
    }

    @Override
    public void setSolde(java.lang.String newSolde) {
        _getValueObject().setProperty("solde", newSolde);
    }
}
