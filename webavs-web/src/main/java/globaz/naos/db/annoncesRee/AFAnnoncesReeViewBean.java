/**
 * 
 */
package globaz.naos.db.annoncesRee;

import globaz.naos.db.AFAbstractViewBean;
import globaz.naos.properties.AFProperties;
import ch.globaz.common.properties.PropertiesException;

/**
 * @author est
 *         27.02.2017
 */
public class AFAnnoncesReeViewBean extends AFAbstractViewBean {
    private String typeAnnonce;
    private String nomReference;
    private String telephoneReference;
    private String emailReference;
    private String departementReference;
    private String infoReferences;
    private String nbAnnonces;
    private Boolean isValidationUnitaire;
    private String destinataire;

    // initialise les données properties en les récupérant dans la BDD
    public void initProperties() throws PropertiesException {

        nomReference = AFProperties.REE_NOM_REFERENCE.getValue();
        telephoneReference = AFProperties.REE_TELEPHONE_REFERENCE.getValue();
        emailReference = AFProperties.REE_EMAIL_REFERENCE.getValue();
        departementReference = AFProperties.REE_DEPARTEMENT_REFERENCE.getValue();
        infoReferences = AFProperties.REE_INFO_COMPLEMENTAIRE_REFERENCE.getValue();
        nbAnnonces = AFProperties.REE_NB_PACKAGE.getValue();

        String isValidationUnitaireString = AFProperties.REE_VALIDATION_UNITAIRE.getValue();
        if ("true".equalsIgnoreCase(isValidationUnitaireString) || "false".equalsIgnoreCase(isValidationUnitaireString)) {
            isValidationUnitaire = Boolean.parseBoolean(isValidationUnitaireString);
        } else {
            isValidationUnitaire = false;
        }

        destinataire = AFProperties.REE_DESTINATAIRE.getValue();

    }

    // Ajout de l'attribut checked dans la jsp
    public String checkIfChecked() {
        if (isValidationUnitaire) {
            return "checked=\"checked\"";
        } else {
            return "";
        }
    }

    /*
     * Getters
     */

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public String getNomReference() {
        return nomReference;
    }

    public String getTelephoneReference() {
        return telephoneReference;
    }

    public String getEmailReference() {
        return emailReference;
    }

    public String getDepartementReference() {
        return departementReference;
    }

    public String getInfoReferences() {
        return infoReferences;
    }

    public String getNbAnnonces() {
        return nbAnnonces;
    }

    public Boolean getIsValidationUnitaire() {
        return isValidationUnitaire;
    }

    public String getDestinataire() {
        return destinataire;
    }

    /*
     * Setters
     */
    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }

    public void setNomReference(String nomReference) {
        this.nomReference = nomReference;
    }

    public void setTelephoneReference(String telephoneReference) {
        this.telephoneReference = telephoneReference;
    }

    public void setEmailReference(String emailReference) {
        this.emailReference = emailReference;
    }

    public void setDepartementReference(String departementReference) {
        this.departementReference = departementReference;
    }

    public void setInfoReferences(String infoReferences) {
        this.infoReferences = infoReferences;
    }

    public void setNbAnnonces(String nbAnnonces) {
        this.nbAnnonces = nbAnnonces;
    }

    public void setIsValidationUnitaire(Boolean isValidationUnitaire) {
        this.isValidationUnitaire = isValidationUnitaire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }
}
