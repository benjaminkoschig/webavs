package globaz.osiris.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.osiris.api.APICompteCourant;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APITauxRubriques;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICARubriqueHelper extends GlobazHelper implements APIRubrique {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICARubriqueHelper
     */
    public ICARubriqueHelper() {
        super("globaz.osiris.db.comptes.CARubrique");
    }

    /**
     * Constructeur du type ICARubriqueHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICARubriqueHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICARubriqueHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICARubriqueHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:01:41)
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
    public java.lang.String getAnneeCotisation() {
        return (java.lang.String) _getValueObject().getProperty("anneeCotisation");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 14:00:41)
     * 
     * @return globaz.osiris.db.comptes.CACompteCourant
     */
    @Override
    public APICompteCourant getCompteCourant() {
        return (APICompteCourant) _getValueObject().getProperty("compteCourant");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.12.2001 15:16:55)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescription() {
        return (String) _getValueObject().getProperty("description");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.12.2001 15:16:55)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescription(String langue) {
        try {
            return (String) _getObject("getDescription", new Object[] { langue });
        } catch (Exception e) {
            return this.getDescription();
        }
    }

    @Override
    public java.lang.Boolean getEstVentilee() {
        return (java.lang.Boolean) _getValueObject().getProperty("estVentilee");
    }

    @Override
    public java.lang.String getIdContrepartie() {
        return (java.lang.String) _getValueObject().getProperty("idContrepartie");
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
    public java.lang.String getIdExterne() {
        return (java.lang.String) _getValueObject().getProperty("idExterne");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 13:58:09)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdExterneCompteCourantEcran() {
        return (java.lang.String) _getValueObject().getProperty("idExterneCompteCourantEcran");
    }

    @Override
    public java.lang.String getIdRubrique() {
        return (java.lang.String) _getValueObject().getProperty("idRubrique");
    }

    @Override
    public java.lang.String getIdSecteur() {
        return (java.lang.String) _getValueObject().getProperty("idSecteur");
    }

    @Override
    public java.lang.String getIdTraduction() {
        return (java.lang.String) _getValueObject().getProperty("idTraduction");
    }

    @Override
    public java.lang.String getNatureRubrique() {
        return (java.lang.String) _getValueObject().getProperty("natureRubrique");
    }

    @Override
    public java.lang.String getNumCompteCG() {
        return (java.lang.String) _getValueObject().getProperty("numCompteCG");
    }

    /**
     * Récupère le numéro de compte pour la mise en compte en comptabilité générale Date de création : (28.10.2002
     * 10:48:53)
     * 
     * @return java.lang.String le numéro de compte en comptabilité générale
     */
    @Override
    public String getNumeroComptePourCG() {
        return (String) _getValueObject().getProperty("numeroComptePourCG");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.01.2002 10:36:57)
     * 
     * @return globaz.osiris.db.comptes.CARubrique
     */
    @Override
    public APITauxRubriques getTauxRubriques() {
        return (APITauxRubriques) _getValueObject().getProperty("tauxRubriques");
    }

    @Override
    public java.lang.Boolean getTenirCompteur() {
        return (java.lang.Boolean) _getValueObject().getProperty("tenirCompteur");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2002 18:01:41)
     * 
     * @param newAlias
     *            java.lang.String
     */
    @Override
    public void setAlias(java.lang.String newAlias) {
        _getValueObject().setProperty("alias", newAlias);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (24.02.2003 10:02:32)
     * 
     * @param alternateKey
     *            java.lang.String
     */
    @Override
    public void setAlternateKey(int alternateKey) {
        _getValueObject().setProperty("alternateKey", alternateKey);
    }

    /**
     * Setter
     */
    @Override
    public void setAnneeCotisation(java.lang.String newAnneeCotisation) {
        _getValueObject().setProperty("anneeCotisation", newAnneeCotisation);
    }

    /**
     * Description dans la langue de l'utilisateur Date de création : (19.12.2001 10:55:21)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(String newDescription) {
        _getValueObject().setProperty("description", newDescription);
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    @Override
    public void setDescriptionDe(String newDescription) {
        _getValueObject().setProperty("descriptionDe", newDescription);
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    @Override
    public void setDescriptionFr(String newDescription) {
        _getValueObject().setProperty("descriptionFr", newDescription);
    }

    /**
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02)
     * 
     * @param newDescription
     *            java.lang.String
     * @param codeISOLangue
     *            java.lang.String
     */
    @Override
    public void setDescriptionIt(String newDescription) {
        _getValueObject().setProperty("descriptionIt", newDescription);
    }

    @Override
    public void setEstVentilee(java.lang.Boolean newEstVentilee) {
        _getValueObject().setProperty("estVentilee", newEstVentilee);
    }

    @Override
    public void setIdContrepartie(java.lang.String newIdContrepartie) {
        _getValueObject().setProperty("idContrepartie", newIdContrepartie);
    }

    @Override
    public void setIdExterne(java.lang.String newIdExterne) {
        _getValueObject().setProperty("idExterne", newIdExterne);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 13:58:09)
     * 
     * @param newIdExterneCompteCourantEcran
     *            java.lang.String
     */
    @Override
    public void setIdExterneCompteCourantEcran(java.lang.String newIdExterneCompteCourantEcran) {
        _getValueObject().setProperty("idExterneCompteCourantEcran", newIdExterneCompteCourantEcran);
    }

    @Override
    public void setIdRubrique(java.lang.String newIdRubrique) {
        _getValueObject().setProperty("idRubrique", newIdRubrique);
    }

    @Override
    public void setIdSecteur(java.lang.String newIdSecteur) {
        _getValueObject().setProperty("idSecteur", newIdSecteur);
    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        _getValueObject().setProperty("idTraduction", newIdTraduction);
    }

    @Override
    public void setNatureRubrique(java.lang.String newNatureRubrique) {
        _getValueObject().setProperty("natureRubrique", newNatureRubrique);
    }

    @Override
    public void setNumCompteCG(java.lang.String newNumCompteCG) {
        _getValueObject().setProperty("numCompteCG", newNumCompteCG);
    }

    @Override
    public void setTenirCompteur(java.lang.Boolean newTenirCompteur) {
        _getValueObject().setProperty("tenirCompteur", newTenirCompteur);
    }
}
